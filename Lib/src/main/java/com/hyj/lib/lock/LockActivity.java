package com.hyj.lib.lock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hyj.lib.R;
import com.hyj.lib.lock.lockpattern3.LockPointView;
import com.hyj.lib.tools.DialogUtils;

public class LockActivity extends Activity {
    private TextView tvTitle;
    private TextView tvForget;
    private LockPointView lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        iniListener();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.lockTvTitle);
        tvForget = (TextView) findViewById(R.id.lockTvForget);
        lock = (LockPointView) findViewById(R.id.lock);
    }

    private void initData() {
//        lock.setPointCount(4);
    }

    private void iniListener() {
        lock.setOnCompleteListener(new LockPointView.OnCompleteListener() {
            @Override
            public void onComplete(String password) {
                DialogUtils.showToastShort(LockActivity.this, password);
                lock.resetPoint();
            }
        });
    }
}
