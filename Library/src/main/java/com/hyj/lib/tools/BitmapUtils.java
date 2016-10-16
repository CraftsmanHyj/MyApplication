package com.hyj.lib.tools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片操作工具类
 *
 * @Author hyj
 * @Date 2015-12-16 下午3:03:10
 */
public class BitmapUtils {

    /**
     * 采样率：指定解析图片的比例
     *
     * @param option    BitmapFactory.Options
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return int图片压缩比例
     */
    public static int calculateInSampleSize(Options option, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int width = option.outWidth;
        int height = option.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int scalewidth = Math.round(width * 1.0f / reqWidth);
            int scaleHeight = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.min(scalewidth, scaleHeight);
        }

        return inSampleSize;
    }

    /**
     * 通过图片路径获取bitmap
     *
     * @param path 图片绝对路径
     * @return
     */
    public static Bitmap getBitmapFromPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        // decode image size
        Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);

        // Find the correct scale value. It should be the power of 2.
        int REQUIRED_SIZE = 100;

        int tempWidth = option.outWidth;
        int tempHeight = option.outHeight;
        int inSampleSize = 1;

        while (tempWidth / 2 >= REQUIRED_SIZE && tempHeight / 2 >= REQUIRED_SIZE) {
            tempWidth /= 2;
            tempHeight /= 2;
            inSampleSize *= 2;
        }

        // decode with inSampleSize
        option.inSampleSize = inSampleSize;// 采样率：指定解析图片的比例
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, option);
    }

    /**
     * 把图片(File)解析成Bitmap
     *
     * @param file 图片文件
     * @return Bitmap
     */
    public static Bitmap getBitmapFromFile(File file) {
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // decode image size
            Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, option);

            // Find the correct scale value. It should be the power of 2.
            int REQUIRED_SIZE = 100;

            int tempWidth = option.outWidth;
            int tempHeight = option.outHeight;
            int inSampleSize = 1;

            while (tempWidth / 2 >= REQUIRED_SIZE && tempHeight / 2 >= REQUIRED_SIZE) {
                tempWidth /= 2;
                tempHeight /= 2;
                inSampleSize *= 2;
            }

            // decode with inSampleSize
            option.inSampleSize = inSampleSize;// 采样率：指定解析图片的比例
            option.inJustDecodeBounds = false;

            fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis, null, option);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据图片路径获得压缩过的bitmap
     *
     * @param filePath  文件绝对路径
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 压缩过的Bitmap
     */
    public static Bitmap getCompressBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, option);

        int inSampleSize = calculateInSampleSize(option, reqWidth, reqHeight);
        option.inSampleSize = inSampleSize;
        option.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, option);
    }

    /**
     * 将图片压缩至固定大小的值
     *
     * @param bm        源图片
     * @param newWidth  目标宽度
     * @param newHeight 目标高度
     * @return
     */
    public static Bitmap getResizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获取图片的宽、高
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 计算缩放比例
        float scaleWidth = newWidth * 1.0f / width;
        float scaleHeight = newHeight * 1.0f / height;

        return getZoomBitmap(bm, scaleWidth, scaleHeight);
    }

    /**
     * 图片缩放
     *
     * @param bitmap 源图片
     * @param scale  宽高缩放比
     * @return
     */
    public static Bitmap getZoomBitmap(Bitmap bitmap, float scale) {
        return getZoomBitmap(bitmap, scale, scale);
    }

    /**
     * 缩放图片
     *
     * @param bitmap      源图片
     * @param scaleWidth  宽度缩放比
     * @param scaleHeight 高度缩放比
     * @return
     */
    public static Bitmap getZoomBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片当前的旋转角度
     *
     * @param path 图片绝对路径
     * @return
     */
    public static int getPictureAngle(String path) {
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return angle;
    }

    /**
     * 旋转图片
     *
     * @param bitmap 源图片
     * @param angle  旋转角度
     * @return 旋转后的Bitmap
     */
    public static Bitmap setBitmapRotate(Bitmap bitmap, int angle) {
        if (null == bitmap) {
            return bitmap;
        }

        // 旋转图片
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 设置圆角图片
     *
     * @param bitmap  源图片
     * @param roundPx 圆角角度
     * @return Bitmap
     */
    public static Bitmap setBitmapRoundCorner(Bitmap bitmap, float roundPx) {
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        Bitmap outBitmap = Bitmap.createBitmap(bw, bh, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bw, bh);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return outBitmap;
    }

    /**
     * 将彩色图片转换成灰色图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap setBitmapGrey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }
}