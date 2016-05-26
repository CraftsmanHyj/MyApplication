package com.hyj.lib.lock.lockpattern3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Administrator on 2016/4/26.
 */
public class BitmapUtil {
    /**
     * 图片缩放
     *
     * @param bitmap 源图片
     * @param zf     缩放比
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float zf) {
        return zoom(bitmap, zf, zf);
    }

    /**
     * 缩放图片
     *
     * @param bitmap 源图片
     * @param wf     宽度缩放比
     * @param hf     高度缩放比
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
        Matrix matrix = new Matrix();
        matrix.postScale(wf, hf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        // RCB means Rounded Corner Bitmap
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }
}
