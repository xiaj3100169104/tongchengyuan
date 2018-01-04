package uk.viewpager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.style.utils.CommonUtil;

import java.security.MessageDigest;

import cn.tongchengyuan.app.App;

/**
 * Created by xiajun on 2017/12/23.
 */

public class GlideAutoFitTransform extends BitmapTransformation {
    private final static String TAG = "GlideAutoFitTransform";
    ImageView imageView;
    //注意计算高宽比需全部转化Wie浮点数，不然很容易出错
    float mScreenWidth;
    float mScreenHeight;

    public GlideAutoFitTransform(ImageView imageView) {
        this.imageView = imageView;
        mScreenWidth = CommonUtil.getScreenWidth(App.getInstance());
        mScreenHeight = CommonUtil.getScreenHeight(App.getInstance());

    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        return circleCrop(pool, source);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();

        float screenYScale = mScreenHeight / mScreenWidth;
        float sourceHeight = source.getHeight();
        float sourceWidth = source.getWidth();
        Log.e(TAG, "source-->Height: " + sourceHeight + "    width: " + sourceWidth);
        float sourceYScale = sourceHeight / sourceWidth;
        //如果原图片的高宽比大于了屏幕的高宽比
        if (sourceYScale >= screenYScale) {
            params.height = (int) mScreenHeight;
            params.width = (int) (mScreenWidth / screenYScale);//注释：params.height/params.width = sourceYScale;
        } else {
            float sourceXScale = sourceWidth / sourceHeight;
            params.width = (int) mScreenWidth;
            params.height = (int) (mScreenHeight / sourceXScale);//注释：params.width/params.height = sourceXScale;
        }
        Log.e(TAG, "params-->Height: " + params.height + "    Width: " + params.width);

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        imageView.post(new Runnable() {
            @Override
            public void run() {
                //imageView.setLayoutParams(params);

            }
        });
        return source;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
