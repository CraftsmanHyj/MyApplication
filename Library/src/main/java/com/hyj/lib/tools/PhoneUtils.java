package com.hyj.lib.tools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 电话状态，网络等于手机相关的数据处理
 *
 * @Author hyj
 * @Date 2016/10/26 21:52
 */
public class PhoneUtils {
    /**
     * 获取手机类型
     *
     * @return String 手机型号：samsung GT-I9508
     */
    public static String getPhoneName() {
        return Build.BRAND + "　　" + Build.MODEL;
    }

    /**
     * 获取手机中的IMEI号
     * 需要添加 android.permission.READ_PHONE_STATE 权限
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机号
     * GSM手机的 MSISDN.
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        if (phoneNumber.startsWith("+86")) {
            phoneNumber = phoneNumber.replace("+86", "");
        }
        return phoneNumber;
    }

    /**
     * 获取手机当前使用的网络类型
     * http://www.cnblogs.com/luxiaofeng54/archive/2011/03/01/1968063.html
     *
     * @param context
     * @return
     */
    public static String getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = tm.getPhoneType();
        String phoneType = "获取失败";
        switch (type) {
            case TelephonyManager.PHONE_TYPE_NONE:
                phoneType = "无信号";
                break;

            case TelephonyManager.PHONE_TYPE_GSM:
                phoneType = "GSM信号";
                break;

            case TelephonyManager.PHONE_TYPE_CDMA:
                phoneType = "CDMA信号";
                break;
        }
        return phoneType;
    }

    /**
     * 获取Android系统版本号
     *
     * @return int 17/19 Android SDK版本号
     */
    public static int getOSVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本号
     *
     * @return String 4.4.4系统版本号
     */
    public static String getOSVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isOpenNetwork(final Context context) {
        boolean isAvailable = hasNetwork(context);

        if (!isAvailable) {
            DialogUtils.DialogAction okAction = new DialogUtils.DialogAction() {

                @Override
                public void action() {
                    Intent intent = null;
                    int sdkVersion = Build.VERSION.SDK_INT;

                    if (sdkVersion > 10) {
                        intent = new Intent(Settings.ACTION_SETTINGS);
                    } else {
                        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    }

                    context.startActivity(intent);
                }
            };

            DialogUtils.showConfirmDialog(context, "提示", "网络未开启，是否马上设置？", okAction);

            return false;
        }

        return true;
    }

    /**
     * 判断手机网络是否正常(包括WIFI、移动网络)
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        boolean isNormal = hasWifiNetwork(context);
        isNormal = isNormal ? isNormal : hasMobileNetwork(context);
        return isNormal;
    }

    /**
     * WIFI是否可用
     *
     * @param context
     * @return
     */
    public static boolean hasWifiNetwork(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        if (null == wifiManager) {
            return false;
        }

        return WifiManager.WIFI_STATE_ENABLED == wifiManager.getWifiState();
    }

    /**
     * 移动网络是否可用(4G/3G/2G网络)
     *
     * @param context
     * @return
     */
    public static boolean hasMobileNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return false;
        }

        NetworkInfo info = manager.getActiveNetworkInfo();
        return null != info && info.isAvailable();
    }
}