package com.hyj.demo.lock.lockpattern3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hyj.demo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *  所有以locus开头的图片都是九宫格要用到的图片
 *  九宫格解锁
 * </pre>
 *
 * @author hyj
 */
public class LockPointView extends View {
    private int pointNumber = 5;// 密码最小长度
    private int pointCount = 3;//密码行列数

    private Point[][] points;
    private boolean isInit = false;//是否已经被初始化

    private float width, height;// 屏幕宽高
    private float offsetsX, offsetsY;// 九宫格内容区域偏移量

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔

    private Bitmap locus_round_original;// 圆点初始状态时的图片
    private Bitmap locus_round_click;// 圆点点击时的图片
    private Bitmap locus_round_click_error;// 出错时圆点的图片
    private Bitmap locus_line;// 正常状态下线的图片
    private Bitmap locus_line_semicircle;// 正常状态下线图片尾部
    private Bitmap locus_line_error;// 错误状态下的线的图片
    private Bitmap locus_line_semicircle_error;// 错误状态下的线图片尾部
    private Bitmap locus_arrow;// 线的移动方向箭头图片
    private float bitRadius = 0;//图片半径

    private List<Point> lSelPoint = new ArrayList<Point>();// 选中点的集合
    private float movingX, movingY;// 鼠标移动坐标
    private boolean isSelect = false;// 是否可以开始绘制线条
    private boolean movingNoPoint = false;// 鼠标在移动但是不是九宫格里面的点
    private Matrix mMatrix = new Matrix();// 图片缩放矩阵

    private long CLEAR_TIME = 1 * 600;// 清除痕迹的时间
    private boolean isTouch = true; // 是否可操作

    private Timer timer = new Timer();//定时器
    private TimerTask task = null;//定时器任务

    //////////以下是实现九宫格非必须变量//////////
    private AudioManager audioManager;//铃声模式
    private SoundPool soundPool;//音效播放
    private Vibrator vibrator;//震动

    private boolean hasLine = true;//绘制线条是否可见
    private boolean hasShake = true;//按下是否震动
    private boolean hasSound = true;//是否有声音

    private OnCompleteListener completeListener;

    /**
     * 设置每行点的个数
     *
     * @param pointCount
     */
    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
        this.pointNumber = pointCount * 2 - 1;
        this.points = new Point[pointCount][pointCount];
    }

    /**
     * 绘制线条是否可见
     *
     * @param line
     */
    public void setHasLine(boolean line) {
        this.hasLine = line;
    }

    /**
     * 是否支持震动
     *
     * @param hasShake
     */
    public void setHasShake(boolean hasShake) {
        this.hasShake = hasShake;
    }

    /**
     * 是否有声音
     *
     * @param hasSound
     */
    public void setHasSound(boolean hasSound) {
        this.hasSound = hasSound;
    }

    /**
     * @param completeListener
     */
    public void setOnCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public LockPointView(Context context) {
        this(context, null);
    }

    public LockPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockPointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        myInit(context, attrs);
    }

    private void myInit(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initData();
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.lockPattern);

        pointCount = ta.getInteger(R.styleable.lockPattern_pointCount, pointCount);
        setPointCount(pointCount);

        hasLine = ta.getBoolean(R.styleable.lockPattern_hasLine, hasLine);
        hasSound = ta.getBoolean(R.styleable.lockPattern_hasSound, hasSound);
        hasShake = ta.getBoolean(R.styleable.lockPattern_hasShake, hasShake);

        ta.recycle();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(pointCount, AudioManager.STREAM_MUSIC, 5);
        //把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
        soundPool.load(getContext(), R.raw.lock, 1);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isInit) {
            initCanvas();
        }

        drawToCanvas(canvas);
    }

    /**
     * 初始化Cache信息
     */
    private void initCanvas() {
        // 1.图片资源
        locus_round_original = BitmapFactory.decodeResource(getResources(), R.drawable.locus_round_original);
        locus_round_click = BitmapFactory.decodeResource(getResources(), R.drawable.locus_round_click);
        locus_round_click_error = BitmapFactory.decodeResource(getResources(), R.drawable.locus_round_click_error);

        locus_line = BitmapFactory.decodeResource(getResources(), R.drawable.locus_line);
        locus_line_semicircle = BitmapFactory.decodeResource(getResources(), R.drawable.locus_line_semicircle);

        locus_line_error = BitmapFactory.decodeResource(getResources(), R.drawable.locus_line_error);
        locus_line_semicircle_error = BitmapFactory.decodeResource(getResources(), R.drawable.locus_line_semicircle_error);
        locus_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.locus_arrow);

        if (null == locus_round_original) {//获取不到图片资源
            return;
        }

        //2、获取屏幕的宽高
        width = getWidth();
        height = getHeight();

        // 3.偏移量
        if (width > height) {// 横屏
            offsetsX = (width - height) / 2;// 这个值就是九宫格内容区域的宽度
            width = height;
        } else {// 纵屏
            offsetsY = (height - width) / 2;
            height = width;
        }

        // 计算圆圈图片的大小
        float roundMaxWidth = width / 20.0f * 2;//8.0f * 2//图片最大宽度
        float deviation = width % (20 * 2) / 2;//内容区域偏差量 (8 * 2) / 2
        offsetsX += deviation;
        offsetsY += deviation;

        //对图片进行缩放
        if (locus_round_original.getWidth() > roundMaxWidth) {
            // 取得缩放比例，将所有的图片进行缩放
            float sf = roundMaxWidth * 1.0f / locus_round_original.getWidth();
            locus_round_original = BitmapUtil.zoom(locus_round_original, sf);
            locus_round_click = BitmapUtil.zoom(locus_round_click, sf);
            locus_round_click_error = BitmapUtil.zoom(locus_round_click_error, sf);

            locus_line = BitmapUtil.zoom(locus_line, sf);
            locus_line_semicircle = BitmapUtil.zoom(locus_line_semicircle, sf);

            locus_line_error = BitmapUtil.zoom(locus_line_error, sf);
            locus_line_semicircle_error = BitmapUtil.zoom(locus_line_semicircle_error, sf);
            locus_arrow = BitmapUtil.zoom(locus_arrow, sf);
        }

        // 4.点的坐标、设置密码
        float unitDistance = width / (points.length + 1);// 3个点将竖直/水平方向分成4分
        for (int row = 0, rCount = points.length; row < rCount; row++) {
            for (int column = 0, cCount = points[row].length; column < cCount; column++) {
                Point point = new Point(offsetsX + unitDistance * (column + 1), offsetsY + unitDistance * (row + 1));
                point.setIndex(row * rCount + column);
                points[row][column] = point;
            }
        }

        // 5.图片资源半径
        bitRadius = locus_round_original.getHeight() / 2;

        // 6.初始化完成
        isInit = true;
    }

    private void drawToCanvas(Canvas canvas) {
        // 画布抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        //1、画所有的点
        Bitmap bitmap = null;
        for (int row = 0, rCount = points.length; row < rCount; row++) {
            for (int column = 0, cCount = points[row].length; column < cCount; column++) {
                Point p = points[row][column];
                float x = p.getX() - bitRadius;
                float y = p.getY() - bitRadius;

                switch (p.getState()) {
                    case Point.STATE_NORMAL:
                        bitmap = locus_round_original;
                        break;

                    case Point.STATE_CHECK:
                        bitmap = locus_round_click;
                        break;

                    case Point.STATE_ERROR:
                        bitmap = locus_round_click_error;
                        break;
                }

                if (!hasLine && Point.STATE_ERROR != p.getState()) {//不显示绘制路劲
                    bitmap = locus_round_original;
                }

                canvas.drawBitmap(bitmap, x, y, mPaint);
            }
        }

        // 2、画点之间的连线
        if (lSelPoint.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();//获取画笔原本透明度

            // 绘制九宫格里面的点
            Point prePoint = lSelPoint.get(0);
            if (!hasLine && Point.STATE_ERROR != prePoint.getState()) {
                mPaint.setAlpha(0);
            }

            for (int i = 1; i < lSelPoint.size(); i++) {
                Point nextPoint = lSelPoint.get(i);
                drawLine(canvas, prePoint, nextPoint);
                prePoint = nextPoint;
            }

            // 绘制鼠标坐标点
            if (movingNoPoint) {
                drawLine(canvas, prePoint, new Point((int) movingX, (int) movingY));
            }

            mPaint.setAlpha(tmpAlpha);
        }
    }

    /**
     * 画两点的连接
     *
     * @param canvas
     * @param a
     * @param b
     */
    private void drawLine(Canvas canvas, Point a, Point b) {
        float lineLength = (float) Point.distance(a, b);//两点之间长度
        //计算旋转角度
        float degrees = (float) Math.toDegrees(Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()));
        canvas.rotate(degrees, a.getX(), a.getY());

        Bitmap line;//线条主体部分
        Bitmap lineEnd;//线条尾端部分
        if (a.getState() == Point.STATE_ERROR) {
            line = locus_line_error;
            lineEnd = locus_line_semicircle_error;
        } else {
            line = locus_line;
            lineEnd = locus_line_semicircle;
        }

        //画连线
        mMatrix.setScale((lineLength - lineEnd.getWidth()) / line.getWidth(), 1);
        mMatrix.postTranslate(a.getX(), a.getY() - line.getHeight() / 2.0f);
        canvas.drawBitmap(line, mMatrix, mPaint);
        canvas.drawBitmap(lineEnd, a.getX() + lineLength - lineEnd.getWidth(), a.getY() - line.getHeight() / 2.0f, mPaint);
        //画箭头
        canvas.drawBitmap(locus_arrow, a.getX(), a.getY() - locus_arrow.getHeight() / 2.0f, mPaint);

        // 画完线之后把角度旋转回来
        canvas.rotate(-degrees, a.getX(), a.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {// 不可操作
            return false;
        }

        movingNoPoint = false;
        movingX = event.getX();
        movingY = event.getY();

        boolean isTouchUp = false;//是否是触摸抬起操作
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
                // 如果正在清除密码,则取消
                if (null != task) {
                    task.cancel();
                    task = null;
                }

                resetPoint();//重置九宫格界面
                if (checkSelectPoint(isTouchUp)) {
                    isSelect = true;
                }
                break;

            case MotionEvent.ACTION_MOVE: // 移动
                if (isSelect && !checkSelectPoint(isTouchUp)) {
                    movingNoPoint = true;
                }
                break;

            case MotionEvent.ACTION_UP: // 抬起
                isSelect = false;
                isTouchUp = true;
                checkSelectPoint(isTouchUp);
                break;
        }

        // 绘制结束,判断当前绘制的状态
        int pointCount = lSelPoint.size();
        if (isTouchUp && pointCount > 0) {
            if (1 == pointCount) {// 绘制不成立
                resetPoint();
            } else if (pointCount < pointNumber && pointCount > 1) {
                errorPoint(); // 绘制错误
                showToast("密码最小长度为 " + pointNumber + " 位,请重新输入");
            } else if (pointCount >= pointNumber && null != completeListener) {// 绘制成功
                isTouch = false;

                StringBuffer sb = new StringBuffer();
                for (Point p : lSelPoint) {
                    sb.append(File.separator);
                    sb.append(p.getIndex());
                }
                completeListener.onComplete(sb.deleteCharAt(0).toString());
            }
        }

        postInvalidate();// 刷新界面
        return true;
    }

    /**
     * <pre>
     *     1、检测手指滑过的点是否是九宫格上的点
     *     2、该点点是否可用，经过九宫格的点是否已选中
     * </pre>
     *
     * @param isTouchUp 是否是触摸抬起操作，最后抬起瞬间的点不检查
     * @return
     */
    private boolean checkSelectPoint(boolean isTouchUp) {
        for (int row = 0, rCount = points.length; row < rCount; row++) {
            for (int col = 0, cCount = points[row].length; col < cCount; col++) {
                Point p = points[row][col];
                if (Point.checkInRound(p.getX(), p.getY(), bitRadius, movingX, movingY)) {

                    if (!isTouchUp) {
                        if (lSelPoint.contains(p)) {
                            movingNoPoint = true;
                            return false;
                        } else {
                            p.setState(Point.STATE_CHECK);
                            lSelPoint.add(p);

                            if (hasSound) {
                                playMusic();
                            }

                            if (hasShake) {
                                vibrator.vibrate(30);
                            }
                        }
                    }

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 播放音效
     */
    private void playMusic() {
        //判断当前手机模式不是普通模式则不发出声音
        if (AudioManager.RINGER_MODE_NORMAL != audioManager.getRingerMode()) {
            return;
        }

        // 参数说明：播放的音乐id;左声道音量;右声道音量;优先级0为最低;
        // 循环次数，0无不循环，-1无永远循环;回放速度 ，该值在0.5-2.0之间，1为正常速度;
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置已选中的点，选中状态为错误
     */
    public void errorPoint() {
        for (Point p : lSelPoint) {
            p.setState(Point.STATE_ERROR);
        }
        clearPassword();
    }

    /**
     * 重置点的状态
     */
    public void resetPoint() {
        for (Point p : lSelPoint) {
            p.setState(Point.STATE_NORMAL);
        }
        lSelPoint.clear();

        isTouch = true;
    }

    /**
     * 清除已绘制的密码
     */
    public void clearPassword() {
        if (CLEAR_TIME > 0) {
            if (null != task) {
                task.cancel();
            }
            postInvalidate();
            task = new TimerTask() {
                public void run() {
                    resetPoint();
                    postInvalidate();
                }
            };
            timer.schedule(task, CLEAR_TIME);
        } else {
            resetPoint();
            postInvalidate();
        }
    }

    /**
     * 轨迹绘画完成事件
     *
     * @author: hyj
     */
    public interface OnCompleteListener {
        /**
         * 绘制完成
         *
         * @param password
         */
        public void onComplete(String password);
    }
}
