package com.hyj.demo.porgress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.progress.CircleProgressBar;
import com.hyj.lib.progress.HorizontalProgressBar;

public class ProgressBarActivity extends BaseActivity {
    private final int MSG_UPDATE = 0X00010000;

    private HorizontalProgressBar pb1;
    private HorizontalProgressBar pb2;
    private HorizontalProgressBar pb3;
    private CircleProgressBar pb4;
    private CircleProgressBar pb5;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = pb1.getProgress();
            pb1.setProgress(++progress);
            pb5.setProgress(++progress);

            int delay = 100;
            if (progress > 100) {
                pb1.setProgress(0);
                delay = 2 * 1000;
//                handler.removeMessages(MSG_UPDATE);
            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probar_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
    }

    private void initView() {
        pb1 = (HorizontalProgressBar) findViewById(R.id.proBarPb1);
        pb2 = (HorizontalProgressBar) findViewById(R.id.proBarPb2);
        pb3 = (HorizontalProgressBar) findViewById(R.id.proBarPb3);
        pb4 = (CircleProgressBar) findViewById(R.id.proBarPb4);
        pb5 = (CircleProgressBar) findViewById(R.id.proBarPb5);
    }

    private void initData() {
        handler.sendEmptyMessage(MSG_UPDATE);
    }
}
