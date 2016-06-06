package com.hyj.lib.tools;

import android.util.Log;


/**
 * <pre>
 *     log工具类，改变boolean值常量控制是否打印日志
 *     在调用项目的Application中调用initParameter()方法，初始化参数
 * </pre>
 *
 * @Author hyj
 * @Date 2015-12-16 下午2:53:10
 */
public abstract class LogUtils {
    /**
     * 是否打印日志
     */
    private static boolean printLog = false;
    /**
     * 日志TAG标签
     */
    private static String TAG = "TAG";

    /**
     * 初始化参数：printLog、TAG
     *
     * @param isPrintLog 是否打印日志
     * @param tagName    打印标签名
     */
    public static void initParameter(boolean isPrintLog, String tagName) {
        printLog = isPrintLog;
        TAG = tagName;
    }

    /**
     * 提醒信息
     *
     * @param msg
     */
    public static void v(String msg) {
        if (printLog) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (printLog) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 普通消息
     *
     * @param msg
     */
    public static void i(String msg) {
        if (printLog) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 警告消息
     *
     * @param msg
     */
    public static void w(String msg) {
        if (printLog) {
            Log.w(TAG, msg);
        }
    }

    /**
     * 错误消息
     *
     * @param msg
     */
    public static void e(String msg) {
        if (printLog) {
            Log.e(TAG, msg);
        }
    }
}
