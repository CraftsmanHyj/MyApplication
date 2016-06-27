package com.hyj.lib.tools.appinfo;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * 手机上安装App的信息
 * Created by hyj on 2016/6/27.
 */
public class AppInfo implements Serializable {
    public static final long serialVersionUID = 1L;

    private String appName;//App名
    private String packageName;//包名
    private String versionName;//版本名
    private int versionCode;//版本号
    private Drawable appIcon;//应用logo

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode + '}';
    }
}