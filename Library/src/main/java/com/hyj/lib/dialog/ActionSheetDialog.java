package com.hyj.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyj.lib.R;

import java.util.List;

/**
 * 底部弹出Dialog控件
 */
public class ActionSheetDialog {
    private Context context;

    private TextView tvTitle;
    private TextView tvCancel;
    private ScrollView svContent;
    private LinearLayout llContent;

    //当自定义布局的时候显示此容器，隐藏SvContent
    private LinearLayout llCusContent;

    private Display display;//屏幕宽、高参数
    private Dialog dialog;

    /**
     * 是否需要显示标题
     */
    private boolean showTitle = false;
    /**
     * 点击取消按钮的事件
     */
    private OnSheetItemClickListener onCancelListener;

    /**
     * 设置点击取消按钮事件
     *
     * @param onCancelListener
     */
    public void setOnCancelListener(OnSheetItemClickListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    /**
     * 点击back是否可以返回
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
     * @return
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    public ActionSheetDialog(Context context) {
        this(context, null);
    }

    public ActionSheetDialog(Context context, List<ActionSheetItem> lSheetItem) {
        this(context, null, lSheetItem);
    }

    public ActionSheetDialog(Context context, String title, List<ActionSheetItem> lSheetItem) {
        this.context = context;

        initView();
        setTitle(title);
        setSheetItems(lSheetItem);
    }

    private void initView() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_actionsheet, null);
        view.setMinimumWidth(display.getWidth());// 设置Dialog最小宽度为屏幕宽度

        tvTitle = (TextView) view.findViewById(R.id.sheetTvTitle);
        // 获取自定义Dialog布局中的控件
        svContent = (ScrollView) view.findViewById(R.id.sheetSvContent);
        llContent = (LinearLayout) view.findViewById(R.id.sheetLlContent);
        //显示自定义布局的容器
        llCusContent = (LinearLayout) view.findViewById(R.id.sheetLLCusContent);

        tvCancel = (TextView) view.findViewById(R.id.sheetTvCancel);
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onCancelListener) {
                    onCancelListener.onClick(-1);
                }
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    /**
     * <pre>
     *     设置弹出对话框标题
     *     此方法需要调用在setSheetItems()方法之前
     * </pre>
     *
     * @param title
     */
    public void setTitle(String title) {
        showTitle = !TextUtils.isEmpty(title);
        if (showTitle) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置自定义Dialog布局视图
     * TODO 高度控制是否有更好的方法？如何能达到动态计算View高度去控制？
     *
     * @param view     布局文件
     * @param rowCount 显示行数
     */
    public void setContentView(View view, int rowCount) {
        svContent.setVisibility(View.GONE);
        llCusContent.setVisibility(View.VISIBLE);

        if (showTitle) {
            llCusContent.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
        } else {
            llCusContent.setBackgroundResource(R.drawable.actionsheet_single_selector);
        }

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        LayoutParams params = (LayoutParams) llCusContent.getLayoutParams();
        if (rowCount >= 4) {
            params.height = display.getHeight() / 2;
        } else {
            params.height = LayoutParams.WRAP_CONTENT;
        }
        llCusContent.setLayoutParams(params);

        llCusContent.removeAllViews();
        llCusContent.addView(view);
    }

    /**
     * 设置Item条目
     *
     * @param lSheetItem
     */
    public void setSheetItems(List<ActionSheetItem> lSheetItem) {
        svContent.setVisibility(View.VISIBLE);
        llCusContent.setVisibility(View.GONE);

        llContent.removeAllViews();
        if (lSheetItem == null || lSheetItem.size() <= 0) {
            return;
        }

        int itemCount = lSheetItem.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        LayoutParams params = (LayoutParams) svContent.getLayoutParams();
        if (itemCount >= 7) {
            params.height = display.getHeight() / 2;
        } else {
            params.height = LayoutParams.WRAP_CONTENT;
        }
        svContent.setLayoutParams(params);

        // 循环添加条目
        for (int i = 0; i < itemCount; i++) {
            ActionSheetItem sheetItem = lSheetItem.get(i);
            SheetItemColor color = sheetItem.color;

            TextView textView = new TextView(context);
            textView.setText(sheetItem.name);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);

            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));

            // 背景图片
            if (itemCount == 1) {//只有一个Item
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 0 && i < itemCount - 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 0) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < itemCount - 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }

            final int index = i;
            final OnSheetItemClickListener listener = sheetItem.itemClickListener;
            // 点击事件
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onClick(index);
                    }
                    dialog.dismiss();
                }
            });

            llContent.addView(textView);
        }
    }

    /**
     * 显示对话框
     */
    public void show() {
        dialog.show();
    }

    /**
     * 关闭Dialog弹出框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * 选点点击事件接口
     */
    public interface OnSheetItemClickListener {
        /**
         * 点击事件
         *
         * @param index
         */
        void onClick(int index);
    }

    /**
     * 颜色枚举类
     */
    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}