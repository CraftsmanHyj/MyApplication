package com.hyj.demo.example;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.view.ClockView;

/**
 * 自定义时钟mode
 * Created by hyj on 2016/11/10.
 */

public class CusClockViewActivity extends BaseActivity implements View.OnClickListener {
    private ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_main);

        final TextView tv = (TextView) findViewById(R.id.tv);
        clockView = (ClockView) findViewById(R.id.clockView);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);

        clockView.setOnTimeChangeListener(new ClockView.OnTimeChangeListener() {
            @Override
            public void onTimeChange(View view, int hour, int minute, int second) {
                tv.setText(String.format("%s-%s-%s", hour, minute, second));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                clockView.setTime(11, 59, 55);
                break;
            case R.id.btn2:
                clockView.setTime(20, 30, 0);
                break;
            case R.id.btn3:
                clockView.setTime(23, 59, 55);
                break;
        }
    }
}