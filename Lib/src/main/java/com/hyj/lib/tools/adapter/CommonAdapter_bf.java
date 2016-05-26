package com.hyj.lib.tools.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 适配器模板
 *
 * @param <T>
 * @author async
 */
public abstract class CommonAdapter_bf<T> extends BaseAdapter {
    protected int layoutItemID;

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * Item填充数据
     */
    protected List<T> lDatas;

    public CommonAdapter_bf(Context context, List<T> lDatas, int layoutItemID) {
        super();
        this.context = context;
        this.lDatas = lDatas;
        this.layoutItemID = layoutItemID;
    }

    @Override
    public int getCount() {
        return lDatas.size();
    }

    @Override
    public T getItem(int position) {
        return lDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context, convertView,
                parent, layoutItemID, position);

        getViewItem(holder, getItem(position));

        return holder.getConvertView();
    }

    /**
     * 给每个Item设置数据
     *
     * @param holder holder句柄对象
     * @param item   item每一项数据值
     */
    public abstract void getViewItem(ViewHolder holder, T item);
}
