package com.hyj.lib.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
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
    private int outCircleColor = Color.WHITE;//外圆的颜色

    private Paint paint;//定义画笔
    private int width, height;//控件宽高
    private Bitmap bitmap;//显示的图片

    /**
     * 设置外圆的颜色
     *
     * @param outCircleColor
     */
    public void setOutCircleColor(int outCircleColor) {
        this.outCircleColor = outCircleColor;
        if (null != paint) {
            paint.setColor(outCircleColor);
        }
        invalidate();
    }

    /**
     * 设置外圆的宽度
     *
     * @param outCircleWidth
     */
    public void setOutCircleWidth(int outCircleWidth) {
        outCircleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, outCircleWidth, getResources().getDisplayMetrics());
        this.outCircleWidth = outCircleWidth;
        invalidate();
    }

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
        initView();
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

    private void initView() {
        paint = new Paint();
        paint.setColor(outCircleColor);
        paint.setAntiAlias(true);//设置抗锯齿
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);

        width = w - outCircleWidth * 2;//减去外圆的宽度
        height = h - outCircleWidth * 2;

        setMeasuredDimension(w, h);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = width;
        }

        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = height;
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        loadImg();

        if (null == bitmap) {
            return;
        }

        int min = Math.min(height, width);

        int circleCenter = min / 2;
        bitmap = Bitmap.createScaledBitmap(bitmap, min, min, false);

        canvas.drawCircle(circleCenter + outCircleWidth, circleCenter + outCircleWidth, circleCenter + outCircleWidth, paint);
        canvas.drawBitmap(createCircleBitmap(bitmap, min), outCircleWidth, outCircleWidth, null);
    }

    /**
     * 生成一个圆形的图片
     *
     * @param img
     * @param min
     * @return
     */
    private Bitmap createCircleBitmap(Bitmap img, int min) {
        Bitmap bitmap = null;
        Paint paintCircle = new Paint();
        paintCircle.setAntiAlias(true);

        bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(min / 2, min / 2, min / 2, paintCircle);
        paintCircle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置画笔模式
        canvas.drawBitmap(img, 0, 0, paintCircle);

        return bitmap;
    }

    //加载图片
    private void loadImg() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (null != bitmapDrawable) {
            bitmap = bitmapDrawable.getBitmap();
        }
    }
}
