package com.hyj.lib.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences文件操作工具类
 *
 * @Author hyj
 * @Date 2015-12-15 下午3:12:49
 */
public class SPUtils {
    /**
     * 存放SharedPreferences对象集合
     */
    private static Map<String, SharedPreferences> mapSP = new HashMap<String, SharedPreferences>();
    /**
     * APP默认配置文件
     */
    private static final String DEFAULT_PREFERENCES = "preferences";

    /**
     * <pre>
     *     获取一个SharedPrefernces对象
     *      获取SharedPreferences的三种方式，文件保存路径：/data/data/包名/shared_prefs/
     *        1、sp = PreferenceManager.getDefaultSharedPreferences(context);
     *          每个应用都有一个默认的配置文件preferences.xml文件，此方法就是获取这个文件的对象
     *
     *        2、sp = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
     *          默认使用当前类不带包名的类名作为文件的名称
     *
     *        3、sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
     *          获取指定名字、指定模式的文件
     * </pre>
     *
     * @param context 上下文
     * @param spName  sp文件名
     * @return
     */
    private static SharedPreferences getSharedPre(Context context, String spName) {
        if (TextUtils.isEmpty(spName)) {
            spName = DEFAULT_PREFERENCES;
        }

        SharedPreferences sp = mapSP.get(spName);
        if (null == sp) {
            synchronized (SPUtils.class) {
                if (null == sp) {
                    if (DEFAULT_PREFERENCES.equals(spName)) {
                        sp = PreferenceManager.getDefaultSharedPreferences(context);
                    } else {
                        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
                    }
                    mapSP.put(spName, sp);
                }
            }
        }
        return sp;
    }

    /**
     * 保存数据到APP默认配置文件preferences.xml中
     *
     * @param context 上下文
     * @param key     关键字
     * @param value   对应的值
     */
    public static void putParam(Context context, String key, Object value) {
        putParam(context, "", key, value);
    }

    /**
     * 保存数据到sharedPreference里面
     *
     * @param context 上下文
     * @param spName  sp文件名
     * @param key     关键字
     * @param value   对应的值
     */
    public static void putParam(Context context, String spName, String key, Object value) {
        Map<String, Object> mValues = new HashMap<String, Object>();
        mValues.put(key, value);
        putParam(context, spName, mValues);
    }

    /**
     * 保存数据到APP默认配置文件preferences.xml中
     *
     * @param context 上下文
     * @param mValues Map<String, Object>的格式，要保存的数据
     */
    public static void putParam(Context context, Map<String, Object> mValues) {
        putParam(context, "", mValues);
    }

    /**
     * 保存数据到sharedPreference里面
     *
     * @param context 上下文
     * @param spName  sp文件名
     * @param mValues Map<String, Object>的格式，要保存的数据
     */
    @SuppressWarnings("unchecked")
    public static void putParam(Context context, String spName, Map<String, Object> mValues) {
        SharedPreferences sp = getSharedPre(context, spName);
        SharedPreferences.Editor editor = sp.edit();

        String key;
        Object value;
        for (Map.Entry<String, Object> entry : mValues.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Set) {
                editor.putStringSet(key, (Set<String>) value);
            }
        }

        editor.commit();
    }

    /**
     * 获取保存在APP默认配置文件preferences.xml中的数据
     * 我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context  上下文
     * @param key      关键字
     * @param defValue 默认值
     * @return
     */
    public static Object getParam(Context context, String key, Object defValue) {
        return getParam(context, "", key, defValue);
    }

    /**
     * 获取保存的数据，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context  上下文
     * @param spName   sp文件名
     * @param key      关键字
     * @param defValue 默认值
     * @return Object
     */
    public static Object getParam(Context context, String spName, String key, Object defValue) {
        SharedPreferences sp = getSharedPre(context, spName);

        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defValue);
        }

        return null;
    }

    /**
     * 获取APP默认配置文件preferences.xml中所有的参数值
     *
     * @param context 上下文
     * @return
     */
    public static Map<String, Object> getAllParams(Context context) {
        return getAllParams(context, "");
    }

    /**
     * 获取Sharedpreferences里面所有的参数值
     *
     * @param context 上下文
     * @param spName  文件名
     * @return
     */
    public static Map<String, Object> getAllParams(Context context, String spName) {
        SharedPreferences sp = getSharedPre(context, spName);
        return (Map<String, Object>) sp.getAll();
    }

    /**
     * 判断SharedPreferences中是否含有某个key的记录
     *
     * @param context 上下文
     * @param spName  文件名
     * @param key     key值
     * @return
     */
    public static boolean containsKey(Context context, String spName, String key) {
        return containsKey(context, spName, key, null);
    }

    /**
     * 判断SharedPreferences中是否含有某个key的记录
     * 当value值传null：仅仅判断是否含有这个key的一条记录
     * 当value值非null：存在则返回ture，不存在则将此值插入
     *
     * @param context 上下文
     * @param spName  文件名
     * @param key     key值
     * @param value   value值
     * @return
     */
    public static boolean containsKey(Context context, String spName, String key, Object value) {
        SharedPreferences sp = getSharedPre(context, spName);
        if (!sp.contains(key)) {
            if (null == value) {
                return false;
            }
            putParam(context, spName, key, value);
        }
        return true;
    }
}