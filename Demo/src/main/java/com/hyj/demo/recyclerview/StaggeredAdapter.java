package com.hyj.demo.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.hyj.demo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by async on 2016/1/16.
 */
public class StaggeredAdapter extends RecyclerAdapter {

	private List<Integer> lHeights;

	public StaggeredAdapter(Context context, List<String> lDatas) {
		super(context, lDatas);

		lHeights = new ArrayList<Integer>();
		for (int i = 0; i < super.lDatas.size(); i++) {
			lHeights.add((int) (150 + Math.random() * 500));
		}
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);

		ViewGroup.LayoutParams lp = holder.tv.getLayoutParams();
		lp.height = lHeights.get(position);
		LogUtils.i("View高度：" + lHeights.get(position));
		holder.tv.setLayoutParams(lp);

		holder.tv.setText(lDatas.get(position));
	}
}
