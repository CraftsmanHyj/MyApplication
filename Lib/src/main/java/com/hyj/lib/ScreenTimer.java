package com.hyj.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import com.hyj.lib.lock.LockActivity;
import com.hyj.lib.tools.LogUtils;


/**
 * Created by hyj on 2016/4/26.
 */
public class ScreenTimer extends CountDownTimer {
    /**
     * 倒计时总时长(s)
     */
    private final static long millisInFuture = 5 * 60 * 1000;

    private static ScreenTimer instance;
    private Activity activity;

    /**
     * 倒计时是否结束
     */
    private static boolean isTimeFinish = false;
    /**
     * APP是否是后台运行
     */
    public static boolean isBackgound = false;

    private ScreenTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * 获取一个倒计时实例
     *
     * @return
     */
    public static ScreenTimer getInstance() {
        if (null == instance) {
            synchronized (ScreenTimer.class) {
                if (null == instance) {
                    instance = new ScreenTimer(millisInFuture, 1 * 1000);
                }
            }
        }

        return instance;
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        LogUtils.i("倒计时：" + millisUntilFinished + " " + activity.getClass());
        if (millisUntilFinished <= 2 * 1000) {
            isTimeFinish = true;
            return;
        }
        isTimeFinish = false;
    }

    @Override
    public void onFinish() {
        if (isTimeFinish && !isBackgound) {
            LogUtils.e("倒计时结束，执行响应操作" + activity.getClass());

            Intent intent = new Intent(activity, LockActivity.class);
            activity.startActivity(intent);

            isTimeFinish = false;
        } else {
            LogUtils.e("倒计时中断，停止计时" + activity.getClass());
        }
    }


    /**
     * 开始倒计时
     *
     * @param activity
     */
    public void start(Activity activity) {
        this.activity = activity;
        stop();

        if (activity instanceof LockActivity) {
            return;
        }
        instance.start();
    }

    /**
     * 停止倒计时
     */
    public void stop() {
        instance.cancel();
        instance.onFinish();
    }

    /**
     * 触摸事件响应
     *
     * @param activity
     * @param ev
     */
    public void onTouch(Activity activity, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;

            case MotionEvent.ACTION_UP:
                start(activity);
                break;
        }
    }
}
