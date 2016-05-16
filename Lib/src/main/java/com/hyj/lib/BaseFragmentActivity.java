package com.hyj.lib;

import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.hyj.lib.tools.LogUtils;

import java.util.ArrayList;

public class BaseFragmentActivity extends FragmentActivity {
    /**
     * 存放Fragment中的Touch事件监听器
     */
    private ArrayList<BaseActivity.OnFragmentTouchListener> lFragmentTouch = new ArrayList<BaseActivity.OnFragmentTouchListener>(
            10);

    /**
     * Fragment注册Touch响应事件
     *
     * @param onTouchListener
     */
    public void registerOnTouchListener(BaseActivity.OnFragmentTouchListener onTouchListener) {
        lFragmentTouch.add(onTouchListener);
    }

    /**
     * Fragment注销Touch响应事件
     *
     * @param onTouchListener
     */
    public void unregisterOnTouchListener(BaseActivity.OnFragmentTouchListener onTouchListener) {
        lFragmentTouch.remove(onTouchListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 循环遍历注册过事件的Fragment对象
        for (BaseActivity.OnFragmentTouchListener listener : lFragmentTouch) {
            listener.onTouch(ev);
        }

        ScreenTimer.getInstance().onTouch(this, ev);

        return super.dispatchTouchEvent(ev);
    }
}
