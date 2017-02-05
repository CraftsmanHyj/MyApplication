package com.hyj.lib.lock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyj.lib.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * 数字解锁界面
 * 如果旋转屏幕要求状态保存，则必须在XML文件中定义ID，否则无法保存
 * Created by hyj on 2016/8/1.
 */
public class LockNumberView extends ViewGroup {
    private final String[] pwdShow = new String[]{"ㄛ", "ㄜ", "ㄞ", "ㄓ", "ㄒ", "ㄌ", "ㄢ", "ㄝ", "ㄘ", "ㄊ"};
    private final String BTN_CLEAR = "C";//清除所有密码
    private final String BTN_DEL = "←";//删除一个密码

    private final int MSG_COMPLETE = 0X0000100;//密码输入完成判断
    private final int MSG_PWDERROR = 0X0000101;//密码错误执行操作

    private final float TEXT_SIZE_SCALE = 1 / 4.0f;//将字体大小同按钮大小成比例缩放
    private final float PWD_WIDTH_SCALE = 9 / 10.0f;//密码显示界面所占宽度比例
    private final int PWD_COUNT = 6;//输入pwd的位数
    private int pwdNumber = 0;//当前输入密码个数

    private boolean isError = false;//密码输入错误
    private int sparkTimes = 0;//密码错误闪烁的次数

    private int viewWidth;//整个密码界面宽度
    private int dividerWidth = 15;//间距宽度

    private ArrayList<String> lPwdValue;//密码值
    private ArrayList<TextView> lPwdView;//存放密码
    private ArrayList<String> lBtValue;//存放按钮显示的值
    private ArrayList<TextView> lBtView;//按钮按键

    //////////以下是实现非必须变量//////////
    private AudioManager audioManager;//铃声模式
    private SoundPool soundPool;//音效播放
    private Vibrator vibrator;//震动

    private boolean hasTrack = true;//绘制线条是否可见
    private boolean hasShake = true;//按下是否震动
    private boolean hasVoice = true;//是否有声音

    private OnCompleteListener completeListener;

    /**
     * 绘制轨迹是否可见
     *
     * @param track
     */
    public void setHasTrack(boolean track) {
        this.hasTrack = track;
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
     * @param hasVoice
     */
    public void setHasVoice(boolean hasVoice) {
        this.hasVoice = hasVoice;
    }

    /**
     * 设置密码完成事件
     *
     * @param completeListener
     */
    public void setOnCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    /**
     * 用于延迟判断输入密码位数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COMPLETE:
                    String pwd = (String) msg.obj;
                    if (null != completeListener) {
                        completeListener.onComplete(pwd);
                    }
                    break;

                case MSG_PWDERROR:
                    error();
                    break;
            }
        }
    };

    public LockNumberView(Context context) {
        this(context, null);
    }

    public LockNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myInit(context, attrs);
    }

    private void myInit(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initDatas();
        initAttrDatas();
        initBtValues();
    }

    /**
     * 初始化自定义xml属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LockNumber);
        hasTrack = ta.getBoolean(R.styleable.LockNumber_hasTrack, hasTrack);
        hasVoice = ta.getBoolean(R.styleable.LockNumber_hasSound, hasVoice);
        hasShake = ta.getBoolean(R.styleable.LockNumber_hasShake, hasShake);
        ta.recycle();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        lPwdValue = new ArrayList<String>();
        lPwdView = new ArrayList<TextView>();
        lBtValue = new ArrayList<String>();
        lBtView = new ArrayList<TextView>();
    }

    /**
     * 初始化按钮的值
     */
    private void initBtValues() {
        lBtValue.clear();
        lBtValue.addAll(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        Collections.sort(lBtValue, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
        lBtValue.add(lBtValue.size() - 1, BTN_CLEAR);
        lBtValue.add(BTN_DEL);
    }

    /**
     * 初始化属性数据
     */
    private void initAttrDatas() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 5);//总共有12个按钮，发声12次
        //把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
        soundPool.load(getContext(), R.raw.lock, 1);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取控件宽高
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        //根据不同模式去获取精确值
        w = resolveSize(w, widthMeasureSpec);
        h = resolveSize(h, heightMeasureSpec);

        //获取宽高中最小的值，重新设置当前View尺寸
        viewWidth = Math.min(w, h);
        setMeasuredDimension(viewWidth, viewWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        /**
         * 密码显示框设置
         * 密码框宽度进行缩小：viewWidth * 9 / 10
         * 6个密码框中间有7个等间距的分割线
         */
        int pwdWidth = (int) ((viewWidth * PWD_WIDTH_SCALE - dividerWidth * (PWD_COUNT + 1)) / PWD_COUNT);
        int buttonWidth = (viewWidth - dividerWidth * (5 + 1)) / 5;
        float textSize = pwdWidth * TEXT_SIZE_SCALE;//字体大小按宽高比例缩小
        lPwdView.clear();//清除之前存放的数据
        for (int j = 0; j < PWD_COUNT; j++) {
            TextView tv = getTextView(pwdWidth, textSize);
            lPwdView.add(tv);

            if (j < lPwdValue.size()) {//输入密码状态恢复
                String pwd = lPwdValue.get(j);
                if (!TextUtils.isEmpty(pwd)) {
                    tv.setText(pwd);
                }
            }

            //因为显示框有缩小，所有需要加上缩小的部分：(viewWidth * 1 / 10 * 1 / 2);
            int cl = (int) (dividerWidth * (j + 1) + pwdWidth * j + (viewWidth * ((1 - PWD_WIDTH_SCALE) * 1 / 2)));
            int cr = cl + pwdWidth;
            int ct = dividerWidth + (buttonWidth - pwdWidth) / 2;//因为密码框比按钮小，需要计算垂直中间点
            int cb = ct + pwdWidth;
            tv.layout(cl, ct, cr, cb);
        }

        /**
         * 密码按钮设置：计算宽度的时候按5行5列计算
         *  第一行用于放密码显示框
         *  第一列、最后一列不需要显示
         */
        buttonWidth = (viewWidth - dividerWidth * (5 + 1)) / 5;
        textSize = buttonWidth * TEXT_SIZE_SCALE;//字体大小按宽高比例缩小
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 4; j++) {
                TextView tv = getTextView(buttonWidth, textSize);
                int cl = dividerWidth * (j + 1) + buttonWidth * j;
                int cr = cl + buttonWidth;
                int ct = dividerWidth * (i + 1) + buttonWidth * i;
                int cb = ct + buttonWidth;
                tv.layout(cl, ct, cr, cb);

                int index = (i - 1) * 3 + (j - 1);
                tv.setText(lBtValue.get(index));
                lBtView.add(tv);

                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonCLickDeal(v);
                    }
                });
            }
        }
    }

    /**
     * 按钮点击事件处理
     */
    private void buttonCLickDeal(View v) {
        //若果当前在执行错误动画，则忽略所有点击操作
        if (isError) {
            return;
        }

        if (hasVoice) {
            playMusic();
        }

        if (hasShake) {
            vibrator.vibrate(30);
        }

        String pwd = ((TextView) v).getText().toString();
        if (BTN_DEL.equals(pwd)) {
            delete();
        } else if (BTN_CLEAR.equals(pwd)) {
            resert();
        } else {
            setPwd(pwd);
        }
    }

    /**
     * 获取一个按钮
     *
     * @param width
     * @return
     */
    private TextView getTextView(int width, float textSize) {
        TextView tv = new TextView(getContext());
        tv.setTextSize(textSize);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //TODO 只能达到水平居中，无法使其垂直居中，使用这种方法间接达到效果，但是不好
        tv.setPadding(0, (int) (width * TEXT_SIZE_SCALE * 1 / 2.0f), 0, 0);

        if (!hasTrack) {
            tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.item_normal));
        } else {
            tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.item_press_bg));
        }

        addView(tv);
        return tv;
    }

    /**
     * 设置显示的密码
     *
     * @param pwd
     */
    private void setPwd(String pwd) {
        for (TextView tv : lPwdView) {
            String str = tv.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwdNumber++;
                lPwdValue.add(pwd);
                int index = (int) (Math.random() * pwdShow.length);
                tv.setText(pwdShow[index]);
                tv.setTag(pwd);

                if (PWD_COUNT == pwdNumber) {
                    StringBuilder sb = new StringBuilder();
                    for (String value : lPwdValue) {
                        sb.append(File.separator);
                        sb.append(value);
                    }

                    Message msg = handler.obtainMessage();
                    msg.what = MSG_COMPLETE;
                    msg.obj = sb.deleteCharAt(0).toString();
                    handler.sendMessageDelayed(msg, 100);
                }

                break;
            }
        }
    }

    /**
     * 重置密码
     */
    public void resert() {
        for (TextView tv : lPwdView) {
            pwdNumber = 0;
            lPwdValue.clear();
            tv.setText("");
            tv.setTag("");
        }

        initBtValues();
        for (int i = 0; i < lBtView.size(); i++) {
            TextView tv = lBtView.get(i);
            tv.setText(lBtValue.get(i));
        }
    }

    /**
     * 删除显示的密码
     */
    private void delete() {
        for (int i = lPwdView.size() - 1; i >= 0; i--) {
            TextView tv = lPwdView.get(i);
            String str = tv.getText().toString().trim();
            if (!TextUtils.isEmpty(str)) {
                pwdNumber--;
                lPwdValue.remove(i);
                tv.setText("");
                tv.setTag("");
                break;
            }
        }
    }

    /**
     * 密码输入错误：让密码闪烁
     */
    public void error() {
        isError = true;

        if (sparkTimes > 5) {   //闪烁5次提示
            isError = false;
            sparkTimes = 0;
            handler.removeMessages(MSG_PWDERROR);
            resert();
            return;
        }

        for (TextView tv : lPwdView) {
            int color = sparkTimes % 2 == 0 ? Color.RED : Color.BLACK;
            tv.setText((String) tv.getTag());
            tv.setTextColor(color);
        }

        sparkTimes++;
        handler.sendEmptyMessageDelayed(MSG_PWDERROR, 200);
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
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private final String INSTANCE = "instance";// 存默认的instance
    private final String INPUTPWD = "pwd";//存储已经输入了的密码数据
    private final String PWDNUMBER = "pwdNumber";//当前输入密码个数

    /**
     * 必须给调用这个组件的xml中写上id，否则状态保存不会生效
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putStringArrayList(INPUTPWD, lPwdValue);
        bundle.putInt(PWDNUMBER, pwdNumber);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            lPwdValue = bundle.getStringArrayList(INPUTPWD);
            pwdNumber = bundle.getInt(PWDNUMBER);
            // 默认的Parcelable
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
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