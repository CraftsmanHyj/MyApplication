package com.hyj.lib.porgress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.hyj.lib.R;

/**
 * <pre>
 *     圆形进度条
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/22 12:34
 */
public class CircleProgressBar extends HorizontalProgressBar {
    private int radius = dp2px(30);
    private int maxPaintWidth;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initCircleAttrs(attrs);
    }

    /**
     * 获取圆形进度条的属性
     *
     * @param attrs
     */
    private void initCircleAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProBar);
        radius = (int) ta.getDimension(R.styleable.CircleProBar_radius, radius);
        ta.recycle();
    }

    @Override
    protected void initData() {
        super.initData();

        //增加视觉效果
        reachHeight = (int) (unReachHeight * 2.5f);

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);//防抖动
        paint.setStrokeCap(Paint.Cap.ROUND);//设置连接处为弧形
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        maxPaintWidth = Math.max(reachHeight, unReachHeight);
        //期望直径值，默认padding一致
        int expect = radius * 2 + maxPaintWidth + getPaddingLeft() + getPaddingRight();

        //系统提供：同样是根据不同模式去计算值的大小
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int realWidth = Math.min(width, height);//取宽高最小，保证圆形

        radius = (realWidth - getPaddingLeft() - getPaddingRight() - maxPaintWidth) / 2;

        setMeasuredDimension(realWidth, realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = paint.measureText(text);
        float textHeight = (paint.descent() + paint.ascent()) / 2;

        canvas.save();

        canvas.translate(getPaddingLeft() + maxPaintWidth / 2, getPaddingTop() + maxPaintWidth / 2);
        paint.setStyle(Paint.Style.STROKE);

        //draw unreach bar
        paint.setColor(unReachColor);
        paint.setStrokeWidth(unReachHeight);
        canvas.drawCircle(radius, radius, radius, paint);

        //draw reach bar
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(reachColor);
        paint.setStrokeWidth(reachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, radius * 2, radius * 2), -90, sweepAngle, true, paint);//画一个弧

        //draw text
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setStrokeWidth(textSize);
        canvas.drawText(text, radius - textWidth / 2, radius - textHeight, paint);

        canvas.restore();
    }
}
