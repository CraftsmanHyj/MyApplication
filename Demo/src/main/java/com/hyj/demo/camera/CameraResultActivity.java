package com.hyj.demo.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;

import java.io.FileInputStream;

public class CameraResultActivity extends BaseActivity {
	public static final String PIC_PATH = "picturePath";

	private ImageView imgResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_result);

		myInit();
	}

	private void myInit() {
		imgResult = (ImageView) findViewById(R.id.cameraIvResult);

		String path = getIntent().getStringExtra(PIC_PATH);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			imgResult.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
