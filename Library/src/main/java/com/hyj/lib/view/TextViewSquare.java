package com.hyj.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <pre>
 *     正方形Textview
 * </pre>
 *
 * @Author hyj
 * @Date 2016/7/30 9:18
 */
public class TextViewSquare extends TextView {
    public TextViewSquare(Context context) {
        this(context, null);
    }

    public TextViewSquare(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        /**
         * 宽、高中取最小的
         * 以适应LinearLayout中Vertical、Horizontal的情况
         * 这样无论什么时候都能保证正方形且在可见范围(使用weight的情况)
         */
        int minSize = Math.min(childWidthSize, childHeightSize);
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(minSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
