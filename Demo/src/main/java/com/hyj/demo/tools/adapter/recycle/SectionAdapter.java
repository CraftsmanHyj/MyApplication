package com.hyj.demo.tools.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.hyj.demo.tools.adapter.ViewHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hyj on 2016/5/26.
 */
public abstract class SectionAdapter<T> extends CommonAdapter<T> {
    private static final int TYPE_SECTION = 0;
    private LinkedHashMap<String, Integer> mSections;

    private final int DEFAULT_VIEWTYPE = -100;
    private final int DEFAULT_LAYOUTID = -101;

    private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };

    /**
     * 设置标题布局文件
     *
     * @return
     */
    public abstract int sectionHeaderLayoutId();

    /**
     * 标题布局文件中的textView的id
     *
     * @return
     */
    public abstract int sectionTitleTextViewId();

    /**
     * 获取标题显示文字
     *
     * @param t
     * @return
     */
    public abstract String getTitle(T t);

    /**
     * 多种Item布局的时候使用
     *
     * @param context
     * @param datas
     */
    public SectionAdapter(Context context, List<T> datas) {
        this(context, -1, datas);
    }

    /**
     * 单Item布局的时候使用
     *
     * @param context
     * @param layoutItemID
     * @param datas
     */
    public SectionAdapter(Context context, int layoutItemID, List<T> datas) {
        super(context, datas, layoutItemID);
        super.layoutItemID = layoutItemID;
        mSections = new LinkedHashMap<>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    @Override
    protected boolean isEnabled(int viewType) {
        if (viewType == TYPE_SECTION)
            return false;
        return super.isEnabled(viewType);
    }

    //TODO 当释放Adapter的时候注销观察者，怎么做？
//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        unregisterAdapterDataObserver(observer);
//    }

    /**
     * 标记标题所在位置？
     */
    public void findSections() {
        int nSections = 0;
        mSections.clear();

        for (int i = 0, len = lDatas.size(); i < len; i++) {
            String sectionName = getTitle(lDatas.get(i));

            if (!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mSections.size();
    }

    @Override
    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        //TODO 这里是因为版本变化所引起的吗
        return getIndexForPosition(viewHolder.getPosition());//getAdapterPosition();初始值
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            holder.setText(sectionTitleTextViewId(), getTitle(lDatas.get(position)));
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    /**
     * 获取viewholder真正位置
     *
     * @param position
     * @return
     */
    public int getIndexForPosition(int position) {
        int nSections = 0;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                nSections++;
            }
        }
        return position - nSections;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = getSectionItemViewType(position);
        if (DEFAULT_VIEWTYPE == viewType) {//初始化没有带入布局文件，说明是多种Item
            return mSections.values().contains(position) ? TYPE_SECTION : 1;
        } else if (DEFAULT_VIEWTYPE != viewType) {
            int positionVal = getIndexForPosition(position);
            return mSections.values().contains(position) ? TYPE_SECTION : getSectionItemViewType(positionVal);
        } else {
            throw new RuntimeException("layoutItemID or MultiItemTypeSupport must set one.");
        }
    }

    /**
     * 获取Item的类型
     *
     * @param position
     * @return
     */
    public int getSectionItemViewType(int position) {
        return DEFAULT_VIEWTYPE;
    }

    @Override
    public int getLayoutId(int itemType) {
        if (itemType == TYPE_SECTION) {//是否是标题
            return sectionHeaderLayoutId();
        } else {
            int itemLayout = getSectionLayoutId(itemType);
            if (DEFAULT_LAYOUTID == itemLayout) {//单布局Item
                return layoutItemID;
            } else if (DEFAULT_LAYOUTID != itemLayout) {//多布局Item
                return getSectionLayoutId(itemType);
            } else {
                throw new RuntimeException("layoutItemID or MultiItemTypeSupport must set one.");
            }
        }
    }

    /**
     * 获取Item的布局文件
     *
     * @param itemType
     * @return
     */
    public int getSectionLayoutId(int itemType) {
        return DEFAULT_LAYOUTID;
    }
}