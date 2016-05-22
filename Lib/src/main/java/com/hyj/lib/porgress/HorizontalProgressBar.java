package com.hyj.lib.porgress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.hyj.lib.R;

/**
 * <pre>
 *     自定义控件几个步骤，也是要弄明白的几点
 *      1、自定义属性的申明与获取
 *      2、测量onMeasure
 *      3、布局onLayout(ViewGroup)
 *      4、绘制onDraw
 *      5、onTouchEvent
 *      6、onInterceptTouchEvent(ViewGroup)
 *      7、状态恢复与保存
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/22 9:46
 */
public class HorizontalProgressBar extends ProgressBar {
    private final int DEFAULT_TEXT_SIZE = 10;//SP
    private final int DEFAULT_TEXT_OFFSET = 10;
    private final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private final int DEFAULT_HEIGHT_UNREACH = 2;
    private final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private final int DEFAULT_HEIGHT_REACH = 2;

    //自定义属性
    protected int textSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int textOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected int unReachColor = DEFAULT_COLOR_UNREACH;
    protected int unReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int reachColor = DEFAULT_COLOR_REACH;
    protected int reachHeight = dp2px(DEFAULT_HEIGHT_REACH);

    //绘制工具
    protected Paint paint = new Paint();
    protected int realWidth;//当前控件的宽度-padding值,真正占用的宽度

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myInit(attrs);
    }

    protected void myInit(AttributeSet attrs) {
        initAttrs(attrs);
        initData();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProBar);

        textSize = (int) ta.getDimension(R.styleable.HorizontalProBar_pbTextSize, textSize);
        textColor = ta.getColor(R.styleable.HorizontalProBar_pbTextColor, textColor);
        textOffset = (int) ta.getDimension(R.styleable.HorizontalProBar_pbTextOffset, textOffset);
        unReachColor = ta.getColor(R.styleable.HorizontalProBar_pbUnreachColor, unReachColor);
        unReachHeight = (int) ta.getDimension(R.styleable.HorizontalProBar_PbUnreachHeight, unReachHeight);
        reachColor = ta.getColor(R.styleable.HorizontalProBar_pbReachColor, reachColor);
        reachHeight = (int) ta.getDimension(R.styleable.HorizontalProBar_pbReachHeight, reachHeight);

        ta.recycle();
    }

    protected void initData() {
        paint.setTextSize(textSize);//设置画笔绘制字体的大小
    }

    /**
     * 将dp转换成px
     *
     * @param dpValue
     * @return
     */
    protected int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    /**
     * 将sp转换成px
     *
     * @param spValue
     * @return
     */
    protected int sp2px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //因为是水平进度条，所以宽度肯定有一个具体的值(match_parent、其他)，因此不必分情况
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);

        int height = measureHeight(heightMeasureSpec);

        //确定view的宽高
        setMeasuredDimension(widthValue, height);

        //设置控件实际绘制的大小
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 拿到高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {//用户给了一个精确值
            height = size;
        } else {
            //高度取已显示、未显示、文字三者中最大的值
            int textHieght = (int) (paint.descent() - paint.ascent());
            int max = Math.max(reachHeight, unReachHeight);
            max = Math.max(max, Math.abs(textHieght));
            height = getPaddingTop() + getPaddingBottom() + max;

            //此种模式下，测量值不能超过给定的size值
            if (MeasureSpec.AT_MOST == mode) {
                height = Math.min(height, size);
            }
        }

        return height;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();

        //移动绘制坐标位置
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReach = false;//是否需要绘制未完成部分

        //draw reachBar
        String text = getProgress() + "%";
        int textWidth = (int) paint.measureText(text);//测量文本宽度

        float radio = getProgress() * 1.0f / getMax();//reachbar绘制长度
        float progressX = radio * realWidth;
        if (progressX + textWidth > realWidth) {
            progressX = realWidth - textWidth;
            noNeedUnReach = true;
        }

        float endX = progressX - textOffset / 2;//reachbar实际长度得减去textoffset/2
        if (endX > 0) {
            paint.setColor(reachColor);
            paint.setStrokeWidth(reachHeight);
            canvas.drawLine(0, 0, endX, 0, paint);
        }

        //draw text
        paint.setColor(textColor);
        int y = (int) (-(paint.descent() + paint.ascent()) / 2);//绘制文字的基线往下移动
        canvas.drawText(text, progressX, y, paint);

        //draw unreachBar
        if (!noNeedUnReach) {
            float start = progressX + textOffset / 2 + textWidth;
            paint.setColor(unReachColor);
            paint.setStrokeWidth(unReachHeight);
            canvas.drawLine(start, 0, realWidth, 0, paint);
        }

        canvas.restore();
    }
}
