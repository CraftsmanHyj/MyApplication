package com.hyj.lib;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.hyj.lib.tools.DialogUtils;
import com.hyj.lib.tools.ServiceUtils;
import com.hyj.lib.tools.Utils;

/**
 * <pre>
 * 　系统Application配置类，在这里做一些全局初始化设置
 *   ActivityLifecycleCallbacks：用于判断APP是否在后台运行
 *   	http://www.cnblogs.com/stay/p/5012370.html
 * </pre>
 *
 * @author hyj
 * @Date 2016-3-21 下午2:54:16
 */
public class LibActivityLifecycle implements ActivityLifecycleCallbacks {
    private int activityCount = 0;// 当前活动Activity的数量
    private Activity preActivity;//当前活动的Activity

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        preActivity = activity;
        ScreenTimer.isBackgound = false;
        ScreenTimer.getInstance().start(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityCount--;

        if (activityCount <= 0 && ServiceUtils.isBackground(activity)) {
            ScreenTimer.isBackgound = true;
            DialogUtils.showToastShort(activity, Utils.getAppName(activity) + "正在后台运行");
        }

        ScreenTimer.getInstance().start(preActivity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ScreenTimer.getInstance().stop();
    }
}
