package com.hyj.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义波形图
 * http://www.jianshu.com/p/cba46422de67
 * Created by hyj on 2016/11/1.
 */
public class WaveView extends View {
    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;   // 最大波纹半径
    private long mDuration = 2000; // 一个波纹从创建到消失的持续时间
    private int mSpeed = 500;   // 波纹的创建速度，每500ms创建一个
    private float mMaxRadiusRate = 0.9f;//最大半径缩放比例
    private boolean mMaxRadiusSet;//标记是否手动设置最大半径

    private boolean mIsRunning;//是否正在执行动画
    private long mLastCreateTime;//最后创建圆环的时间

    private List<Circle> mCircleList = new ArrayList<Circle>();
    private Interpolator mInterpolator = new LinearInterpolator();//圆环变大的差值器
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//画笔

    private int circleColor = Color.RED;//画笔颜色
    private boolean isFill = true;//填充，是否是实心圆环
    private boolean isEqual = false;//圆环是否是以等间距扩散

    /**
     * 设置执行动画的时间
     *
     * @param duration
     */
    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    /**
     * 设置动画执行的速度
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    /**
     * 设置圆环颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.circleColor = color;
        mPaint.setColor(color);
    }

    /**
     * 设置圆是否有填充色
     *
     * @param isFill
     */
    public void setFill(boolean isFill) {
        this.isFill = isFill;

        if (isFill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
    }

    /**
     * 设置圆是否是等间距扩散
     *
     * @param isEqual
     */
    public void setEqual(boolean isEqual) {
        this.isEqual = isEqual;

        if (isEqual) {
            mInterpolator = new AccelerateInterpolator(1.2f);
        } else {
            mInterpolator = new LinearOutSlowInInterpolator();
        }

        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    /**
     * 波形图中的圆环对象
     */
    private class Circle {
        private long mCreateTime;

        public Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        public int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
        }

        public float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }
}