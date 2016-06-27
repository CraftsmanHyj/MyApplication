package com.hyj.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <pre>
 *     嵌套在ScrollView中显示不全冲突
 *     若要让ScrollView置顶,设置一下代码：
 * 		    sv = (ScrollView) findViewById(R.id.scrollview);
 * 		    sv.smoothScrollTo(0, 0);
 * </pre>
 * Created by hyj on 2016/6/27.
 */
public class GridViewShowAll extends GridView {
    public GridViewShowAll(Context context) {
        this(context, null);
    }

    public GridViewShowAll(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewShowAll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
