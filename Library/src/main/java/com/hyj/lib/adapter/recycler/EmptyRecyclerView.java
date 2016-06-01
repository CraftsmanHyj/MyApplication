package com.hyj.lib.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
 * <p/>
 * Created by Administrator on 2016/5/25.
 */
public class EmptyRecyclerView extends RecyclerView {
    private View emptyView;

    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        this(context, null);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 检查View是否有数据
     */
    private void checkIfEmpty() {
        if (null != emptyView) {
            emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    /**
     * 设置View控件
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }


    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        checkIfEmpty();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }
}