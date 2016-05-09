package com.hyj.lib.circleimageview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.hyj.lib.R;
import com.hyj.lib.tools.DialogUtils;
import com.hyj.lib.tools.FileUtils;
import com.hyj.lib.tools.LogUtils;

import java.io.File;

/**
 * 圆形头像上传功能
 */
public class CircleImageActivity extends Activity {
    private final int REQUESTCODE_PIC = 0X00000001;//相册
    private final int REQUESTCODE_CAM = 0X00000002;//相机
    private final int REQUESTCODE_CUT = 0X00000003;//裁剪

    private Bitmap headBitmap;//剪切好的图片文件

    private CircleImageView civHead;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleimg_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        civHead = (CircleImageView) findViewById(R.id.circleimgHead);
        civHead.setOutCircleWidth(5);
        civHead.setOutCircleColor(Color.RED);
    }

    private void initData() {
    }

    private void initListener() {
        civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog dialog = new ActionSheetDialog(CircleImageActivity.this).builder();
                dialog.addSheetItem("拍照", ActionSheetDialog.SheetItemColor.BULE, new ActionSheetDialog.onSheetItemClickListener() {
                    @Override
                    public void onClick(int witch) {
                        openCamera();
                    }
                });

                dialog.addSheetItem("打开相册", ActionSheetDialog.SheetItemColor.RED, new ActionSheetDialog.onSheetItemClickListener() {
                    @Override
                    public void onClick(int witch) {
                        openPic();
                    }
                });

                dialog.show();
            }
        });
    }

    private void openCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            file = new File(outDir, "circleImageHeader.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, REQUESTCODE_CAM);
        } else {
            DialogUtils.showToastShort(this, "请插入SD卡");
        }

//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//            DialogUtils.showToastShort(this, "请插入SD卡");
//            return;
//        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = FileUtils.getAppFile(this, "circleImageHeader.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//
//        startActivityForResult(intent, REQUESTCODE_CAM);
    }

    private void openPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUESTCODE_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_CAM:
                startPhotoZoom(Uri.fromFile(file));
                break;

            case REQUESTCODE_PIC:
                if (null == data || null == data.getData()) {
                    return;
                }

                startPhotoZoom(data.getData());
                break;

            case REQUESTCODE_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 打开系统图片的裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUT);
    }

    public void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (null == extras) {
            return;
        }

        headBitmap = extras.getParcelable("data");
        if (null == headBitmap) {
            return;
        }
        //这里可以添加文件上传代码，将头像上传到服务器
        civHead.setImageBitmap(headBitmap);
    }
}
