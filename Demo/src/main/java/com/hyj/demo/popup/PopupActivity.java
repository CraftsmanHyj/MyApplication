package com.hyj.demo.popup;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.dialog.ActionSheetDialog;
import com.hyj.lib.dialog.AlertDialog;
import com.hyj.lib.dialog.SheetItem;
import com.hyj.lib.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class PopupActivity extends BaseActivity implements OnClickListener {

    private Button bt4Bottom;// 从底部上滑显示popup
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        bt4Bottom = (Button) findViewById(R.id.popupShowBottom);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
    }

    private AlertDialog alertDialog;
    private ActionSheetDialog sheetDialog;
    private List<SheetItem> lSheetItem;
    private ActionSheetDialog.OnSheetItemClickListener sheetItemListener = new ActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int index) {
            ToastUtils.showToast(PopupActivity.this, "item" + index);
        }
    };

    private void initData() {
        sheetDialog = new ActionSheetDialog(this);
        sheetDialog.setCancelable(false);
        sheetDialog.setCanceledOnTouchOutside(false);
    }

    private void initListener() {
        bt4Bottom.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popupShowBottom:
                lightSwitch(0.3f);
                showPopup4Bottom(bt4Bottom);
                break;

            case R.id.btn1:
                sheetDialog.setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？");
                lSheetItem = new ArrayList<SheetItem>();
                lSheetItem.add(new SheetItem("清空消息列表", sheetItemListener));
                sheetDialog.setSheetItems(lSheetItem);
                sheetDialog.show();
                break;

            case R.id.btn2:
                //清除以前设置的标题
                sheetDialog.setTitle(null);
                lSheetItem = new ArrayList<SheetItem>();
                lSheetItem.add(new SheetItem("发送给好友", sheetItemListener));
                lSheetItem.add(new SheetItem("转载到空间相册", sheetItemListener));
                lSheetItem.add(new SheetItem("上传到群相册", sheetItemListener));
                lSheetItem.add(new SheetItem("保存到手机", sheetItemListener));
                lSheetItem.add(new SheetItem("收藏", sheetItemListener));
                lSheetItem.add(new SheetItem("查看聊天图片", sheetItemListener));
                sheetDialog.setSheetItems(lSheetItem);
                sheetDialog.show();
                break;

            case R.id.btn3:
                sheetDialog.setTitle("请选择操作");
                lSheetItem = new ArrayList<SheetItem>();
                lSheetItem.add(new SheetItem("条目一", sheetItemListener));
                lSheetItem.add(new SheetItem("条目二", sheetItemListener));
                lSheetItem.add(new SheetItem("条目三", sheetItemListener));
                lSheetItem.add(new SheetItem("条目四", sheetItemListener));
                lSheetItem.add(new SheetItem("条目五", sheetItemListener));
                lSheetItem.add(new SheetItem("条目六", sheetItemListener));
                lSheetItem.add(new SheetItem("条目七", sheetItemListener));
                lSheetItem.add(new SheetItem("条目八", sheetItemListener));
                lSheetItem.add(new SheetItem("条目九", sheetItemListener));
                lSheetItem.add(new SheetItem("条目十", sheetItemListener));
                sheetDialog.setSheetItems(lSheetItem);
                sheetDialog.show();
                break;

            case R.id.btn4:

                alertDialog = new AlertDialog(this);
                alertDialog.setTitle("退出当前账号");
                alertDialog.setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？");
                alertDialog.setPositiveButton("确认退出", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                alertDialog.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                alertDialog.show();
                break;

            case R.id.btn5:
                alertDialog = new AlertDialog(this);
                alertDialog.setMsg("你现在无法接收到新消息提醒。请到系统-设置-通知中开启消息提醒");
                alertDialog.setNegativeButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                alertDialog.show();
                break;
        }
    }

    /**
     * 从底部展示popupWindow
     *
     * @param locationView 触发显示popup的view
     */
    private void showPopup4Bottom(View locationView) {
        /**
         * <pre>
         * window.setFocusable(true)、window.setBackgroundDrawable()这两个方法必须调用
         * 如果是想让popWindow半透明，就是上面的那个方法，
         * 如果只是单纯的调用这个方法就这样写window.setBackgroundDrawable(new BitmapDrawable());
         * </pre>
         */

        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_bottom, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(popupView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);// 设置窗口可点击

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.popup_bottom);
        // 设置在底部显示
        window.showAtLocation(locationView, Gravity.BOTTOM, 0, 0);

        /**
         * <pre>
         * 将popup显示在任意位置
         * int[] location = new int[2];
         * view.getLocationOnScreen(location);
         * window.showAtLocation(view, Gravity.NO_GRAVITY, location[0],
         * 		location[1] - locationView.getHeight());
         * 也可以使用window.showAsDropDown(v,location[0],location[1]);方法实现
         * </pre>
         */

        window.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lightSwitch(1.0f);
            }
        });

        // 实现按钮点击事件
        Button btUpdate = (Button) popupView.findViewById(R.id.popupBtnUpdate);
        Button btBug = (Button) popupView.findViewById(R.id.popupBtnBug);
        Button btExit = (Button) popupView.findViewById(R.id.popupBtnExit);
        OnClickListener click = new OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                String msg = ((Button) v).getText().toString();
                ToastUtils.showToast(PopupActivity.this, msg);
            }
        };
        btUpdate.setOnClickListener(click);
        btBug.setOnClickListener(click);
        btExit.setOnClickListener(click);
    }

    /**
     * 内容区域明暗度设置
     */
    private void lightSwitch(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }
}
