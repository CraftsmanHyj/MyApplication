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

    private LinearLayout llContent;
    private TextView tvTitle;
    private TextView tvMsg;
    private ImageView imgLine;
    private Button btNeg;
    private Button btPos;

    private boolean showTitle = false;//显示标题
    private boolean showMsg = false;//显示类容
    private boolean showPosBtn = false;//显示确认按钮
    private boolean showNegBtn = false;//显示取消按钮

    public AlertDialog(Context context) {
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

    public void setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            tvTitle.setText("标题");
        } else {
            tvTitle.setText(title);
        }
    }

    public void setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            tvMsg.setText("内容");
        } else {
            tvMsg.setText(msg);
        }
    }

    public void setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
    }

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
