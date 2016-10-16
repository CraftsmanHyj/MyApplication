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
public class LogUtils {
    /**
     * config.properties配置文件中配置
     * 是否打印日志
     */
    public static boolean isDebug = false;
    /**
     * onfig.properties配置文件中配置
     * 日志TAG标签
     */
    public static String TAG = "TAG";

    /**
     * 提醒信息
     *
     * @param msg
     */
    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 普通消息
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 警告消息
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    /**
     * 错误消息
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }
}