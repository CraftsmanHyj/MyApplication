package com.hyj.lib.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.hyj.lib.R;

/**
 * <pre>
 *     圆形图片组件
 * </pre>
 *
 * @Author hyj
 * @Date 2016/4/26 22:27
 */
public class CircleImageView extends ImageView {
    private int outCircleWidth = 10;//外圆的宽度
    private int outCircleColor = Color.GRAY;//外圆的颜色

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myInit(context, attrs);
    }

    private void myInit(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (null == attrs) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.circleImageView);
        outCircleColor = ta.getColor(R.styleable.circleImageView_outCircleColor, outCircleColor);

        //将outCircleWidth的值转换成dp
        outCircleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, outCircleWidth, getResources().getDisplayMetrics());
        outCircleWidth = (int) ta.getDimension(R.styleable.circleImageView_outCircleWidth, outCircleWidth);

        ta.recycle();
    }
}
