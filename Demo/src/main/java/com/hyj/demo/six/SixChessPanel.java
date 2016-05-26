package com.hyj.demo.six;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyj.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     六子飞棋盘
 * </pre>
 *
 * @author hyj
 * @Date 2016-4-5 上午10:21:31
 */
public class SixChessPanel extends View {
    private final int MAX_LINE = 4;// 最大行数
    private final int DELETE_COUNT = 3;//当行、列刚好是这个数的时候才有可能吃掉子

    private float panelWidth;// 棋盘宽度
    private float lineHeight;// 每行高度

    private Paint paint;

    private Bitmap whitePiece;// 白色棋子
    private Bitmap whitePieceSel;//白方按下
    private Bitmap blackPiece;// 黑色棋子
    private Bitmap blackPieceSel;// 黑方按下
    private float ratioPieceOfLineHeight = 3 * 1.0f / 5;// 棋子大小比例

    private boolean isWhite = true;// 当前轮到白棋
    private Chess selChess = null;//当前选中，想要走的棋子
    private ArrayList<Chess> lChess;

    private boolean isGameOver;// 判断游戏是否结束

    private OnGameOverListener listener;

    /**
     * 设置游戏结束监听事件
     *
     * @param listener
     */
    public void setOnGameOverListener(OnGameOverListener listener) {
        this.listener = listener;
    }

    public SixChessPanel(Context context) {
        this(context, null);
    }

    public SixChessPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SixChessPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myInit();
    }

    private void myInit() {
        initDatas();
        initChess();
    }

    private void initDatas() {
        paint = new Paint();
        paint.setColor(0X88000000);
        paint.setDither(true);//防抖动
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.STROKE);// 画线

        whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        whitePieceSel = BitmapFactory.decodeResource(getResources(), R.drawable.composer_camera);
        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
        blackPieceSel = BitmapFactory.decodeResource(getResources(), R.drawable.composer_sleep);
    }

    /**
     * 初始化棋子
     */
    private void initChess() {
        lChess = new ArrayList<Chess>();
        Chess chess = new Chess(0, 0, Chess.BLACK);
        lChess.add(chess);
        chess = new Chess(1, 0, Chess.BLACK);
        lChess.add(chess);
        chess = new Chess(2, 0, Chess.BLACK);
        lChess.add(chess);
        chess = new Chess(3, 0, Chess.BLACK);
        lChess.add(chess);
        chess = new Chess(0, 1, Chess.BLACK);
        lChess.add(chess);
        chess = new Chess(3, 1, Chess.BLACK);
        lChess.add(chess);

        chess = new Chess(0, 2, Chess.WHITE);
        lChess.add(chess);
        chess = new Chess(3, 2, Chess.WHITE);
        lChess.add(chess);
        chess = new Chess(0, 3, Chess.WHITE);
        lChess.add(chess);
        chess = new Chess(1, 3, Chess.WHITE);
        lChess.add(chess);
        chess = new Chess(2, 3, Chess.WHITE);
        lChess.add(chess);
        chess = new Chess(3, 3, Chess.WHITE);
        lChess.add(chess);
    }


    /**
     * 测量棋盘大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        // 避免嵌套在scrollview中的时候无法显示
        if (MeasureSpec.UNSPECIFIED == widthMode) {
            width = heightSize;
        } else if (MeasureSpec.UNSPECIFIED == heightMode) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    /**
     * 当宽高确定了发生改变以后就会回调此方法
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        panelWidth = w;
        //计算每行的行间距
        lineHeight = panelWidth * 1.0f / MAX_LINE;

        // 根据值动态缩放图片大小
        int pieceWidth = (int) (lineHeight * ratioPieceOfLineHeight);
        whitePiece = Bitmap.createScaledBitmap(whitePiece, pieceWidth, pieceWidth, false);
        whitePieceSel = Bitmap.createScaledBitmap(whitePieceSel, pieceWidth, pieceWidth, false);
        blackPiece = Bitmap.createScaledBitmap(blackPiece, pieceWidth, pieceWidth, false);
        blackPieceSel = Bitmap.createScaledBitmap(blackPieceSel, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawChess(canvas);
    }


    /**
     * 绘制棋盘
     *
     * @param canvas
     */
    private void drawBoard(Canvas canvas) {
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (panelWidth - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            // 绘制横线
            canvas.drawLine(startX, y, endX, y, paint);
            // 绘制纵线(因为是正方形，所以他的纵线与横线恰好原理相反)
            canvas.drawLine(y, startX, y, endX, paint);
        }
    }

    /**
     * 绘制棋子
     *
     * @param canvas
     */
    private void drawChess(Canvas canvas) {
        for (int i = 0; i < lChess.size(); i++) {
            Chess chess = lChess.get(i);
            Bitmap bitTemp = null;
            if (Chess.WHITE == chess.getType()) {
                bitTemp = chess.isSelect() ? whitePieceSel : whitePiece;
            } else if (Chess.BLACK == chess.getType()) {
                bitTemp = chess.isSelect() ? blackPieceSel : blackPiece;
            }
            canvas.drawBitmap(bitTemp,
                    (chess.getX() + (1 - ratioPieceOfLineHeight) / 2) * lineHeight,
                    (chess.getY() + (1 - ratioPieceOfLineHeight) / 2) * lineHeight, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {// 若游戏结束，不响应事件
            return false;
        }

        if (MotionEvent.ACTION_UP != event.getAction()) {
            return true;
        }

        // 将点击的点转换成落子的点的坐标
        int x = (int) (event.getX() / lineHeight);
        int y = (int) (event.getY() / lineHeight);
        Chess chess = getChess(x, y);

        if (null != chess) {
            if (isWhite && Chess.BLACK == chess.getType()) {//应白方走子但是点的是黑方
                return true;
            } else if (!isWhite && Chess.WHITE == chess.getType()) {//应黑方走子但是点的是白方
                return true;
            }

            //之前有选中想要走的子
            if (null != selChess && !chess.equals(selChess)) {
                selChess.setSelect(false);
            }

            chess.setSelect(!chess.isSelect());
            selChess = chess;

            if (!chess.isSelect()) {//当子由选中变成不选的时候，置空当前选择的棋子
                selChess = null;
            }
        } else if (null == chess && null != selChess) {
            //控制只能走直线
            if (x != selChess.getX() && y != selChess.getY()) {
                return true;
            }

            int xStep = Math.abs(x - selChess.getX());
            int yStep = Math.abs(y - selChess.getY());
            if (1 != xStep && 1 != yStep) {//限定每次只能走一步
                return true;
            }

            selChess.setX(x);
            selChess.setY(y);
            selChess.setSelect(false);
            chessDownEvent();
            selChess = null;
            isWhite = !isWhite;
        }

        invalidate();// 通知重绘
        return true;// 消耗了这个事件，不往下传递
    }

    /**
     * 落子事件处理
     */
    private void chessDownEvent() {
        killHorizontal();
        killVertical();
        checkGameOver();
    }

    /**
     * 检测水平方向是否可以吃子
     */
    private void killHorizontal() {
        if (null == selChess) {
            return;
        }

        //获取水平方向上所有棋子
        List<Chess> lY = new ArrayList<Chess>();
        for (Chess bean : lChess) {
            if (selChess.getY() == bean.getY()) {
                lY.add(bean);
            }
        }

        if (DELETE_COUNT != lY.size()) {
            return;
        }

        Chess left = getChess(selChess.getX() - 1, selChess.getY());
        Chess right = getChess(selChess.getX() + 1, selChess.getY());

        if (!lY.contains(left) && lY.contains(right)) {//左边无子，右边有子
            if (selChess.getType() == right.getType()) {
                Chess del = getChess(selChess.getX() + 2, selChess.getY());
                if (lY.contains(del) && selChess.getType() != del.getType()) {
                    lChess.remove(del);
                }
            }
        } else if (!lY.contains(right) && lY.contains(left)) {//右边无子,左边有子
            if (selChess.getType() == left.getType()) {
                Chess del = getChess(selChess.getX() - 2, selChess.getY());
                if (lY.contains(del) && selChess.getType() != del.getType()) {
                    lChess.remove(del);
                }
            }
        } else if (lY.contains(left) && lY.contains(right)) {//左右两边都有
            if (left.getType() != right.getType()) {
                if (selChess.getType() == left.getType()) {
                    lChess.remove(right);
                } else if (selChess.getType() == right.getType()) {
                    lChess.remove(left);
                }
            }
        }
    }

    /**
     * 检查垂直方向是否可以吃子
     */
    private void killVertical() {
        if (null == selChess) {
            return;
        }

        //获取垂直方向上所有棋子
        List<Chess> lX = new ArrayList<Chess>();
        for (Chess bean : lChess) {
            if (selChess.getX() == bean.getX()) {
                lX.add(bean);
            }
        }

        if (DELETE_COUNT != lX.size()) {
            return;
        }

        Chess top = getChess(selChess.getX(), selChess.getY() - 1);
        Chess bottom = getChess(selChess.getX(), selChess.getY() + 1);

        if (!lX.contains(top) && lX.contains(bottom)) {//上方无子，下方有子
            if (selChess.getType() == bottom.getType()) {
                Chess del = getChess(selChess.getX(), selChess.getY() + 2);
                if (lX.contains(del) && selChess.getType() != del.getType()) {
                    lChess.remove(del);
                }
            }
        } else if (!lX.contains(bottom) && lX.contains(top)) {//下方无子，上方有子
            if (selChess.getType() == top.getType()) {
                Chess del = getChess(selChess.getX(), selChess.getY() - 2);
                if (lX.contains(del) && selChess.getType() != del.getType()) {
                    lChess.remove(del);
                }
            }
        } else if (lX.contains(top) && lX.contains(bottom)) {//上下两方都有子
            if (top.getType() != bottom.getType()) {
                if (selChess.getType() == top.getType()) {
                    lChess.remove(bottom);
                } else if (selChess.getType() == bottom.getType()) {
                    lChess.remove(top);
                }
            }
        }
    }

    /**
     * 检查游戏是否结束
     */
    private void checkGameOver() {
        int blackCount = 0, whiteCount = 0;

        for (Chess bean : lChess) {
            if (Chess.WHITE == bean.getType()) {
                whiteCount++;
            } else if (Chess.BLACK == bean.getType()) {
                blackCount++;
            }
        }

        int chessType = -100;
        if (whiteCount <= 1) {
            isGameOver = true;
            chessType = Chess.BLACK;
        } else if (blackCount <= 1) {
            isGameOver = true;
            chessType = Chess.WHITE;
        }

        if (isGameOver && null != listener) {
            listener.onGameOver(chessType);
        }
    }

    /**
     * 获取指定坐标的棋子
     *
     * @param x
     * @param y
     * @return
     */
    private Chess getChess(int x, int y) {
        for (Chess bean : lChess) {
            if (x == bean.getX() && y == bean.getY()) {
                return bean;
            }
        }
        return null;
    }

    /*
     * 重置棋盘,重新开始游戏
     */
    public void reStart() {
        isWhite = true;
        isGameOver = false;
        initChess();
        invalidate();
    }

    private static final String INSTANCE = "instance";// 存默认的instance
    private static final String INSTANCE_GAME_OVER = "instance_gameOver";
    private static final String INSTANCE_ARRAY = "instance_array";
    private static final String INSTANCE_ISWHITE = "instance_isWhite";

    /**
     * 必须给调用这个组件的xml中写上id，否则状态保存不会生效
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isGameOver);
        bundle.putSerializable(INSTANCE_ARRAY, lChess);
        bundle.putBoolean(INSTANCE_ISWHITE, isWhite);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            lChess = (ArrayList<Chess>) bundle.getSerializable(INSTANCE_ARRAY);
            isGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            isWhite = bundle.getBoolean(INSTANCE_ISWHITE);
            // 默认的Parcelable
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 游戏结束监听事件
     *
     * @Author hyj
     * @Date 2016-4-2 上午10:03:33
     */
    public interface OnGameOverListener {
        /**
         * 游戏结束
         *
         * @param chessType Chess:BLACK、WHITE，哪一方获胜； -100系统异常
         */
        public void onGameOver(int chessType);
    }
}
