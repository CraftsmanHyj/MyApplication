package com.hyj.demo.downservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.db.ThreadDao;
import com.hyj.demo.db.ThreadDaoImpl;
import com.hyj.demo.down.DownLoad;
import com.hyj.demo.http.download.DownNotification;
import com.hyj.demo.http.download.DownService;
import com.hyj.demo.http.download.FileInfo;
import com.hyj.demo.tools.DialogUtils;
import com.hyj.demo.tools.ServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件下载测试
 */
public class DownServiceActivity extends BaseActivity {
    private List<FileInfo> lFile;
    private ListView lvDownFile;
    private FileListAdapter adapter;

    private ThreadDao dao;
    private Map<String, Integer> mapProgress;
    private DownNotification notificationUtils;

    private String[][] urls = new String[0][0];//下载网址

    /**
     * 更新UI的广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(DownService.DOWNINFO);
            int index = lFile.indexOf(fileInfo);
            if (index < 0) {//下载列表中不存在
                return;
            }
            FileInfo file = lFile.get(index);
            file.setDownStatus(fileInfo.getDownStatus());
            file.setProgress(fileInfo.getProgress());

            switch (intent.getAction()) {
                case DownService.ACTION_PREPARE:
                    notificationUtils.showNotification(file);//显示通知
                    break;

                case DownService.ACTION_START:
                    //更新通知里的进度
                    notificationUtils.updateNotification(file.getId(), file.getProgress());
                    break;

                case DownService.ACTION_PAUSE:
                    break;

                case DownService.ACTION_STOP:
                    break;

                case DownService.ACTION_FINISH:
                    // 下载完成进度条重置
                    file.setProgress(0);

                    String msg = "文件<" + file.getFileName() + ">下载完成";
                    DialogUtils.showToastShort(DownServiceActivity.this, msg);

                    //下载完成取消通知
                    notificationUtils.cancleNotification(file.getId());
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downservice_main);

        myInit();
        singleFileDown();
    }

    private void myInit() {
        initView();
        initUrls();
        initData();
        registerBroadcast();
    }

    private void initView() {
        lFile = new ArrayList<FileInfo>();
        lvDownFile = (ListView) findViewById(R.id.downListView);
        adapter = new FileListAdapter(this, lFile, R.layout.downservice_item);
        lvDownFile.setAdapter(adapter);
    }

    private void initUrls() {
        boolean isNetWork = false;
        String ip = "192.168.23.1";//公司
        ip = "192.168.31.225";
        if (isNetWork) {
            urls = new String[][]{
                    {"慕课网APK", "http://www.imooc.com/mobile/imooc.apk"},
                    {"酷狗音乐.apk", "http://downmobile.kugou.com/Android/KugouPlayer/7840/KugouPlayer_219_V7.8.4.apk"},
                    {"e代驾.apk", "http://f2.market.xiaomi.com/download/AppStore/0d39f5b3b27bad6601aba10606b63e472f54091c1/cn.edaijia.android.client.apk"},
                    {"界面.apk", "http://f1.market.mi-img.com/download/AppStore/098b6753991fd4fb414aff12e29d9d5db9a0d63d8/com.jiemian.news.apk"},
                    {"功夫熊猫.apk", "http://f2.market.mi-img.com/download/AppStore/006784c9c7551b9bbe1ea0b084a24c96c9a42b795/com.qingguo.gfxiong.apk"}
            };
        } else {
            urls = new String[][]{
                    {"QQ0.apk", "http://" + ip + ":8080/AndroidServer/QQ0.apk"},
                    {"QQ1.apk", "http://" + ip + ":8080/AndroidServer/QQ1.apk"},
                    {"QQ2.apk", "http://" + ip + ":8080/AndroidServer/QQ2.apk"},
                    {"QQ3.apk", "http://" + ip + ":8080/AndroidServer/QQ3.apk"},
                    {"QQ4.apk", "http://" + ip + ":8080/AndroidServer/QQ4.apk"},
                    {"QQ5.apk", "http://" + ip + ":8080/AndroidServer/QQ5.apk"},
                    {"mobile.apk", "http://" + ip + ":8080/AndroidServer/mobile.apk"}
            };
        }
    }

    private void initData() {
        notificationUtils = new DownNotification(this);
        dao = new ThreadDaoImpl(this);
        mapProgress = dao.queryFileProgress();

        for (int i = 0; i < urls.length; i++) {
            Object progress = mapProgress.get(urls[i][1]);
            FileInfo fileInfo = new FileInfo(i, urls[i][1], urls[i][0]);
            fileInfo.setProgress(progress == null ? 0 : (int) progress);
            lFile.add(fileInfo);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownService.ACTION_PREPARE);
        filter.addAction(DownService.ACTION_START);
        filter.addAction(DownService.ACTION_PAUSE);
        filter.addAction(DownService.ACTION_STOP);
        filter.addAction(DownService.ACTION_FINISH);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        ServiceUtils.stopService(this, DownService.class);
        // 注销广播接收者
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * <pre>
     *     单文件、多线程、在线下载文件
     *     不在这个包里面
     * </pre>
     */
    private void singleFileDown() {
        Button btn = (Button) findViewById(R.id.downBtDown);
        btn.setEnabled(false);

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        String url = "http://downmobile.kugou.com/Android/KugouPlayer/7840/KugouPlayer_219_V7.8.4.apk";
                        DownLoad down = new DownLoad(DownServiceActivity.this);
                        down.downLoadFile(url);
                    }
                }.start();
            }
        });
    }
}
