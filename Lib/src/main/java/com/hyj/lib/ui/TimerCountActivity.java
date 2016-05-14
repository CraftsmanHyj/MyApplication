package com.hyj.lib.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hyj.lib.BaseActivity;
import com.hyj.lib.R;
import com.hyj.lib.utils.TimerCount;

/**
 * 实现倒计时
 *
 * @author async
 */
public class TimerCountActivity extends BaseActivity {
    private Button btTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timercount);

        myInit();
    }

    private void myInit() {
        firstScheme();
        secondScheme();
    }

    /**
     * 倒计时实现第一种方案
     */
    private void firstScheme() {
        btTimer = (Button) findViewById(R.id.tcBtTimer);
        ValueAnimator va = ValueAnimator.ofInt(1000, 0);
        va.setDuration(60 * 1000);// 设置持续时间
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer count = (Integer) animation.getAnimatedValue();
                btTimer.setText("倒计时：" + count);
            }
        });
        va.start();
    }

    /**
     * 倒计时实现第二种方案
     */
    private void secondScheme() {
        Button btTimerCount = (Button) findViewById(R.id.tcBtTimerCount);
        final TimerCount tc = new TimerCount(this, btTimerCount);
        btTimerCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tc.startTimerCount();
            }
        });
    }
}
