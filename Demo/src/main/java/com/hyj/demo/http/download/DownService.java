package com.hyj.demo.http.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hyj.demo.tools.FileUtils;
import com.hyj.demo.tools.LogUtils;
import com.hyj.demo.Constants;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     下载后台Service
 *     当Service被停止的时候会停止所有下载任务并保持当前下载进度
 * </pre>
 *
 * @author hyj
 * @Date 2016-3-10 下午5:56:00
 */
public class DownService extends Service {
    /**
     * 下载状态——准备
     */
    public static final String ACTION_PREPARE = "ACTION_PREPARE";
    /**
     * 下载状态——开始，文件进入下载任务队列
     */
    public static final String ACTION_START = "ACTION_START";
    /**
     * 下载状态——暂停，暂停当前任务后，还会通知service启动下一个下载任务
     */
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    /**
     * 下载状态——停止，保存下载进度，退出下载
     */
    public static final String ACTION_STOP = "ACTION_STOP";
    /**
     * 下载状态——结束，删除数据库中线程信息
     */
    public static final String ACTION_FINISH = "ACTION_FINISH";
    /**
     * 下载状态——若下载队列中还有未下载的，则让Service继续下载
     */
    public static final String ACTION_DOWN_OTHER = "ACTION_DOWN_OTHER";
    /**
     * ExtraName——下载文件信息
     */
    public static final String DOWNINFO = "fileInfo";

    private final int WHAT_INIT = 0X00000001;// 初始化下载任务

    private final int DOWNTASK_COUNT = 2;// 同时开始下载的文件数
    private int THREADCOUNT = 3;// 一个文件同时下载的线程数
    private int downFileNum = 0;// 当前下载文件数

    // 下载任务集合
    private Map<Integer, DownTask> mapDownTask = new LinkedHashMap<Integer, DownTask>();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INIT:
                    FileInfo file = (FileInfo) msg.obj;

                    // 文件初始化信息完成，启动下载任务
                    DownTask downTask = new DownTask(DownService.this, file, THREADCOUNT);
                    // 将下载任务添加到集合中
                    mapDownTask.put(file.getId(), downTask);

                    downTask(file);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取手机有多少个CPU
        int CPUCount = Runtime.getRuntime().availableProcessors();
        THREADCOUNT = Math.round(THREADCOUNT * 1.0f / DOWNTASK_COUNT);
        LogUtils.e("CUP总数：" + CPUCount + "　文件下载线程数：" + THREADCOUNT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileInfo file = (FileInfo) intent.getSerializableExtra(DOWNINFO);
        DownTask downTask = mapDownTask.get(file.getId());
        String value = intent.getStringExtra("value");
        LogUtils.e("操作来源：" + value + " " + file);

        /**
         * <pre>
         *     开始下载文件有两种情况：
         *          1、新文件下载
         *          2、暂停后继续下载
         * </pre>
         */
        switch (intent.getAction()) {
            case ACTION_PREPARE:
                if (null == downTask) {
                    DownTask.esThreadService.execute(new FileSizeThread(file));
                } else {
                    downTask.setDownStatus(intent.getAction());
                    downTask(file);
                }
                break;

            case ACTION_PAUSE:
            case ACTION_STOP:
                if (null != downTask) {
                    downTask.setDownStatus(intent.getAction());// 暂停下载任务
                }
                break;

            case ACTION_DOWN_OTHER:
                downTask(file);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * <pre>
     *     当绑定(onBind())成功的时候会调用此方法
     * </pre>
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载任务队列，根据设定策略取文件下载
     *
     * @param file
     */
    private void downTask(FileInfo file) {
        DownTask task = mapDownTask.get(file.getId());
        switch (task.getDownStatus()) {
            case ACTION_FINISH:
                mapDownTask.remove(file.getId());
            case ACTION_PAUSE:
                downFileNum--;
                break;
        }

        if (downFileNum < DOWNTASK_COUNT) {
            for (DownTask downTask : mapDownTask.values()) {
                if (ACTION_PREPARE.equals(downTask.getDownStatus())) {
                    downTask.downLoad();
                    //发送广播更新界面
                    Intent intent = new Intent(ACTION_PREPARE);
                    intent.putExtra(DOWNINFO, downTask.getFileInfo());
                    sendBroadcast(intent);

                    if (++downFileNum >= DOWNTASK_COUNT) {
                        break;
                    }
                }
            }
        }

        // 当所有任务都下载完成的时候停止Service
        if (mapDownTask.size() <= 0) {
            stopSelf();
        }
    }

    /**
     * 当销毁Service时停止所有下载任务
     */
    @Override
    public void onDestroy() {
        for (DownTask task : mapDownTask.values()) {
            task.setDownStatus(ACTION_STOP);
        }

        LogUtils.e("销毁Service：" + DownService.class.getName());
        super.onDestroy();
    }

    /**
     * 此线程主要用于获取下载文件总长度信息，不做他用
     *
     * @Author hyj
     * @Date 2016-1-21 下午4:05:38
     */
    private class FileSizeThread extends Thread {
        private FileInfo fileInfo;

        public FileSizeThread(FileInfo mFileInfo) {
            this.fileInfo = mFileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection con = null;
            RandomAccessFile raf = null;
            try {
                // 链接网络文件
                URL url = new URL(fileInfo.getUrl());
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(3000);
                con.setRequestMethod("GET");// 除下载文件外其他都用post方式
                if (HttpStatus.SC_OK != con.getResponseCode()) {
                    return;
                }

                // 获取文件长度
                int length = con.getContentLength();
                if (length <= 0) {// 网络链接或读取文件有问题
                    return;
                }

                // 文件写入路径
                String path = File.separator + Constants.DIR_DOWNLOAD + File.separator + fileInfo.getFileName();
                File file = FileUtils.getAppFile(DownService.this.getBaseContext(), path);

                // 随机访问文件，可以在文件任意位置进行写入操作
                raf = new RandomAccessFile(file, "rwd");
                // 设置文件长度
                raf.setLength(length);

                fileInfo.setLength(length);
                handler.obtainMessage(WHAT_INIT, fileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
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