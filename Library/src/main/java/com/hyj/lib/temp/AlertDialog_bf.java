package com.hyj.lib.temp;

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

public class AlertDialog_bf {
    private Context context;
    private Dialog dialog;

    private LinearLayout llContent;//父布局
    private TextView tvTitle;//标题
    private TextView tvMsg;//文字提示
    private LinearLayout llView;//预留给自定义的布局
    private ImageView imgLine;//水平分割线
    private Button btNeg;//确认按钮
    private Button btPos;//取消按钮

    private boolean showTitle = false;//显示标题
    private boolean showMsg = false;//显示类容
    private boolean showView = false;//显示自定义界面
    private boolean showPosBtn = false;//显示确认按钮
    private boolean showNegBtn = false;//显示取消按钮

    public AlertDialog_bf(Context context) {
        this.context = context;

        initView();
    }

    private void initView() {
        //获取到屏幕的参数
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        llContent = (LinearLayout) view.findViewById(R.id.alertLlContent);
        tvTitle = (TextView) view.findViewById(R.id.alertTvTitle);
        tvTitle.setVisibility(View.GONE);

        tvMsg = (TextView) view.findViewById(R.id.alertTvMsg);
        tvMsg.setVisibility(View.GONE);

        llView = (LinearLayout) view.findViewById(R.id.alertLlView);
        llView.setVisibility(View.GONE);

        imgLine = (ImageView) view.findViewById(R.id.alertImgLine);
        imgLine.setVisibility(View.GONE);

        btNeg = (Button) view.findViewById(R.id.alertBtNeg);
        btNeg.setVisibility(View.GONE);

        btPos = (Button) view.findViewById(R.id.alertBtPos);
        btPos.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        llContent.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置对话框标题
     *
     * @param title
     */
    public void setTitle(String title) {
        showTitle = true;
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText("标题");
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
        showMsg = true;
        if (TextUtils.isEmpty(msg)) {
            tvMsg.setText("内容");
        } else {
            tvMsg.setText(msg);
        }
    }

    /**
     * 自定义Dialog内容部分
     *
     * @param view
     */
    public void setContentView(View view) {
        if (null != view) {
            showView = true;
            llView.addView(view);
        }
    }

    /**
     * 设置Dialog是否可以点击外部取消
     *
     * @param cancel
     */
    public void setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
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
    public void setPositiveButton(String text, final OnClickListener listener) {
        showPosBtn = true;
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
                dialog.dismiss();
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
    public void setNegativeButton(String text, final OnClickListener listener) {
        showNegBtn = true;
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
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示Dialog
     */
    public void show() {
        setLayout();
        dialog.show();
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            tvTitle.setText("提示");
            tvTitle.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            tvTitle.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            tvMsg.setVisibility(View.VISIBLE);
        }

        if (showView) {
            llView.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btPos.setText("确定");
            btPos.setVisibility(View.VISIBLE);
            btPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btPos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btPos.setVisibility(View.VISIBLE);
            btPos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btNeg.setVisibility(View.VISIBLE);
            btNeg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            imgLine.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btPos.setVisibility(View.VISIBLE);
            btPos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btNeg.setVisibility(View.VISIBLE);
            btNeg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }
}
