package com.hyj.lib.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyj.lib.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ListView拉、下拉刷新
 *
 * @author hyj
 * @date 2014-8-9 上午12:57:54
 */
public class ListViewPullUp extends ListView {
    private static final int STATUS_PULL_TO_REFRESH = 0;// 状态：正在下拉，处于手指下滑状态     刷新完成 /初始状态→ 进入 下拉刷新
    private static final int STATUS_RELEASE_TO_REFRESH = 1; //状态：正在下拉，达到可以释放刷新的状态    下啦刷新 → 松开刷新
    private static final int STATUS_REFRESHING = 2;//状态：正在刷新
    private static final int STATUS_COMPLETE = 3;//状态：刷新完成

    private View header;
    private ImageView headerArrow;
    private ProgressBar headerProgressBar;
    private TextView headerTitle;
    private TextView headerLastUpdated;

    private View footer;
    private ImageView footerArrow;
    private ProgressBar footerProgressBar;
    private TextView footerTitle;
    private TextView footerLastUpdated;

    private int headerWidth;
    private int headerHeight;

    private Animation ra02180;
    private Animation ra18020;

    private static final float RATIO = 3;//下拉、上拉阻尼缩放系数

    private static boolean isBack = false;//标记箭头是否可以方向反转
    private boolean isRecorded;//标记是否进入下拉刷新流程

    private float startY;//手指按下时Y坐标值
    private float firstTempY = 0;
    private float secondTempY = 0;

    private int refreshStatus;// 当前刷新状态
    private boolean pullType = false;//当前拖动刷新类型    true：下拉；false：上拉；

    private boolean pullDownRefereshEnable;// 下拉刷新是否可用
    private OnRefreshListener pullDownListener;//下拉刷新监听事件

    private boolean pullUpRefreshEnable;//上拉刷新是否可用
    private OnRefreshListener pullUpListener;//上拉刷新监听事件
    private boolean isFooterVisible = false;//Footer当前是否可见

    /**
     * 设置上拉刷新监听事件
     *
     * @param listener
     */
    public void setOnPullUpRefreshListener(OnRefreshListener listener) {
        this.pullUpListener = listener;
        this.pullUpRefreshEnable = true;
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnPullDownRefreshListener(OnRefreshListener listener) {
        this.pullDownListener = listener;
        this.pullDownRefereshEnable = true;//有设置监听器可以进行刷新
    }

    public ListViewPullUp(Context context) {
        this(context, null);
    }

    public ListViewPullUp(Context context, AttributeSet attrs) {
        super(context, attrs);

        myInit(context);
    }

    /**
     * 初始化listview
     *
     * @param context
     */
    private void myInit(Context context) {
        initView(context);
        initData();
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.listviewpullup_header, null);
        headerArrow = (ImageView) header.findViewById(R.id.arrow);
        headerProgressBar = (ProgressBar) header.findViewById(R.id.progerssbar);
        headerTitle = (TextView) header.findViewById(R.id.title);
        headerLastUpdated = (TextView) header.findViewById(R.id.updated);
        headerArrow.setMinimumWidth(70);
        headerArrow.setMaxHeight(50);

        footer = inflater.inflate(R.layout.listviewpullup_header, null);
        footerArrow = (ImageView) footer.findViewById(R.id.arrow);
        footerProgressBar = (ProgressBar) footer.findViewById(R.id.progerssbar);
        footerTitle = (TextView) footer.findViewById(R.id.title);
        footerLastUpdated = (TextView) footer.findViewById(R.id.updated);
        footerArrow.setRotation(180);
        footerArrow.setMinimumWidth(70);
        footerArrow.setMaxHeight(50);

        measureView(header);//测量View所占的实际宽、高
        headerWidth = header.getMeasuredWidth();
        headerHeight = header.getMeasuredHeight();

        header.setPadding(0, -1 * headerHeight, 0, 0);// 设置与界面上边距的距离
        header.invalidate();// 控件重绘

        footer.setPadding(0, -1 * headerHeight, 0, 0);// 设置与界面上边距的距离
        footer.invalidate();// 控件重绘

        addHeaderView(header);
        addFooterView(footer);
    }

    private void initData() {
        pullDownRefereshEnable = false;
        refreshStatus = STATUS_COMPLETE;

        ra02180 = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra02180.setDuration(150);
        ra02180.setFillAfter(true);
        ra02180.setInterpolator(new LinearInterpolator());

        ra18020 = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra18020.setDuration(150);
        ra18020.setFillAfter(true);
        ra18020.setInterpolator(new LinearInterpolator());
    }

    /**
     * 通知父布局view所占的宽、高
     *
     * @param v
     */
    private void measureView(View v) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int measureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int measureHeight;
        if (lp.height > 0) {
            //根据提供的大小值和模式创建一个测量值
            measureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        } else {
            measureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.UNSPECIFIED);
        }
        v.measure(measureWidth, measureHeight);
    }

    /**
     * 中央控制台 几乎所有的拉动事件皆由此驱动
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int lastVisibleItemIndex = getLastVisiblePosition() - 1;// 因为加有一尾视图，所以这里要减一
        int totalCounts = getCount() - 2;// 因为给listview加了一头一尾）视图所以这里要减二

        if (pullDownRefereshEnable || pullUpRefreshEnable) {//有设置监听器，则可以进行刷新
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstTempY = ev.getY();
                    isRecorded = false;

                    if (!isRecorded && (getFirstVisiblePosition() == 0
                            || (getLastVisiblePosition() == getCount() - 1
                            || getLastVisiblePosition() == getCount() - 2))) {
                        startY = ev.getY();
                        isRecorded = true;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    pullType = (ev.getY() > startY) ? true : false;

                    if (pullDownRefereshEnable && pullType && getFirstVisiblePosition() == 0) {
                        firstTempY = secondTempY;
                        secondTempY = ev.getY();
                        if (!isRecorded) {
                            startY = secondTempY;
                            isRecorded = true;
                        }

                        if (refreshStatus != STATUS_REFRESHING) {
                            if (refreshStatus == STATUS_COMPLETE) {
                                if (secondTempY - startY > 0) {     //初始状态→ 进入 下拉刷新
                                    refreshStatus = STATUS_PULL_TO_REFRESH;
                                    onHeaderStateChange();
                                }
                            }

                            if (refreshStatus == STATUS_PULL_TO_REFRESH) {      // 下拉刷新 → 松开刷新
                                if ((secondTempY - startY) / RATIO > headerHeight && secondTempY - firstTempY > 3) {
                                    refreshStatus = STATUS_RELEASE_TO_REFRESH;
                                    onHeaderStateChange();
                                } else if (secondTempY - startY <= -5) {    // 下啦刷新 → 回到 刷新完成
                                    refreshStatus = STATUS_COMPLETE;
                                    onHeaderStateChange();
                                }
                            }

                            if (refreshStatus == STATUS_RELEASE_TO_REFRESH) {
                                if (firstTempY - secondTempY > 5) {// 松开刷新 →回到下拉刷新
                                    refreshStatus = STATUS_PULL_TO_REFRESH;
                                    isBack = true;// 从松开刷新 → 回到的下拉刷新
                                    onHeaderStateChange();
                                } else if (secondTempY - startY <= -5) { // 松开刷新 → 回到 刷新完成
                                    refreshStatus = STATUS_COMPLETE;
                                    onHeaderStateChange();
                                }
                            }

                            if (refreshStatus == STATUS_PULL_TO_REFRESH || refreshStatus == STATUS_RELEASE_TO_REFRESH) {
                                header.setPadding(0, (int) ((secondTempY - startY) / RATIO - headerHeight), 0, 0);
                            }
                        }
                    } else if (pullUpRefreshEnable && !pullType && !isFooterVisible
                            && (getLastVisiblePosition() == getCount() - 1 || getLastVisiblePosition() == getCount() - 2)) {
                        //上拉刷新
                        firstTempY = secondTempY;
                        secondTempY = ev.getY();

                        if (!isRecorded) {
                            startY = secondTempY;
                            isRecorded = true;
                        }

                        if (refreshStatus != STATUS_REFRESHING) {// 不是正在刷新状态
                            if (refreshStatus == STATUS_COMPLETE) {
                                if (startY - secondTempY > 0) {  // 刷新完成/初始状态 → 进入 下拉刷新
                                    refreshStatus = STATUS_PULL_TO_REFRESH;
                                    onFooterStateChange();
                                }
                            }

                            if (refreshStatus == STATUS_PULL_TO_REFRESH) {
                                if ((startY - secondTempY) / RATIO > headerHeight && firstTempY - secondTempY >= 9) {
                                    // 上拉刷新 → 松开刷新
                                    refreshStatus = STATUS_RELEASE_TO_REFRESH;
                                    onFooterStateChange();
                                } else if (startY - secondTempY <= 0) { // 上拉刷新 → 回到 刷新完成
                                    refreshStatus = STATUS_COMPLETE;
                                    onFooterStateChange();
                                }
                            }

                            if (refreshStatus == STATUS_RELEASE_TO_REFRESH) {
                                if (firstTempY - secondTempY < -5) {// 从松开刷新 → 回到的上拉刷新
                                    refreshStatus = STATUS_PULL_TO_REFRESH;
                                    isBack = true;
                                    onFooterStateChange();
                                } else if (secondTempY - startY >= 0) {// 松开刷新 → 回到 刷新完成
                                    refreshStatus = STATUS_COMPLETE;
                                    onFooterStateChange();
                                }
                            }

                            if ((refreshStatus == STATUS_PULL_TO_REFRESH
                                    || refreshStatus == STATUS_RELEASE_TO_REFRESH) && secondTempY < startY) {
                                footer.setPadding(0, 0, 0, (int) ((startY - secondTempY) / RATIO - headerHeight));
                            }
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (refreshStatus != STATUS_REFRESHING) {
                        if (refreshStatus == STATUS_PULL_TO_REFRESH) {
                            refreshStatus = STATUS_COMPLETE;
                            if (pullType) {// 下拉
                                onHeaderStateChange();
                            } else if (!pullType) {// 上拉
                                onFooterStateChange();
                            }
                        }

                        if (refreshStatus == STATUS_RELEASE_TO_REFRESH) {
                            refreshStatus = STATUS_REFRESHING;
                            if (pullType) { // 下拉
                                onHeaderStateChange();
                                if (pullDownListener != null) {
                                    if (isFooterVisible) {
                                        hideFooter();
                                    }
                                    pullDownListener.onRefresh();
                                }
                            } else if (!pullType) {// 上拉
                                onFooterStateChange();
                                if (pullUpListener != null) {
                                    pullUpListener.onRefresh();
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更改尾视图显示状态
     */
    private void onHeaderStateChange() {
        headerProgressBar.setVisibility(View.GONE);
        headerArrow.setVisibility(View.VISIBLE);
        headerArrow.clearAnimation();// 清除上一次的动画

        switch (refreshStatus) {
            case STATUS_PULL_TO_REFRESH:
                headerTitle.setText("下拉刷新");
                if (isBack) {
                    headerArrow.startAnimation(ra02180);
                    isBack = false;
                }
                break;

            case STATUS_RELEASE_TO_REFRESH:
                headerTitle.setText("松开刷新");
                headerArrow.startAnimation(ra18020);
                break;

            case STATUS_REFRESHING:
                headerProgressBar.setVisibility(View.VISIBLE);
                headerArrow.setVisibility(View.GONE);

                headerTitle.setText("正在刷新");
                header.setPadding(0, 0, 0, 0);
                break;

            case STATUS_COMPLETE:
                headerTitle.setText("下拉刷新");
                header.setPadding(0, -1 * headerHeight, 0, 0);
                break;
        }
    }

    /**
     * 更改尾视图显示状态
     */
    private void onFooterStateChange() {
        footerProgressBar.setVisibility(View.GONE);
        footerArrow.setVisibility(View.VISIBLE);
        footerArrow.clearAnimation();//清除上次的动画

        switch (refreshStatus) {
            case STATUS_PULL_TO_REFRESH:
                footerTitle.setText("上拉刷新");
                if (isBack) {
                    footerArrow.startAnimation(ra02180);
                    isBack = false;
                }
                break;

            case STATUS_RELEASE_TO_REFRESH:
                footerTitle.setText("松开刷新");
                footerArrow.startAnimation(ra18020);
                break;

            case STATUS_REFRESHING:
                footerProgressBar.setVisibility(View.VISIBLE);
                footerArrow.setVisibility(View.GONE);

                footerTitle.setText("正在刷新");
                footer.setPadding(0, 0, 0, 0);
                break;

            case STATUS_COMPLETE:
                footerTitle.setText("上拉刷新");
                footer.setPadding(0, -1 * headerHeight, 0, 0);
                break;
        }
    }

    /**
     * 隐藏Footer
     */
    private void hideFooter() {
        isFooterVisible = false;

        footerArrow.setVisibility(View.VISIBLE);
        footerLastUpdated.setVisibility(View.VISIBLE);
        footerTitle.setText("");
        footer.setPadding(0, -1 * headerHeight, 0, 0);
    }

    /**
     * 显示Footer提示语
     * 此方法需要在onRefreshComplete()方法之后调用
     */
    public void showFooter() {
        isFooterVisible = true;

        footerArrow.setVisibility(View.GONE);
        footerLastUpdated.setVisibility(View.GONE);
        footerTitle.setText("已显示全部数据");
        footer.setPadding(0, 0, 0, 0);
    }

    /**
     * 处理刷新完成后事项
     */
    public void onRefreshComplete() {
        refreshStatus = STATUS_COMPLETE;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String hint = "最后刷新时间：" + sdf.format(new Date());

        if (pullType) {// 上拉
            onHeaderStateChange();
            headerLastUpdated.setText(hint);
        } else {// 下拉
            onFooterStateChange();
            footerLastUpdated.setText(hint);
        }
    }

    /**
     * 刷新监听事件
     */
    public interface OnRefreshListener {
        /**
         * 下拉刷新
         */
        public void onRefresh();
    }
}
