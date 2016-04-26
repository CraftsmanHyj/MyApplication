package com.hyj.lib;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.hyj.lib.tools.LogUtils;

import java.util.ArrayList;

public class BaseActivity extends Activity {
    /**
     * 存放Fragment中的Touch事件监听器
     */
    private ArrayList<FragmentOnTouchListener> lFragmentTouch = new ArrayList<FragmentOnTouchListener>(
            10);

    /**
     * Fragment注册Touch响应事件
     *
     * @param onTouchListener
     */
    public void registerOnTouchListener(FragmentOnTouchListener onTouchListener) {
        lFragmentTouch.add(onTouchListener);
    }

    /**
     * Fragment注销Touch响应事件
     *
     * @param onTouchListener
     */
    public void unregisterOnTouchListener(FragmentOnTouchListener onTouchListener) {
        lFragmentTouch.remove(onTouchListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 循环遍历注册过事件的Fragment对象
        for (FragmentOnTouchListener listener : lFragmentTouch) {
            listener.onTouch(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ScreenTimer.getInstance().stop();
                break;

            case MotionEvent.ACTION_UP:
                ScreenTimer.getInstance().start(this);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    /**
     * Fragment中Touch事件接口
     */
    public interface FragmentOnTouchListener {
        /**
         * 触摸事件响应
         *
         * @param ev
         */
        public void onTouch(MotionEvent ev);
    }
}
