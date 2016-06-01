package com.hyj.demo.adapter;

import android.content.Context;

import com.hyj.demo.R;
import com.hyj.lib.adapter.CommonAdapter;
import com.hyj.lib.adapter.ViewHolder;

import java.util.List;

public class MyAdapter extends CommonAdapter<News> {

	public MyAdapter(Context context, List<News> lDatas, int layoutItemID) {
		super(context, lDatas, layoutItemID);
	}

	@Override
	public void getItemView(ViewHolder holder, News item) {
		holder.setText(R.id.adpItemTvTitle, item.getTitle());
		holder.setText(R.id.adpItemContent, item.getDesc());
		holder.setText(R.id.adpItemTime, item.getTime());
		holder.setText(R.id.adpItemTel, item.getPhone());
	}
}
