package com.hyj.demo.tools.adapter;

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
public abstract class CommonAdapter<T> extends BaseAdapter {
    /**
     * 上下文对象
     */
    protected Context context;
    /**
     * 单Item的时候，Item xml的ID
     */
    protected int layoutItemID;
    /**
     * Item填充数据
     */
    protected List<T> lDatas;

    /**
     * 多Item时构造函数
     *
     * @param context 山下文
     * @param lDatas  Item数据集
     */
    public CommonAdapter(Context context, List<T> lDatas) {
        this.context = context;
        this.lDatas = lDatas;
    }

    /**
     * 单Item Type时构造函数
     *
     * @param context      上下文
     * @param lDatas       Item数据集
     * @param layoutItemID Item xml布局文件ID
     */
    public CommonAdapter(Context context, List<T> lDatas, int layoutItemID) {
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

    /**
     * 获取Item布局文件的ID
     *
     * @param position
     * @return
     */
    public int getLayoutId(int position) {
        return layoutItemID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemID = getLayoutId(position);
        ViewHolder holder = ViewHolder.getInstance(context, convertView, parent, itemID, position);

        getItemView(holder, getItem(position));

        return holder.getConvertView();
    }

    /**
     * 给每个Item设置数据
     *
     * @param holder holder句柄对象
     * @param item   item每一项数据值
     */
    public abstract void getItemView(ViewHolder holder, T item);
}
