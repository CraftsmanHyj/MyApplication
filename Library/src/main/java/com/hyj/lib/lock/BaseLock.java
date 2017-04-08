package com.hyj.lib.lock;

/**
 * 数字解锁，九宫格解锁统一对外暴露的方法
 * Created by hyj on 2017/4/6.
 */

public interface BaseLock {

    /**
     * 绘制轨迹是否可见
     *
     * @param track
     */
    public void setHasTrack(boolean track);

    /**
     * 是否支持震动
     *
     * @param hasShake
     */
    public void setHasShake(boolean hasShake);

    /**
     * 是否有声音
     *
     * @param hasVoice
     */
    public void setHasVoice(boolean hasVoice);

    /**
     * 设置绘制完成事件
     *
     * @param completeListener
     */
    public void setOnCompleteListener(OnCompleteListener completeListener);

    /**
     * 重置输入状态
     */
    public void resert();

    /**
     * 密码输入错误，显示错误状态
     */
    public void error();

    /**
     * 延迟清除已输入的密码
     */
    public void clearPassword();
}