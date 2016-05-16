package com.hyj.lib.ActivityService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.Random;

public class MessengerService extends Service {
    /**
     * 绑定标识，获取activity中的引用
     */
    public static final int SERVICE_BIND = 0X00000001;
    /**
     * 消息通信
     */
    public static final int SERVICE_MSG = 0X00000002;

    private Messenger activityMessenger;//Activity中Messenger的引用

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_BIND:
                    //处理绑定的Messenger
                    activityMessenger = msg.replyTo;//获取来自Activity的Messenger
                    break;

                case SERVICE_MSG:
                    sendToActivity((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        //创建Messenger对象，指定由handler来处理Activity发过来的信息
        Messenger messenger = new Messenger(handler);
        //返回Messenger的Binder给绑定的Activity
        return messenger.getBinder();
    }

    /**
     * 发送消息回Activity
     *
     * @param str
     */
    private void sendToActivity(String str) {
        Message message = new Message();
        message.what = MessengerActivit.REQUEST_SERVICE;
        message.obj = "service：" + str + "　" + new Random().nextInt(100);
        try {
            activityMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
