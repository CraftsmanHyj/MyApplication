package com.hyj.demo.six;

import java.io.Serializable;

/**
 * 棋子
 * Created by hyj on 2016/5/20.
 */
public class Chess implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 黑方
     */
    public static final int BLACK = 0X00000001;
    /**
     * 白方
     */
    public static final int WHITE = 0X00000002;

    private int x;
    private int y;
    private int type;
    private boolean isSelect;//true：点已经被选中；false：点未被选中；

    public Chess() {
    }

    public Chess(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Chess(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * true：点已经被选中；false：点未被选中；
     *
     * @return
     */
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "Chess(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof Chess)) {
            return false;
        }

        Chess point = (Chess) obj;
        if (this.x != point.x || this.y != point.y) {
            return false;
        }
        return true;
    }
}
