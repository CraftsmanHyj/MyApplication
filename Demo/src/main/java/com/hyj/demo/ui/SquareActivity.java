package com.hyj.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.adapter.CommonAdapter;
import com.hyj.lib.adapter.ViewHolder;

import java.util.Arrays;
import java.util.List;

public class SquareActivity extends BaseActivity {
	private String[] strs = { "imtem1", "imtem2", "imtem3", "imtem4", "imtem5",
			"imtem6", "imtem7", "imtem8", "imtem9" };

	private List<String> lData;
	private GridView gvSquare;
	private SquareAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_main);

		myInit();
	}

	private void myInit() {
		lData = Arrays.asList(strs);
		gvSquare = (GridView) findViewById(R.id.squareGridView);
		adapter = new SquareAdapter(this, lData, R.layout.square_item);
		gvSquare.setAdapter(adapter);
	}

	private class SquareAdapter extends CommonAdapter<String> {

		public SquareAdapter(Context context, List<String> lDatas,
				int layoutItemID) {
			super(context, lDatas, layoutItemID);
		}

		@Override
		public void getItemView(ViewHolder holder, String item) {
			holder.setText(R.id.squareTvValue, item);
		}
	}
}
