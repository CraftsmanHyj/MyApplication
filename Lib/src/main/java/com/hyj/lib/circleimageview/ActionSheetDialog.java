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

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/8 12:56
 */
public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancle;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean isShowTitle;//是否显示title
    private Display display;//拿到屏幕宽度

    private List<SheetItem> sheetItemList;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = manager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.circleimg_dialog, null);
        view.setMinimumWidth(display.getWidth());//设置view的最小宽度

        lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancle = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog设置
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public ActionSheetDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            isShowTitle = true;
            txt_title.setText(title);
            txt_title.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置点击旁边的区域是否能取消
     *
     * @param cancel
     * @return
     */
    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置点击旁边的区域是否能取消
     *
     * @param cancel
     * @return
     */
    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }


    /**
     * 添加Item对象
     *
     * @param name
     * @param color
     * @param listener
     * @return
     */
    public ActionSheetDialog addSheetItem(String name, SheetItemColor color, onSheetItemClickListener listener) {
        if (null == sheetItemList) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(name, color, listener));
        return this;
    }

    public void show() {
        setSheetItem();
        dialog.show();
    }

    /**
     * 设置列表项
     */
    private void setSheetItem() {
        if (null == sheetItemList || sheetItemList.size() < 1) {
            return;
        }

        int size = sheetItemList.size();
        if (size > 6) {//控制高度
            ViewGroup.LayoutParams params = sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        for (int i = 1; i <= size; i++) {
            final int index = i;

            final SheetItem sheetItem = sheetItemList.get(i - 1);
            TextView textView = new TextView(context);
            textView.setText(sheetItem.name);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            if (1 == size) {
                if (isShowTitle) {
                    textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_bottom_selector
                } else {
                    textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_single_pressed
                }
            } else {
                if (isShowTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_middle_selector
                    } else {
                        textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_bottom_selector
                    }
                } else {
                    if (1 == i) {
                        textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_top_selector
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_middle_selector
                    } else {
                        textView.setBackgroundResource(R.drawable.text_bg);//R.drawable.actionsheet_bottom_selector
                    }
                }
            }

            if (sheetItem.color != null) {
                textView.setTextColor(Color.parseColor(sheetItem.color.getName()));
            } else {
                textView.setTextColor(Color.parseColor(SheetItemColor.BULE.getName()));
            }

            //设置高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sheetItem.listener.onClick(index);
                    dialog.dismiss();
                }
            });

            lLayout_content.addView(textView);
        }
    }

    private class SheetItem {
        public String name;
        public onSheetItemClickListener listener;
        public SheetItemColor color;

        public SheetItem(String name, SheetItemColor color, onSheetItemClickListener listener) {
            this.name = name;
            this.listener = listener;
            this.color = color;
        }
    }

    /**
     * Item点击事件
     */
    public interface onSheetItemClickListener {
        public void onClick(int witch);
    }

    /**
     * 颜色枚举
     */
    public enum SheetItemColor {

        BULE("#037BFF"), RED("#FD4A2E");

        String name;

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
