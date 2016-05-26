package com.hyj.demo.listviewrefresh;

import android.content.Context;

import com.hyj.demo.R;
import com.hyj.demo.tools.adapter.CommonAdapter;
import com.hyj.demo.tools.adapter.ViewHolder;

import java.util.List;

public class ListViewAdapter extends CommonAdapter<Integer> {

	public ListViewAdapter(Context context, List<Integer> lDatas,
			int layoutItemID) {
		super(context, lDatas, layoutItemID);
	}

	@Override
	public void getItemView(ViewHolder holder, Integer item) {
		holder.setText(R.id.refreshTvName, "默认用户　" + item);
	}
}
