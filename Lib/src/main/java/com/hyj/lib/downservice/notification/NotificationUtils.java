package com.hyj.lib.downservice.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hyj.lib.R;
import com.hyj.lib.downservice.DownServiceActivity;
import com.hyj.lib.http.download.DownService;
import com.hyj.lib.http.download.FileInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     通知工具类
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/9 22:29
 */
public class NotificationUtils {
    private NotificationManager notificationManager = null;
    //所有通知集合，集中管理通知栏内容
    private Map<Integer, Notification> mapNotification = null;

    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
        //获得通知系统服务
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建一个通知的集合
        mapNotification = new HashMap<Integer, Notification>();
    }

    /**
     * 显示下载文件的通知
     *
     * @param fileInfo
     */
    public void showNotification(FileInfo fileInfo) {
        //判断通知是否已经显示
        if (mapNotification.containsKey(fileInfo.getId())) {
            return;
        }

        //创建一个通知对象
        Notification notification = new Notification();
        //设置滚动文字
        notification.tickerText = fileInfo.getFileName() + "开始下载";
        //设置通知显示的时间
        notification.when = System.currentTimeMillis();
        //设置图标
        notification.icon = R.drawable.ic_launcher;
        //设置通知特性，点击通知之后自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //设置点击通知栏的操作
        Intent intent = new Intent(context, DownServiceActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        notification.contentIntent = pi;//点击通知栏之后的操作

        //创建RemoteViews对象
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.downservice_notification);
        //设置开始按钮操作
        Intent intentStart = new Intent(context, DownService.class);
        intentStart.setAction(DownService.ACTION_PREPARE);
        intentStart.putExtra(DownService.DOWNINFO, fileInfo);
        PendingIntent piStart = PendingIntent.getService(context, 0, intentStart, 0);
        remoteViews.setOnClickPendingIntent(R.id.downNfBtBegin, piStart);
        //设置暂停按钮操作
        Intent intentPause = new Intent(context, DownService.class);
        intentPause.setAction(DownService.ACTION_PAUSE);
        intentPause.putExtra(DownService.DOWNINFO, fileInfo);
        PendingIntent piPause = PendingIntent.getService(context, 0, intentPause, 0);
        remoteViews.setOnClickPendingIntent(R.id.downNfBtPause, piPause);
        //设置TextVie里面的值
        remoteViews.setTextViewText(R.id.downNfTvFileName, fileInfo.getFileName());

        //设置Notification的视图
        notification.contentView = remoteViews;

        //发出通知显示在通知栏
        notificationManager.notify(fileInfo.getId(), notification);
        //把通知加入到集合中
        mapNotification.put(fileInfo.getId(), notification);
    }

    /**
     * 取消通知
     *
     * @param id
     */
    public void cancleNotification(int id) {
        //取消通知
        notificationManager.cancel(id);
        mapNotification.remove(id);
    }

    /**
     * 更新通知栏进度
     *
     * @param id
     * @param progress
     */
    public void updateNotification(int id, int progress) {
        Notification notification = mapNotification.get(id);
        if (null != notification) {//说明当前是一个活动的通知
            //修改进度条
            notification.contentView.setProgressBar(R.id.downNfPbProgress, 100, progress, false);
            notificationManager.notify(id, notification);
        }
    }
}
