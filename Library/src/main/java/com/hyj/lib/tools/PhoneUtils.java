package com.hyj.lib.tools;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

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
     * 获取设备MAC地址
     *
     * @param context
     * @return
     */
    public static String getDeviceMac(Context context) {
        try {
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备唯一ID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = null;
        if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            device_id = tm.getDeviceId();
        }
        if (TextUtils.isEmpty(device_id)) {
            device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        }
        return device_id;
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

        //若果拿不到手机号码，就取SIM卡中的IMSI号
        if (TextUtils.isEmpty(phoneNumber)) {
            // IMSI号前面3位460是国家；紧接着后面2位00、02是中国移动；01是中国联通；03是中国电信。
            phoneNumber = tm.getSubscriberId();
            int type = Integer.parseInt(phoneNumber.substring(0, 5));
            switch (type) {
                case 46000:
                case 46002:
                    phoneNumber = "中国移动　" + phoneNumber;
                    break;
                case 46001:
                    phoneNumber = "中国联通　" + phoneNumber;
                    break;
                case 46003:
                    phoneNumber = "中国电信　" + phoneNumber;
                    break;
            }
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

    /**
     * 检查用户是否有某一个权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}