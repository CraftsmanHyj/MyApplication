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

    BaseActivity.FragmentOnTouchListener touchListener = new BaseActivity.FragmentOnTouchListener() {
        @Override
        public void onTouch(MotionEvent ev) {
            LogUtils.e("进入Fragment：" + ev.getAction());

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ScreenTimer.getInstance().stop();
                    break;

                case MotionEvent.ACTION_UP:
                    ScreenTimer.getInstance().start(getActivity());
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseActivity) getContext()).registerOnTouchListener(touchListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((BaseActivity) getContext()).unregisterOnTouchListener(touchListener);
    }
}
