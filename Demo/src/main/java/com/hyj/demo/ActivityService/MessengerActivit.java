package com.hyj.demo.ActivityService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.tools.LogUtils;
import com.hyj.lib.tools.ToastUtils;

/**
 * <pre>
 *     此为Activity与Service相互通信Demo
 *     相互通信有两种实现方案
 *      1、在Service中发送广播可以再Activity中接受，不过Activity没法向Service中发送广播
 *      2、使用Messenger信使的方式可以实现相互通信且效率高。
 * </pre>
 */
public class MessengerActivit extends BaseActivity {
    /**
     * 从Service过来的请求
     */
    public static final int REQUEST_SERVICE = 0X00000001;

    private Messenger serviceMenssenger;//服务端Messenger的引用

    private TextView tvMsg;
    private EditText etSendMsg;
    private Button btSend;
    private ScrollView svContent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SERVICE:
                    tvMsg.append(msg.obj + "\n");

                    svContent.post(new Runnable() {
                        @Override
                        public void run() {
                            svContent.fullScroll(ScrollView.FOCUS_DOWN);

                            //让etMsg重新获取焦点
                            etSendMsg.setText("");
                            etSendMsg.requestFocus();
                        }
                    });
                    break;
            }
        }
    };

    /**
     * 绑定服务的链接对象
     */
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i("Activity与Service绑定");

            //获得Service中的Messenger的引用
            serviceMenssenger = new Messenger(service);

            //创建一个用于接收Service消息的Messenger，指定handler接收、处理消息
            Messenger activityMessenger = new Messenger(handler);
            Message message = new Message();//用于发送给Service的message
            message.what = MessengerService.SERVICE_BIND;
            message.replyTo = activityMessenger;//指定由activityMessenger接收Service中返回来的消息
            try {
                serviceMenssenger.send(message);//将Activity中的信息发往Service
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i("Activity与Service断开绑定");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger_main);

        myInit();
    }

    private void myInit() {
        initView();
        initBind();
    }

    private void initView() {
        tvMsg = (TextView) findViewById(R.id.asTvMsg);
        etSendMsg = (EditText) findViewById(R.id.asEtSendMsg);
        svContent = (ScrollView) findViewById(R.id.asSlContent);

        btSend = (Button) findViewById(R.id.asBtSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToService();
            }
        });
    }

    /**
     * 初始化Service绑定
     */
    private void initBind() {
        Intent intent = new Intent(this, MessengerService.class);
        //绑定服务
        bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    /**
     * 向Service发送消息
     */
    private void sendToService() {
        String msg = etSendMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.showToast(this, etSendMsg, "请输入要发送的信息");
            return;
        }

        tvMsg.append("activity：" + msg + "\n");

        Message message = new Message();
        message.what = MessengerService.SERVICE_MSG;
        message.obj = msg;
        try {
            serviceMenssenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtils.i("解除Service的绑定");
        //与服务解除绑定
        unbindService(connection);
    }
}
