package com.hyj.demo.circleimageview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.Constants;
import com.hyj.demo.R;
import com.hyj.lib.tools.BitmapUtils;
import com.hyj.lib.tools.FileUtils;
import com.hyj.lib.tools.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     圆形头像上传功能
 *     获取系统图片等文件存放路径
 *     File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
 * </pre>
 */
public class CircleImageActivity extends BaseActivity {
    private final int REQUESTCODE_PIC = 0X00000001;//相册
    private final int REQUESTCODE_CAM = 0X00000002;//相机
    private final int REQUESTCODE_CUT = 0X00000003;//裁剪

    private final String PHOTO_TEMP = File.separator + Constants.DIR_TEMP + File.separator + "temp.jpg";//临时文件
    private final String PHOTO_HEAD = File.separator + Constants.DIR_TEMP + File.separator + "head.jpg";//头像文件

    private Bitmap headBitmap;//剪切好的图片文件

    private CircleImageView civHead;
    private ActionSheetDialog dialog;
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
        List<SheetItem> lSheetItem = new ArrayList<SheetItem>();
        SheetItem item = new SheetItem("拍照", SheetItem.BULE, new ActionSheetDialog.OnItemClickListener() {
            @Override
            public void onItemClick() {
                openCamera();
            }
        });
        lSheetItem.add(item);

        item = new SheetItem("打开相册", SheetItem.RED, new ActionSheetDialog.OnItemClickListener() {
            @Override
            public void onItemClick() {
                openPic();
            }
        });
        lSheetItem.add(item);

        dialog = new ActionSheetDialog(this, lSheetItem);
        dialog.setTitle("选择图片");

        //设置头像
//        Bitmap bmpHead = BitmapFactory.decodeFile(FileUtils.getAppFile(this, PHOTO_HEAD).getAbsolutePath());
//        Bitmap bmpHead = BitmapUtils.getBitmapFromFile(FileUtils.getAppFile(this, PHOTO_HEAD));
        Bitmap bmpHead = BitmapUtils.getBitmapFromPath(FileUtils.getAppFile(this, PHOTO_HEAD).getAbsolutePath());
        if (null != bmpHead) {
            civHead.setImageBitmap(bmpHead);
        }
    }

    private void initListener() {
        civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    /**
     * 打开摄像头拍照
     */
    private void openCamera() {
        file = FileUtils.getAppFile(this, PHOTO_TEMP);
        if (null == file) {
            ToastUtils.showToast(this, "请检查您的存储空间是否充足");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUESTCODE_CAM);
    }

    /**
     * 在相册中选择照片
     */
    private void openPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUESTCODE_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case REQUESTCODE_CAM:
                startPhotoZoom(Uri.fromFile(file));
                break;

            case REQUESTCODE_PIC:
                if (null != data && null != data.getData()) {
                    startPhotoZoom(data.getData());
                }
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

    /**
     * 将图片显示在界面上
     *
     * @param data
     */
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

        //保存头像到本地
        FileUtils.saveFileFromBitmap(this, headBitmap, PHOTO_HEAD);
    }
}
