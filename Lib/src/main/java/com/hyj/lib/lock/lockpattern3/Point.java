package com.hyj.lib.lock.lockpattern3;

import java.io.Serializable;

/**
 * 九宫格中的点
 */
public class Point implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 状态：未选中
     */
    public static int STATE_NORMAL = 0X001;
    /**
     * 状态：选中
     */
    public static int STATE_CHECK = 0X002;
    /**
     * 状态：选择错误
     */
    public static int STATE_CHECK_ERROR = 0X003;

    public int state = STATE_NORMAL;
    public int index = 0;// 下标

    public float x;
    public float y;


    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * 两点间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }

    /**
     * 计算点a(x,y)的角度
     *
     * @param x
     * @param y
     * @return
     */
    public static double pointTotoDegrees(double x, double y) {
        return Math.toDegrees(Math.atan2(x, y));
    }

    /**
     * 点在圆肉
     *
     * @param sx
     * @param sy
     * @param r
     * @param x
     * @param y
     * @return
     */
    public static boolean checkInRound(float sx, float sy, float r, float x, float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }
}
