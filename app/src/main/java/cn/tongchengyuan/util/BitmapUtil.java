package cn.tongchengyuan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import cn.tongchengyuan.app.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by 王者 on 2016/8/22.
 */
public class BitmapUtil {

    /**
     * 通过图片质量进行压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        if (baos.toByteArray().length / 1000 > 1000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        if (w <= 0 || h <= 0) { //无效图片
            return null;
        }
        float screenW = DisplayUtil.getScreenMetrics(App.getInstance()).x;
        float screenY = DisplayUtil.getScreenMetrics(App.getInstance()).y;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > screenW) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / screenW);
        } else if (w < h && h > screenY) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / screenY);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (be > 1) {
            int scaleX = (int) screenW;
            int scaleY = (int) (scaleX * 0.75f);
            Point centerP = new Point(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            int startX = centerP.x - scaleX / 2;
            int startY = centerP.y - scaleY / 2;
            bitmap = Bitmap.createBitmap(bitmap, startX, startY, scaleX, scaleY);
        }
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 生成带一个小三角的图形
     *
     * @param bitmap_in
     * @return
     */
    public static Bitmap getTriangleImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        int w = bitmap_in.getWidth();
        int h = bitmap_in.getHeight();
        Bitmap triangleImage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(triangleImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, w, h);
        Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap_in, rectF, rect, paint);
        return triangleImage;
    }

    /**
     * 生成带三角以及边框的image
     *
     * @param bitmap_bg
     * @param bitmap_in
     * @return
     */
    public static Bitmap getCornerAndTriangleImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        Bitmap roundConcerImage = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, 500, 500);
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        Rect rect2 = new Rect(2, 2, 498, 498);
        canvas.drawBitmap(bitmap_in, rect, rect2, paint);
        return roundConcerImage;
    }
}
