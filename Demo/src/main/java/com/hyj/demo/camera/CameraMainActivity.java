package com.hyj.demo.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.Constants;
import com.hyj.demo.R;
import com.hyj.demo.tools.BitmapUtils;
import com.hyj.demo.tools.FileUtils;

import java.io.File;

public class CameraMainActivity extends BaseActivity implements OnClickListener {
    private static final int REQ_SLT = 1;// 返回的为一个缩略图
    private static final int REQ_YT = 2;// 返回一个原图路径

    public static final String Path = File.separator + Constants.DIR_TEMP + File.separator + "temp.jpg";

    private String filePath;
    private ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initData() {
        filePath = FileUtils.getAppFile(this, Path).getAbsolutePath();
    }

    private void initView() {
        imgPreview = (ImageView) findViewById(R.id.cameraIvPreviewe);
    }

    private void initListener() {
        findViewById(R.id.cameraBtSlt).setOnClickListener(this);
        findViewById(R.id.cameraBtYt).setOnClickListener(this);
        findViewById(R.id.cameraBtCustom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.cameraBtSlt:
                // 这样获取的是相片的缩略图
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_SLT);
                break;

            case R.id.cameraBtYt:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 指定拍完照片所保存的路径
                Uri photoUri = Uri.fromFile(new File(filePath));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQ_YT);
                break;

            case R.id.cameraBtCustom:
                intent.setClass(CameraMainActivity.this, CamaraCustomActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case REQ_SLT:
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                imgPreview.setImageBitmap(bitmap);
                break;

            case REQ_YT:
                // 读取文件
                bitmap = BitmapUtils.getBitmapFromPath(filePath);
                imgPreview.setImageBitmap(bitmap);
                break;
        }
    }
}
