package com.hyj.demo;

import android.app.Activity;
import android.view.MotionEvent;

import java.util.ArrayList;

public class BaseActivity extends Activity {
    /**
     * 存放Fragment中的Touch事件监听器
     */
    private ArrayList<OnFragmentTouchListener> lFragmentTouch = new ArrayList<OnFragmentTouchListener>(
            10);

    /**
     * Fragment注册Touch响应事件
     *
     * @param onTouchListener
     */
    public void registerOnTouchListener(OnFragmentTouchListener onTouchListener) {
        lFragmentTouch.add(onTouchListener);
    }

    /**
     * Fragment注销Touch响应事件
     *
     * @param onTouchListener
     */
    public void unregisterOnTouchListener(OnFragmentTouchListener onTouchListener) {
        lFragmentTouch.remove(onTouchListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 循环遍历注册过事件的Fragment对象
        for (OnFragmentTouchListener listener : lFragmentTouch) {
            listener.onTouch(ev);
        }

        ScreenTimer.getInstance().onTouch(this, ev);

        return super.dispatchTouchEvent(ev);
    }

    /**
     * Fragment中Touch事件接口
     */
    public interface OnFragmentTouchListener {
        /**
         * 触摸事件响应
         *
         * @param ev
         */
        public void onTouch(MotionEvent ev);
    }
}
