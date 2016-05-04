package com.hyj.lib;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;

import com.hyj.lib.tools.LogUtils;

/**
 * Fragment基类
 */
public class BaseFragment extends Fragment {

    BaseActivity.OnFragmentTouchListener touchListener = new BaseActivity.OnFragmentTouchListener() {
        @Override
        public void onTouch(MotionEvent ev) {
            LogUtils.e("进入Fragment：" + ev.getAction());
            ScreenTimer.getInstance().onTouch(getActivity(), ev);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).registerOnTouchListener(touchListener);
        } else if (getContext() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) getContext()).registerOnTouchListener(touchListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).unregisterOnTouchListener(touchListener);
        } else if (getContext() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) getContext()).unregisterOnTouchListener(touchListener);
        }
    }
}
