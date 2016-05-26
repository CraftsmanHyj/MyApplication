package com.hyj.demo.mainview.qq5_0;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;

public class SlidingActivity extends BaseActivity {

	private SlidingMenu mSlidingMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qq_main);

		myInit();
	}

	private void myInit() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.qqSlidingMenu);
	}

	public void toggleMenu(View view) {
		mSlidingMenu.toggleMenu();
	}
}
