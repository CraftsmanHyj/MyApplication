package com.hyj.lib.http.download;

import android.content.Context;
import android.content.Intent;

import com.hyj.lib.Constants;
import com.hyj.lib.db.ThreadDao;
import com.hyj.lib.db.ThreadDaoImpl;
import com.hyj.lib.tools.FileUtils;
import com.hyj.lib.tools.LogUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载任务类
 *
 * @Author hyj
 * @Date 2016-3-21 下午11:24:03
 */
public class DownTask {
    private Context context;
    private FileInfo fileInfo;
    private ThreadDao dao;

    private long progress = 0;// 当前文件下载进度

    /**
     * 文件下载状态
     */
    private String downStatus;

    private int threadCount = 2;// 同时下载该文件的线程数
    private List<DownLoadThread> lThread;// 线程集合
    /**
     * <pre>
     *     多线程暂停的时候，每个线程会暂停一次，
     *     控制只有最后一条线程也暂停的时候才去发送广播
     * </pre>
     */
    private int pausethreadCount = 0;

    /**
     * 线程池管理下载文件线程
     */
    public static ExecutorService esThreadService = Executors.newCachedThreadPool();

    /**
     * <pre>
     *     获取文件下载状态
     * </pre>
     *
     * @return
     */
    public String getDownStatus() {
        return downStatus;
    }

    /**
     * 设置文件下载状态
     *
     * @param downStatus
     */
    public void setDownStatus(String downStatus) {
        this.downStatus = downStatus;
        //准备下载文件，将当前下载进度置0
        if (DownService.ACTION_PREPARE.equals(downStatus)) {
            this.progress = 0;
        }
        fileInfo.setDownStatus(downStatus);
    }

    /**
     * 获取当前下载任务中的文件信息
     *
     * @return
     */
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * 下载线程任务
     *
     * @param context
     * @param file    FileInfo对象
     */
    public DownTask(Context context, FileInfo file) {
        this(context, file, 1);
    }

    /**
     * 下载线程任务
     *
     * @param context
     * @param file        file文件信息对象
     * @param threadCount threadCount下载该文件的线程数量
     */
    public DownTask(Context context, FileInfo file, int threadCount) {
        this.context = context;
        this.fileInfo = file;
        this.threadCount = threadCount;

        this.dao = new ThreadDaoImpl(context);
        setDownStatus(DownService.ACTION_PREPARE);
    }

    /**
     * 启动下载任务
     */
    public void downLoad() {
        // 读取数据库的线程信息
        List<ThreadInfo> lThreadInfo = dao.queryThread(fileInfo.getUrl());
        if (lThreadInfo.size() <= 0) {
            // 获取每个线程现在的长度
            int lengthTh = fileInfo.getLength() / threadCount;

            for (int i = 0; i < threadCount; i++) {
                int start = i * lengthTh;// 下载开始位置
                int end = (i + 1) * lengthTh - 1;// 下载结束位置

                if (i == threadCount - 1) {
                    end = fileInfo.getLength();
                }

                ThreadInfo threadInfo = new ThreadInfo(i, fileInfo.getUrl(), start, end);
                // 添加到线程信息集合中
                lThreadInfo.add(threadInfo);

                // 向数据库中插入当前线程信息
                dao.insertThread(threadInfo);
            }
        }

        lThread = new ArrayList<DownLoadThread>();
        // 启动多个线程进行下载
        for (ThreadInfo thread : lThreadInfo) {
            DownLoadThread down = new DownLoadThread(thread);
            lThread.add(down);

            // 使用线程池来启动、执行线程
            esThreadService.execute(down);
        }

        sendBroadcast(DownService.ACTION_PREPARE);
    }

    /**
     * 判断所有线程是否都已经执行完毕
     */
    private synchronized void checkAllThreadFinished() {
        // 检查每条线程是否都执行完成
        for (DownLoadThread thread : lThread) {
            if (!thread.isFinish()) {
                return;
            }
        }

        dao.deleteThread(fileInfo.getUrl());// 删除线程信息

        sendBroadcast(DownService.ACTION_FINISH); // 通知广播UI下载任务结束
    }

    /**
     * 发送广播，更新UI，启动新任务下载
     *
     * @param action
     */
    private void sendBroadcast(String action) {
        // 通知后端服务开启新的下载任务
        Intent intentService = new Intent(context, DownService.class);
        intentService.setAction(DownService.ACTION_DOWN_OTHER);
        intentService.putExtra(DownService.DOWNINFO, fileInfo);

        switch (action) {
            // 当点击开始下载动作(执行动作的状态)的时候，文件已经进入了正在下载状态(文件下载的状态)
            case DownService.ACTION_PREPARE:
                setDownStatus(DownService.ACTION_START);

                LogUtils.e("文件：" + fileInfo.getFileName() + " 开始下载");
                break;

            case DownService.ACTION_PAUSE:
                if (!allThreadPause()) {
                    return;
                }

                setDownStatus(action);
                context.startService(intentService);

                LogUtils.e("文件：" + fileInfo.getFileName() + " 暂停下载");
                break;

            case DownService.ACTION_STOP:
                if (!allThreadPause()) {
                    return;
                }

                setDownStatus(action);
                LogUtils.e("文件：" + fileInfo.getFileName() + " 停止下载");
                break;

            case DownService.ACTION_FINISH:
                setDownStatus(action);
                context.startService(intentService);

                LogUtils.e("文件：" + fileInfo.getFileName() + " 下载完成");
                break;
        }

        // 通知界面更新UI
        Intent intent = new Intent(action);
        intent.putExtra(DownService.DOWNINFO, fileInfo);
        context.sendBroadcast(intent);
    }

    /**
     * <pre>
     *     判断下载文件的所有线程是否停止
     *     多线程暂停的时候，每个线程会暂停一次，
     *     控制只有最后一条线程也暂停的时候才去发送广播
     * </pre>
     *
     * @return
     */
    private boolean allThreadPause() {
        if (threadCount != (++pausethreadCount)) {
            return false;
        }
        pausethreadCount = 0;// 重置
        return true;
    }

    /**
     * 此线程用于真正去下载文件
     *
     * @Author hyj
     * @Date 2016-1-21 下午4:07:22
     */
    private class DownLoadThread extends Thread {
        private ThreadInfo threadInfo;
        private boolean isFinish;

        private HttpURLConnection con;
        private RandomAccessFile raf;
        private InputStream is;

        /**
         * 当前下载线程是否结束
         *
         * @return
         */
        public boolean isFinish() {
            return isFinish;
        }

        public DownLoadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
            this.isFinish = false;
        }

        @Override
        public void run() {
            // 设置线程下载位置
            try {
                URL url = new URL(threadInfo.getUrl());
                con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setRequestMethod("GET");

                // 设置下载位置
                int start = threadInfo.getStart() + threadInfo.getProgress();
                con.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());

                // 文件写入路径
                String path = File.separator + Constants.DIR_DOWNLOAD + File.separator + fileInfo.getFileName();
                File downFile = FileUtils.getAppFile(context, path);

                raf = new RandomAccessFile(downFile, "rwd");
                // 在读写的时候跳过设置好的字节数，从下一个字节数开始读写
                raf.seek(start);

                Intent intent = new Intent(DownService.ACTION_START);
                intent.putExtra(DownService.DOWNINFO, fileInfo);

                progress += threadInfo.getProgress();// 当前文件下载完成进度

                // 开始下载
                if (HttpStatus.SC_PARTIAL_CONTENT == con.getResponseCode()) {
                    // 读取数据
                    is = con.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {
                        // 写入文件
                        raf.write(buffer, 0, len);

                        // 整个文件的完成进度
                        progress += len;

                        // 当前线程完成的进度
                        threadInfo.setProgress(threadInfo.getProgress() + len);

                        // 每隔500毫秒发送一次广播刷新进度条//此处移到定时器里面执行
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            int percent = (int) (progress * 100 / fileInfo.getLength());
                            LogUtils.i("文件：" + fileInfo.getFileName() + " 进度：" + percent);

                            // 发送广播更新进度条
                            fileInfo.setProgress(percent);
                            context.sendBroadcast(intent);
                        }

                        // 下载暂停时，保存下载进度
                        if (DownService.ACTION_PAUSE.equals(downStatus) || DownService.ACTION_STOP.equals(downStatus)) {
                            dao.updateThread(threadInfo);
                            sendBroadcast(downStatus);
                            return;
                        }
                    }

                    // 标记当前线程执行完毕
                    isFinish = true;

                    // 检查所有下载任务是否执行完毕
                    checkAllThreadFinished();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}