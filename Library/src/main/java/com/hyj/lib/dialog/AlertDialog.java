package com.hyj.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hyj.lib.R;

public class AlertDialog {
    private Context context;
    private Dialog dialog;

    private LinearLayout llContent;//父布局
    private TextView tvTitle;//标题
    private TextView tvMsg;//文字提示
    private LinearLayout llView;//预留给自定义的布局
    private ImageView imgLine;//水平分割线
    private Button btNeg;//确认按钮
    private Button btPos;//取消按钮

    private boolean showPosBtn = false;//显示确认按钮
    private boolean showNegBtn = false;//显示取消按钮

    public AlertDialog(Context context) {
        this.context = context;

        initView();
    }

    private void initView() {
        // 获取Dialog布局、实例化控件
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_alertdialog, null);
        llContent = (LinearLayout) view.findViewById(R.id.alertLlContent);
        tvTitle = (TextView) view.findViewById(R.id.alertTvTitle);
        tvMsg = (TextView) view.findViewById(R.id.alertTvMsg);
        llView = (LinearLayout) view.findViewById(R.id.alertLlView);
        imgLine = (ImageView) view.findViewById(R.id.alertImgLine);

        btNeg = (Button) view.findViewById(R.id.alertBtNeg);
        btPos = (Button) view.findViewById(R.id.alertBtPos);
        setButtonVisible();

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCancelable(false);//点击back键是否可以取消
        dialog.setCanceledOnTouchOutside(false);//点击外部不能取消
        dialog.setContentView(view);

        //获取到屏幕的参数
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        llContent.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
    }

    /**
     * 显示Dialog
     */
    public void show() {
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * 设置点击Back是否可以取消
     *
     * @param cancel
     */
    public void setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
    }

    /**
     * 点击外部是否可以取消
     *
     * @param cancel
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 设置对话框标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText("温馨提示");
        } else {
            tvTitle.setText(title);
        }
    }

    /**
     * 设置对话框提示语
     *
     * @param msg
     */
    public void setMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            tvMsg.setText("内容");
        } else {
            tvMsg.setText(msg);
        }
        tvMsg.setVisibility(View.VISIBLE);
        llView.setVisibility(View.GONE);
    }

    /**
     * 自定义Dialog内容部分
     *
     * @param view
     */
    public void setContentView(View view) {
        if (null != view) {
            tvMsg.setVisibility(View.GONE);
            llView.setVisibility(View.VISIBLE);
            llView.addView(view);
        }
    }

    /**
     * 设置确认点击事件，按钮默认“确定”名称
     *
     * @param listener 点击事件
     */
    public void setPositiveButton(OnClickListener listener) {
        setPositiveButton("", listener);
    }

    /**
     * 设置确认按钮事件
     *
     * @param text     按钮名字，默认“确定”
     * @param listener 点击事件
     */
    public void setPositiveButton(String text, OnClickListener listener) {
        setPositiveButton(text, true, listener);
    }

    /**
     * 设置确认按钮事件
     *
     * @param text     按钮名字，默认“确定”
     * @param dismiss  点击之后是否关闭Dialog
     * @param listener 点击事件
     */
    public void setPositiveButton(String text, final boolean dismiss, final OnClickListener listener) {
        this.showPosBtn = true;
        setButtonVisible();

        if (TextUtils.isEmpty(text)) {
            btPos.setText("确定");
        } else {
            btPos.setText(text);
        }

        btPos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(v);
                }
                if (dismiss) {
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 设置取消按钮事件,按钮名称默认“取消”
     *
     * @param listener 点击事件
     */
    public void setNegativeButton(OnClickListener listener) {
        setNegativeButton("", listener);
    }

    /**
     * 设置取消按钮事件
     *
     * @param text     按钮显示名称，默认“取消”
     * @param listener 点击事件
     */
    public void setNegativeButton(String text, OnClickListener listener) {
        setNegativeButton(text, true, listener);
    }

    /**
     * 设置取消按钮事件
     *
     * @param text     按钮显示名称，默认“取消”
     * @param dismiss  点击按钮后是否关闭Dialog
     * @param listener 点击事件
     */
    public void setNegativeButton(String text, final boolean dismiss, final OnClickListener listener) {
        this.showNegBtn = true;
        setButtonVisible();

        if (TextUtils.isEmpty(text)) {
            btNeg.setText("取消");
        } else {
            btNeg.setText(text);
        }

        btNeg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(v);
                }
                if (dismiss) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 设置按钮是否可见
     */
    private void setButtonVisible() {
        btNeg.setVisibility(View.VISIBLE);
        btPos.setVisibility(View.VISIBLE);

        if (showPosBtn && !showNegBtn) {
            btNeg.setVisibility(View.GONE);
            btPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        } else if (!showPosBtn && showNegBtn) {
            btPos.setVisibility(View.GONE);
            btNeg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        } else if (showPosBtn && showNegBtn) {
            btPos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btNeg.setBackgroundResource(R.drawable.alertdialog_left_selector);
        } else if (!showPosBtn && !showNegBtn) {
            btNeg.setVisibility(View.GONE);
            btPos.setText("确定");
            btPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btPos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
