package com.hyj.demo.startmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.startmenu.ArcMenu.onMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用普通动画属性实现卫星菜单
 * 
 * @author async
 * 
 */
public class StartMenu2 extends BaseActivity {

	private ListView mListView;
	private List<String> mDatas;
	private ArrayAdapter<String> adapter;

	private ArcMenu startMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startmenu2);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
		initListener();
	}

	private void initView() {
		startMenu = (ArcMenu) findViewById(R.id.start2Menu);

		mListView = (ListView) findViewById(R.id.start2LvMain);
		mDatas = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mDatas);
		mListView.setAdapter(adapter);
	}

	private void initData() {
		for (int i = 'A'; i < 'Z'; i++) {
			mDatas.add((char) i + "");
		}

		adapter.notifyDataSetChanged();
	}

	private void initListener() {
		startMenu.setOnMenuItemClickListener(new onMenuItemClickListener() {

			@Override
			public void onClick(View view, int position) {
				Toast.makeText(StartMenu2.this,
						view.getTag().toString() + "　" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
