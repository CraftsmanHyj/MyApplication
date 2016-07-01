package com.hyj.lib.tools.appinfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.hyj.lib.tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 获取安装AppInfo的单实例工具类
 * Created by hyj on 2016/6/27.
 */
public class AppUtils {
    private Map<String, AppInfo> mapApp = new HashMap<String, AppInfo>();//所有APP
    private List<AppInfo> lSysApp = new ArrayList<AppInfo>();//系统App
    private List<AppInfo> lCusApp = new ArrayList<AppInfo>();//非系统App

    private static AppUtils instance;

    /**
     * 获取一个AppUtils实例
     *
     * @param context
     * @return
     */
    public static AppUtils getInstance(Context context) {
        if (null == instance) {
            synchronized (AppUtils.class) {
                instance = new AppUtils(context);
            }
        }
        return instance;
    }

    /**
     * 销毁存放App的实例
     */
    public static void destroy() {
        instance = null;
        instance.mapApp.clear();
    }

    private AppUtils(Context context) {
        initAppInfo(context);
    }

    /**
     * 初始化信息，获取所有安装APP的信息
     */
    private void initAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);//PackageManager.GET_UNINSTALLED_PACKAGES;

        for (PackageInfo packageInfo : packages) {
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));

            mapApp.put(tmpInfo.getPackageName(), tmpInfo);

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //非系统应用
                lCusApp.add(tmpInfo);
            } else if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //系统应用
                lSysApp.add(tmpInfo);
            }
        }
    }

    /**
     * 获取手机上安装的所有应用
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getApp(Context context) {
        getInstance(context);

        List<AppInfo> lApp = new ArrayList<AppInfo>();
        for (Entry<String, AppInfo> entry : instance.mapApp.entrySet()) {
            lApp.add(entry.getValue());
        }

        return lApp;
    }

    /**
     * 获取系统应用
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getSytemApp(Context context) {
        getInstance(context);
        return instance.lSysApp;
    }

    /**
     * 获取非系统应用
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getCusApp(Context context) {
        getInstance(context);
        return instance.lCusApp;
    }

    /**
     * 获取指定App信息
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static AppInfo getAppInfo(Context context, String packageName) {
        getInstance(context);
        return instance.mapApp.get(packageName);
    }

    /**
     * 是否安装了指定的App
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstallApp(Context context, String packageName) {
        AppInfo info = getAppInfo(context, packageName);
        return null != info;
    }

    /**
     * 该手机中是否安装了这个APP
     *
     * @param context     上下文
     * @param packageName APP包名
     * @return
     */
    public static boolean hasApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return null != intent;
    }

    /**
     * 打开一个APP
     *
     * @param context     上下文
     * @param packageName APP包名
     * @return boolean false：打开失败，没有安装此APP；true：打开成功
     */
    public static boolean startApp(Context context, String packageName) {
        return startApp(context, packageName, new Bundle());
    }

    /**
     * 打开一个APP
     *
     * @param context     上下文
     * @param packageName APP包名
     * @param bundle      要传的参数
     * @return boolean false：打开失败，没有安装此APP；true：打开成功
     */
    public static boolean startApp(Context context, String packageName, Bundle bundle) {
        if (!isInstallApp(context, packageName)) {//hasApp(context, packageName)
            return false;
        }

        String msg = "从<" + Utils.getAppName(context) + "_" + context.getPackageName() + ">跳转：" + Utils.getCurrentTime();
        bundle.putString("value", msg);

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return true;
    }
}
