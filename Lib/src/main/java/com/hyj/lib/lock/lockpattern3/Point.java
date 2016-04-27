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
    public static final int STATE_NORMAL = 0X001;
    /**
     * 状态：选中
     */
    public static final int STATE_CHECK = 0X002;
    /**
     * 状态：选择错误
     */
    public static final int STATE_CHECK_ERROR = 0X003;

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
     * @param a
     * @param b
     * @return
     */
    public static double distance(Point a, Point b) {
        // x轴差的平方加上y轴差的平方,对和开放
        return Math.sqrt(Math.abs(a.getX() - b.getX()) * Math.abs(a.getX() - b.getX())
                + Math.abs(a.getY() - b.getY()) * Math.abs(a.getY() - b.getY()));
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
     * 移动点是否跟原来的点重合
     *
     * @param px     参考点的X
     * @param py     参考点的Y
     * @param radius 圆的半径
     * @param mx     移动点的X
     * @param my     移动点的Y
     * @return 是否重合
     */
    public static boolean checkInRound(float px, float py, float radius, float mx, float my) {
        // 开方
        return Math.sqrt((px - mx) * (px - mx) + (py - my) * (py - my)) < radius;
    }
}
