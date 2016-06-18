package com.hyj.lib.tools;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Toat显示工具类
 * Created by hyj on 2016/6/15.
 */
public class ToastUtils {
    /**
     * 初始化一个Toast
     *
     * @param context  上下文
     * @param msg      提示语
     * @param duration 显示时间
     */
    private static void initToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    /**
     * 显示一个Toast，并让EditText获取焦点且弹出软键盘
     *
     * @param activity
     * @param et
     * @param msg      提示信息
     */
    public static void showToast(Activity activity, EditText et, String msg) {
        et.requestFocus();
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        showToast(activity, msg);
    }


    /**
     * 显示一个Toast，时间短
     *
     * @param context 上下文
     * @param msg     提示语
     */
    public static void showToast(Context context, String msg) {
        initToast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个Toast，时间长
     *
     * @param context 上下文
     * @param msg     提示语
     */
    public static void showToastLong(Context context, String msg) {
        initToast(context, msg, Toast.LENGTH_LONG);
    }
}
