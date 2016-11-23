package com.hyj.demo.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.MainDemoActivity;
import com.hyj.demo.R;
import com.hyj.demo.porgress.ProgressBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义通知样式适配
 * 资料：http://www.jianshu.com/p/426d85f34561
 */
public class CustomNotificationActivity extends BaseActivity {

    private Button btSystem;
    private Button btCustom;

    private int notificationID = 0;//每一条通知的ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

        myInit();
    }

    private void myInit() {
        initView();
        initListener();
    }

    private void initView() {
        btSystem = (Button) findViewById(R.id.nfBtSystem);
        btCustom = (Button) findViewById(R.id.nfBtCustom);
    }

    private void initListener() {
        btSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSysTemNotification();
            }
        });

        btCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomNotification();
            }
        });
    }

    /**
     * 调用系统通知栏样式
     */
    private void showSysTemNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("发现新版本");
        builder.setContentText("APP版本有更新：3.4.4，赶快更新，体验新功能吧！");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setShowWhen(false);//不显示时间
        builder.setOngoing(true);//常驻通知栏
        builder.setAutoCancel(true);//点击后自动清除
        //如果不设置LargeIcon，则小图标会显示在大图标的位置
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        Intent intent = new Intent(this, MainDemoActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(notificationID++, builder.build());
    }

    /**
     * 自定义通知栏样式
     */
    private void showCustomNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);//点击后自动清除
        RemoteViews remoteViews;
        //判断系统的通知栏是否是黑色
        boolean isDarkNotificationBar = !isColorSimilar(Color.BLACK, getNotificationColor(this));
        if (isDarkNotificationBar) {
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom);
        } else {
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom_white);
        }

        builder.setContent(remoteViews);
        Intent intent = new Intent(this, ProgressBarActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);
        remoteViews.setOnClickPendingIntent(R.id.nfBtUpdate, pi);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(notificationID++, builder.build());
    }

    /**
     * 获取通知栏titile的颜色
     * Activity不能继承自AppCompatActivity（实测5.0以下机型可以，5.0及以上机型不行），
     * 大致的原因是默认通知布局文件中的ImageView（largeIcon和smallIcon）被替换成了AppCompatImageView，
     * 而在5.0及以上系统中，AppCompatImageView的setBackgroundResource(int)未被标记为RemotableViewMethod，导致apply时抛异常
     *
     * @return
     */
    private int getNotificationColor(Context context) {
        if (context instanceof AppCompatActivity) {
            return getNotificationColorCompat(context);
        } else {
            return getNotificationColorInternal(context);
        }
    }

    private int getNotificationColorCompat(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.build();
        int layoutID = notification.contentView.getLayoutId();
        ViewGroup notificationRoot = (ViewGroup) LayoutInflater.from(context).inflate(layoutID, null);
        TextView tv = (TextView) notificationRoot.findViewById(android.R.id.title);
        if (null != tv) {//如果ROM厂商没有更改默认ID
            return tv.getCurrentTextColor();
        }

        //厂商更改了默认的ID，想办法从notificationRoot中找到title对应的TextView
        //先拿到所有的textView
        final List<TextView> lTv = new ArrayList<TextView>();
        iteratorView(notificationRoot, new Filter() {
            @Override
            public void filter(View view) {
                if (view instanceof TextView) {
                    lTv.add((TextView) view);
                }
            }
        });

        //假定字体最大的TextView就是title
        float minTextSize = Integer.MIN_VALUE;
        int index = 0;
        for (int i = 0, j = lTv.size(); i < j; i++) {
            float currentSize = lTv.get(i).getTextSize();
            if (currentSize > minTextSize) {
                minTextSize = currentSize;
                index = i;
            }
        }
        return lTv.get(index).getCurrentTextColor();
    }


    private int titleColor;

    private int getNotificationColorInternal(Context context) {
        final String DUMMY_TITLE = "DUMMY_TITLE";//设置TextView的title值

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(DUMMY_TITLE);//设置title的值，方便后面迭代的时候寻找标题的TextView
        Notification notification = builder.build();
        ViewGroup notificationRoot = (ViewGroup) notification.contentView.apply(context, new FrameLayout(context));
        TextView title = (TextView) notificationRoot.findViewById(android.R.id.title);//系统通知栏的布局文件中含有该ID的textview
        if (null != title) {//ROM厂商没有更改默认ID
            return title.getCurrentTextColor();
        }

        //如果ROM厂商更改了默认ID
        //找到text为DUMMY_TITLE的textView并获取其颜色
        iteratorView(notificationRoot, new Filter() {
            @Override
            public void filter(View view) {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (DUMMY_TITLE.equals(tv.getText().toString().trim())) {
                        titleColor = tv.getCurrentTextColor();
                    }
                }
            }
        });
        return titleColor;
    }

    private void iteratorView(View view, Filter filter) {
        if (null == view || filter == null) {
            return;
        }
        filter.filter(view);
        if (view instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) view;
            for (int i = 0, j = container.getChildCount(); i < j; i++) {
                View child = container.getChildAt(i);
                iteratorView(child, filter);
            }
        }
    }

    private interface Filter {
        public void filter(View view);
    }

    /**
     * 黑色作为基色
     * 用方差计算目标颜色是否近似基色
     *
     * @param baseColor
     * @param color
     * @return
     */
    public static boolean isColorSimilar(int baseColor, int color) {
        double COLOR_THRESHOLD = 180.0;//比较颜色是否相近的阈值
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);

        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < COLOR_THRESHOLD) {
            return true;
        }
        return false;
    }
}