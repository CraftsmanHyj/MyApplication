package com.hyj.lib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 通用ViewHolder
 *
 * @author async
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> saView;

    private int position;//当前item的索引位置
    private Context context;
    private View convertView;

    /**
     * 获取当前Item的索引位置
     *
     * @return
     */
    public int getItemPosition() {
        return position;
    }

    /**
     * 更新当前holder的position索引
     *
     * @param position
     */
    public void setItemPosition(int position) {
        this.position = position;
    }

    /**
     * 获取实例化的ConvertView
     *
     * @return
     */
    public View getConvertView() {
        return convertView;
    }

    private ViewHolder(Context context, View convertView, int position) {
        super(convertView);
        this.context = context;
        this.convertView = convertView;
        this.position = position;
        this.saView = new SparseArray<View>();
        this.convertView.setTag(this);
    }

    /**
     * RecyclerView用来获取holder实例
     *
     * @param context
     * @param parent
     * @param layoutID
     * @return
     */
    public static ViewHolder getInstance(Context context, ViewGroup parent, int layoutID) {
        return getInstance(context, null, parent, layoutID, -1);
    }

    /**
     * 获取ViewHolder的实例
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutID
     * @param position
     * @return
     */
    public static ViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layoutID, int position) {
        if (null == convertView) {
            View convert = LayoutInflater.from(context).inflate(layoutID, parent, false);
            return new ViewHolder(context, convert, position);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        // convertView可以复用但是position是变化的,在此更新
        holder.position = position;
        return holder;
    }

    /**
     * 通过ViewID获取组件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = saView.get(viewId);
        if (null == view) {
            view = convertView.findViewById(viewId);
            saView.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为textview设置显示文字
     *
     * @param viewId 组件ID
     * @param strId  要显示的字符串的整形值
     */
    public void setText(int viewId, int strId) {
        setText(viewId, convertView.getContext().getResources().getString(strId));
    }

    /**
     * 为textview设置显示文字
     *
     * @param viewId 组件ID
     * @param str    要显示的字符串
     */
    public void setText(int viewId, String str) {
        TextView tv = getView(viewId);
        tv.setText(str);
    }

    /**
     * 设置TextView的颜色
     *
     * @param viewId
     * @param textColor
     */
    public void setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
    }

    /**
     * 设置TextView的颜色，根据资源文件中的resID值
     *
     * @param viewId
     * @param textColorRes
     */
    public void setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
    }

    /**
     * 给TextView添加超链接
     *
     * @param viewId
     */
    public void addLinks(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
    }

    /**
     * 设置TextView的字体
     *
     * @param typeface
     * @param viewIds
     */
    public void setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

    /**
     * 设置组件是否可用
     *
     * @param viewId  组件ID
     * @param enabled 是否可用
     */
    public void setEnabled(int viewId, boolean enabled) {
        getView(viewId).setEnabled(enabled);
    }

    /**
     * 设置控件是否可见
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
    }

    /**
     * 给View设置一个默认的Tag
     *
     * @param viewId
     * @param tag
     */
    public void setTag(int viewId, Object tag) {
        getView(viewId).setTag(tag);
    }

    /**
     * 给View设置指定key的Tag
     *
     * @param viewId
     * @param key
     * @param tag
     */
    public void setTag(int viewId, int key, Object tag) {
        getView(viewId).setTag(key, tag);
    }

    /**
     * 根据id设置ImageView的图片
     *
     * @param viewId
     * @param resId
     */
    public void setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
    }

    /**
     * 根据bitmap设置ImageView的图片
     *
     * @param viewId
     * @param bitmap
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    /**
     * 根据drawable设置ImageView的图片
     *
     * @param viewId
     * @param drawable
     */
    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
    }

    /**
     * 设置控件背景颜色
     *
     * @param viewId
     * @param color
     */
    public void setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
    }

    /**
     * 设置控件背景，根据资源文件设置
     *
     * @param viewId
     * @param resId
     */
    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    /**
     * 设置控件透明度
     *
     * @param viewId
     * @param value
     */
    public void setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
    }

    /**
     * 设置progressBar进度条的值
     *
     * @param viewId   组件ID
     * @param progress 当前进度值
     */
    public void setProgress(int viewId, int progress) {
        ProgressBar pb = getView(viewId);
        pb.setProgress(progress);
    }

    /**
     * 设置ProgressBar的进度值，同时指定最大值
     *
     * @param viewId
     * @param progress
     * @param max
     */
    public void setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
    }

    /**
     * 设置进度条最大值
     *
     * @param viewId
     * @param max
     */
    public void setProgressMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
    }

    /**
     * 设置RatingBar的进度值
     *
     * @param viewId
     * @param rating
     */
    public void setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
    }

    /**
     * 设置RatingBar的进度值、最大值
     *
     * @param viewId
     * @param rating
     * @param max
     */
    public void setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
    }

    /**
     * 设置复选款是否选定
     *
     * @param viewId
     * @param checked
     */
    public void setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
    }

    /**
     * 关于事件的
     */
    /**
     * 给View设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
    }

    /**
     * 给View设置触摸事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        getView(viewId).setOnTouchListener(listener);
    }

    /**
     * 给View设置长按事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
    }
}
