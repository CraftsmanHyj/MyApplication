package com.hyj.demo.largeImage;

import android.os.Bundle;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageViewActivity extends BaseActivity {
	private LargeImageView mLargeImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.largeimage_main);

		mLargeImageView = (LargeImageView) findViewById(R.id.largetImageview);
		try {
			InputStream inputStream = getAssets().open("qm.jpg");
			mLargeImageView.setInputStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
