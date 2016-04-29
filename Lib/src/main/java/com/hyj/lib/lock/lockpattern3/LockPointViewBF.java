package com.hyj.lib.lock.lockpattern3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hyj.lib.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 所有以locus开头的图片都是九宫格要用到的图片<br>
 * 九宫格解锁
 *
 * @author way
 */
public class LockPointViewBF extends View {
    private int pointNumber = 5;// 密码最小长度
    private int pointCount = 3;//密码行列数

    private Point[][] points = new Point[pointCount][pointCount];
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


    private int lineAlpha = 0;// 连线的透明度
    private long CLEAR_TIME = 600;// 清除痕迹的时间
    private boolean isTouch = true; // 是否可操作

    private Timer timer = new Timer();//定时器
    private TimerTask task = null;//定时器任务

    private OnCompleteListener completeListener;

    /**
     * 设置每行点的个数
     *
     * @param pointCount
     */
    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
        this.pointNumber = pointCount * 2 - 1;

        this.isInit = false;
        invalidate();
    }

    /**
     * @param completeListener
     */
    public void setOnCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public LockPointViewBF(Context context) {
        this(context, null);
    }

    public LockPointViewBF(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockPointViewBF(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        float roundMaxW = width / 20.0f * 2;//8.0f * 2//图片最大宽度
        float deviation = width % (20 * 2) / 2;//内容区域偏差量 (8 * 2) / 2
        offsetsX += deviation;
        offsetsY += deviation;

        //对图片进行缩放
        if (locus_round_original.getWidth() > roundMaxW) {
            float sf = roundMaxW * 1.0f / locus_round_original.getWidth(); // 取得缩放比例，将所有的图片进行缩放
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
                Point point = new Point(offsetsX + unitDistance * (column + 1),
                        offsetsY + unitDistance * (row + 1));
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
        float x, y;
        Bitmap bitmap = null;
        for (int row = 0, rCount = points.length; row < rCount; row++) {
            for (int column = 0, cCount = points[row].length; column < cCount; column++) {
                Point p = points[row][column];
                x = p.getX() - bitRadius;
                y = p.getY() - bitRadius;

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
                canvas.drawBitmap(bitmap, x, y, mPaint);
            }
        }

        // 2、画点之间的连线
        if (lSelPoint.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();//获取线条原来的透明度
            mPaint.setAlpha(lineAlpha);
            // 绘制九宫格里面的点
            Point prePoint = lSelPoint.get(0);
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
            lineAlpha = mPaint.getAlpha();
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

        float degrees = getDegrees(a, b);//旋转角度
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


    /**
     * 获取角度
     *
     * @param a
     * @param b
     * @return
     */
    private float getDegrees(Point a, Point b) {
        return (float) Math.toDegrees(Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()));
    }

    /**
     * 检查鼠标的坐标和九宫格上的点是否吻合
     */
    private Point checkSelectPoint() {
        for (int row = 0; row < points.length; row++) {
            for (int col = 0; col < points[row].length; col++) {
                Point p = points[row][col];
                if (Point.checkInRound(p.getX(), p.getY(), bitRadius, movingX, movingY)) {
                    //添加震动、声音代码
                    return p;
                }
            }
        }
        return null;
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
     * 交叉点
     *
     * @param point 点
     * @return 是否交差
     */
    private boolean crossPoint(Point point) {
        if (lSelPoint.contains(point)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {// 不可操作
            return false;
        }

        movingNoPoint = false;
        movingX = event.getX();
        movingY = event.getY();

        Point point = null;
        boolean isFinish = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 点下
                // 如果正在清除密码,则取消
                if (null != task) {
                    task.cancel();
                    task = null;
                }

                // 删除之前的点
                resetPoint();
                point = checkSelectPoint();
                if (null != point) {
                    isSelect = true;
                }
                break;

            case MotionEvent.ACTION_MOVE: // 移动
                if (isSelect) {
                    point = checkSelectPoint();
                    if (null == point) {
                        movingNoPoint = true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP: // 提起
                point = checkSelectPoint();
                isSelect = false;
                isFinish = true;
                break;
        }

        // 选中重复检查
        if (isSelect && !isFinish && null != point) {
            if (crossPoint(point)) {
                movingNoPoint = true;
            } else {
                point.setState(Point.STATE_CHECK);
                lSelPoint.add(point);
            }
        }

        // 绘制结束
        int pointCount = lSelPoint.size();
        if (isFinish && pointCount > 0) {
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
     * 设置已选中的点，选中状态为错误
     */
    public void errorPoint() {
        for (Point p : lSelPoint) {
            p.setState(Point.STATE_ERROR);
        }
        clearPassword();
    }

    /**
     * 清除已绘制的密码
     */
    public void clearPassword() {
        if (CLEAR_TIME > 0) {
            if (null != task) {
                task.cancel();
            }
            lineAlpha = 130;
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
     * 显示提示信息
     *
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
