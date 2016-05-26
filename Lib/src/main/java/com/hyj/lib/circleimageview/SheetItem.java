package com.hyj.lib.circleimageview;

import java.io.Serializable;

/**
 * <pre>
 *     窗口弹出Item实体类
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/14 17:31
 */
public class SheetItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String BULE = "#037BFF";
    public static final String RED = "#FD4A2E";

    private String name;
    private String color;
    private ActionSheetDialog.OnItemClickListener listener;

    public SheetItem() {
    }

    public SheetItem(String name) {
        this.name = name;
    }

    public SheetItem(String name, ActionSheetDialog.OnItemClickListener listener) {
        this.name = name;
        this.listener = listener;
    }

    public SheetItem(String name, String color, ActionSheetDialog.OnItemClickListener listener) {
        this.name = name;
        this.color = color;
        this.listener = listener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ActionSheetDialog.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(ActionSheetDialog.OnItemClickListener listener) {
        this.listener = listener;
    }
}
