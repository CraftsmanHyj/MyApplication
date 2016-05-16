package com.hyj.lib.circleimageview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyj.lib.R;

import java.util.List;

/**
 * <pre>
 *     底部弹出选项Dialog
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/8 12:56
 */
public class ActionSheetDialog {
    private float textSize = 18;//字体大小
    private Context context;

    private TextView tvTitle;
    private TextView tvCancel;
    private LinearLayout llContent;
    private ScrollView svContent;

    private Dialog dialog;
    private Display display;//拿到屏幕宽度

    private List<SheetItem> lSheetItem;

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置点击旁边的区域是否能取消
     *
     * @param cancel
     * @return
     */
    public void setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
    }

    /**
     * 设置点击旁边的区域是否能取消
     *
     * @param cancel
     * @return
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 显示弹出窗体
     */
    public void show() {
        dialog.show();
    }

    public ActionSheetDialog(Context context, List<SheetItem> lSheetItem) {
        this.context = context;
        this.lSheetItem = lSheetItem;

        initView();
        initSheetItem();
    }

    public void initView() {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.display = manager.getDefaultDisplay();

        View view = LayoutInflater.from(context).inflate(R.layout.circleimg_dialog, null);
        view.setMinimumWidth(display.getWidth());//设置view的最小宽度

        llContent = (LinearLayout) view.findViewById(R.id.headLlContent);
        svContent = (ScrollView) view.findViewById(R.id.headSvContent);
        tvTitle = (TextView) view.findViewById(R.id.headTvTitle);
        tvCancel = (TextView) view.findViewById(R.id.headTvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog设置
        dialog = new Dialog(context, R.style.HeadSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置列表项
     */
    private void initSheetItem() {
        if (null == lSheetItem || lSheetItem.isEmpty()) {
            return;
        }

        int size = lSheetItem.size();
        if (size > 6) {//控制高度
            ViewGroup.LayoutParams params = svContent.getLayoutParams();
            params.height = display.getHeight() / 2;
            svContent.setLayoutParams(params);
        }

        for (int i = 0; i < size; i++) {
            SheetItem sheetItem = lSheetItem.get(i);
            llContent.addView(getTextView(sheetItem));

            if (i < (size - 1)) {
                llContent.addView(getLine());
            }
        }
    }

    /**
     * 获取一个TextView对象
     *
     * @return
     */
    private TextView getTextView(final SheetItem sheetItem) {
        TextView textView = new TextView(context);
        //高度缩放比
        float scaleHeight = context.getResources().getDisplayMetrics().density;
        int height = (int) (45 * scaleHeight + 0.5);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        textView.setTextSize(textSize);
        textView.setGravity(Gravity.CENTER);

        textView.setText(sheetItem.getName());
        if (null != sheetItem.getColor()) {
            textView.setTextColor(Color.parseColor(sheetItem.getColor()));
        } else {
            textView.setTextColor(Color.parseColor(SheetItem.BULE));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetItem.getListener().onItemClick();
                dialog.dismiss();
            }
        });

        return textView;
    }

    /**
     * 获取一条分割线
     *
     * @return
     */
    private View getLine() {
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        view.setBackgroundColor(Color.parseColor("#8F8F8F"));
        return view;
    }

    /**
     * Item点击事件
     */
    public interface OnItemClickListener {
        public void onItemClick();
    }
}
