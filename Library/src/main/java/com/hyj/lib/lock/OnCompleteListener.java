package com.hyj.lib.lock;

/**
 * 解锁密码输入完成
 * 轨迹绘画完成事件
 *
 * @author: hyj
 */
public interface OnCompleteListener {
    /**
     * 密码输入完成时调用
     *
     * @param password
     */
    public void onComplete(String password);
}