package com.hyj.lib.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hyj.lib.R;

/**
 * 模仿Windows 10进度条
 * 参考资料：http://blog.csdn.net/zhangml0522/article/details/52556418
 * http://blog.csdn.net/niuzhucedenglu/article/details/52623497
 *
 * @Author hyj
 * @Date 2016/10/13 22:41
 */
public class WinProgressBar extends View {
    private Paint mPaint;// 绘制ProgressBar的画笔
    private int paintWidth = 5;// 定义ProgressBar画笔的粗细
    private int paintColor = Color.WHITE;//画笔颜色
    private Path mPath;// ProgressBar的整个路径
    private Path dstPath = new Path();// ProgrerssBar当前的路径
    private PathMeasure mPathMeasure; // 用于测量ProgressBar的路径
    private int viewWidth;//画布限定为正方形，画布的宽高
    private ValueAnimator valueAnimator; // ProgressBar的动画效果

    private float time;// 动画的进度
    private float pathLength;// ProgressBar路径的总长度

    public WinProgressBar(Context context) {
        this(context, null);
    }

    public WinProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setLayerType(LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速 , 解决低版本无动画效果的问题
        this.paintWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paintWidth, context.getResources().getDisplayMetrics());

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.WinProBar);
        paintWidth = (int) ta.getDimension(R.styleable.WinProBar_paintWidth, paintWidth);
        paintColor = ta.getColor(R.styleable.WinProBar_paintColor, paintColor);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        //根据模式去取控件的宽高值
        int mWidth = resolveSize(w, widthMeasureSpec);
        int mHeight = resolveSize(h, heightMeasureSpec);

        viewWidth = Math.min(mWidth, mHeight);
        setMeasuredDimension(viewWidth, viewWidth);

        init();
    }

    private void init() {
        // 初始化ProgressBar的画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);// 设置画笔尺寸的大小
        mPaint.setColor(paintColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔为圆笔
        mPaint.setAntiAlias(true);// 抗锯齿

        // 初始化ProgressBar的路径
        mPath = new Path();
        float paintOffset = paintWidth / 2;// 画笔偏移量（画笔粗细可以造成绘制效果的偏差）
        // 小知识：Rect是使用int类型作为数值，RectF是使用float类型作为数值
        RectF rectF = new RectF(paintOffset, paintOffset, viewWidth - paintOffset, viewWidth - paintOffset);
        mPath.addArc(rectF, -90f, 359.9f);// 默认从上方开始;这里角度不能使用360f，会导致起点从0度开始而不是-90度开始
        mPathMeasure = new PathMeasure(mPath, true);
        pathLength = mPathMeasure.getLength();
        // 初始化动画效果
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        valueAnimator.setRepeatCount(-1); // 无限重复
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                time = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();// 启动动画效果
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        dstPath.reset(); //将路径重置

        // 解决起点闪烁，这样写可避免起点改变造成闪烁的问题
        if (time >= 0.95) {
            setDstPath(0);
            canvas.drawPath(dstPath, mPaint);
        }

        int num = (int) (time / 0.05);
        float y, x;
        switch (num) {
            default:
            case 3:
                x = time - 0.15f * (1 - time);
                y = -pathLength * x * x + 2 * pathLength * x;
                setDstPath(y);
            case 2:
                x = time - 0.10f * (1 - time);
                y = -pathLength * x * x + 2 * pathLength * x;
                setDstPath(y);
            case 1:
                x = time - 0.05f * (1 - time);
                y = -pathLength * x * x + 2 * pathLength * x;
                setDstPath(y);
            case 0:
                x = time;
                y = -pathLength * x * x + 2 * pathLength * x;
                setDstPath(y);
                break;
        }
        canvas.drawPath(dstPath, mPaint);
    }

    private void setDstPath(float positionOnPath) {
        mPathMeasure.getSegment(positionOnPath, positionOnPath + 1, dstPath, true);
    }
}