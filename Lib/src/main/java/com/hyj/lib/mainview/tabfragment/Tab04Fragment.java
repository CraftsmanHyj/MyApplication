package com.hyj.lib.mainview.tabfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyj.lib.BaseFragment;

public class Tab04Fragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		TextView tv = new TextView(getActivity());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(30);
		tv.setText("我");

		return tv;
	}
}
