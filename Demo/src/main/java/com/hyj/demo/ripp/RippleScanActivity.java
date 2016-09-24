package com.hyj.demo.ripp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;

public class RippleScanActivity extends BaseActivity {
    private ImageView imageview;
    private RippleLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripple_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }


    private void initView() {
        layout = (RippleLayout) findViewById(R.id.ripple_layout);
        imageview = (ImageView) findViewById(R.id.centerImage);
    }

    private void initData() {
    }

    private void initListener() {
        imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.isRippleAnimationRunning()) {
                    layout.stopRippleAnimation();
                } else {
                    layout.startRippleAnimation();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.startRippleAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (layout.isRippleAnimationRunning()) {
            layout.stopRippleAnimation();
        }
    }
}
