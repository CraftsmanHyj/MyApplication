package com.hyj.lib.dialog;


import com.hyj.lib.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.hyj.lib.dialog.ActionSheetDialog.SheetItemColor;

import java.io.Serializable;

/**
 * 选项Item实体类
 * Created by Administrator on 2016/6/16.
 */
public class ActionSheetItem implements Serializable {
    public static final long versionSerialUID = 1L;

    String name;
    ActionSheetDialog.OnSheetItemClickListener itemClickListener;
    SheetItemColor color = SheetItemColor.Blue;

    /**
     * <pre>
     *     选项Item构造函数
     *     默认Item字体颜色是：SheetItemColor.Blue
     * </pre>
     *
     * @param name
     * @param itemClickListener
     */
    public ActionSheetItem(String name, OnSheetItemClickListener itemClickListener) {
        this(name, SheetItemColor.Blue, itemClickListener);
    }

    /**
     * 选项Item全部变量构造函数
     *
     * @param name
     * @param color
     * @param itemClickListener
     */
    public ActionSheetItem(String name, SheetItemColor color, OnSheetItemClickListener itemClickListener) {
        this.name = name;
        this.color = color;
        this.itemClickListener = itemClickListener;
    }
}