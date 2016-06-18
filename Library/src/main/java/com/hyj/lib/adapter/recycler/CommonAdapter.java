package com.hyj.lib.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyj.lib.adapter.ViewHolder;

import java.util.List;

/**
 * Created by hyj on 2016/5/25.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context context;
    protected int layoutItemID;
    protected List<T> lDatas;

    private OnItemClickListener mOnItemClickListener;

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CommonAdapter(Context context, List<T> lDatas) {
        this.context = context;
        this.lDatas = lDatas;
    }

    public CommonAdapter(Context context, List<T> lDatas, int layoutId) {
        this.context = context;
        layoutItemID = layoutId;
        this.lDatas = lDatas;
    }

    @Override
    public int getItemCount() {
        return lDatas.size();
    }

    /**
     * 获取指定索引位置的值
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return lDatas.get(position);
    }

    /**
     * 获取Item布局文件的ID
     *
     * @param viewType
     * @return
     */
    public int getLayoutId(int viewType) {
        //单个布局文件的时候默认返回传入的layoutId
        return layoutItemID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemID = getLayoutId(viewType);
        ViewHolder viewHolder = ViewHolder.getInstance(context, parent, itemID);

        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItemPosition(position);
        getItemView(holder, lDatas.get(position));
    }

    public abstract void getItemView(ViewHolder holder, T t);


    //TODO 这里有疑问是干嘛用的
    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getPosition();//getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, lDatas.get(position), position);
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnItemClickListener) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, lDatas.get(position), position);
                }
                return false;
            }
        });
    }

    /**
     * RcyclerView点击事件
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        public void onItemClick(ViewGroup parent, View view, T t, int position);

        public boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
    }
}
