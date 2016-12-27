package com.juns.wechat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;

import org.xutils.cache.LruCache;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/16.
 */
public class ImageLoader {
    private static final String LOCAL_PATH = PhotoUtil.PHOTO_PATH;
    private static final String REMOTE_PATH = ConfigUtil.REAL_SERVER + "/upload/";
    private static LruCache<String, Bitmap> bitmapCache;

    private static int maxWidth = DisplayUtil.dip2px(App.getInstance(), 130);
    private static int minWidth = DisplayUtil.dip2px(App.getInstance(), 80);

    private static final ImageOptions OPTIONS_AVATAR = new ImageOptions.Builder()
            .setFailureDrawableId(R.drawable.default_useravatar)
            .setLoadingDrawableId(R.drawable.default_useravatar)
            .build();
    private static final ImageOptions OPTIONS_PICTURE = new ImageOptions.Builder()
            .setFailureDrawableId(R.mipmap.image_fail)
            .setLoadingDrawableId(R.mipmap.empty_photo)
            .build();

    public static void loadAvatar(ImageView imageView, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            imageView.setImageResource(R.drawable.default_useravatar);
        } else {
            x.image().bind(imageView, REMOTE_PATH + fileName, OPTIONS_AVATAR);
        }
    }

    public static void loadPicture(ImageView imageView, String fileName) {
        if (!TextUtils.isEmpty(fileName))
            x.image().bind(imageView, REMOTE_PATH + fileName, OPTIONS_PICTURE);
    }

    public static void loadBigPicture(ImageView imageView, String fileName, Callback.CommonCallback<Drawable> callback) {
        if (!TextUtils.isEmpty(fileName))
            x.image().bind(imageView, REMOTE_PATH + fileName, OPTIONS_PICTURE, callback);
    }

    public static void loadBigAvatar(ImageView imageView, String fileName, Callback.CommonCallback<Drawable> callback) {
        if (TextUtils.isEmpty(fileName)) {
            imageView.setImageResource(R.drawable.default_useravatar);
        } else {
            x.image().bind(imageView, REMOTE_PATH + fileName, OPTIONS_AVATAR, callback);
        }
    }

    public static void loadTriangleImage(ImageView imageView, String filePath, int direction) {
        if (bitmapCache == null) {
            bitmapCache = new LruCache<>(30);
        }
        Bitmap cache = bitmapCache.get(filePath + "_" + direction);
        if (cache != null) {
            imageView.setImageBitmap(cache);
        } else {
            Bitmap source = BitmapFactory.decodeFile(filePath);
            Bitmap wantToLoad;
            if (source != null) {
                int bw = source.getWidth();
                int bh = source.getHeight();
                int w = 0;
                int h = 0;
                if (bw > bh) {
                    w = maxWidth;
                } else {
                    w = minWidth;
                }
                h = (int) (bh * ((float) w / bw));
                Bitmap bitmap = Bitmap.createScaledBitmap(source, w, h, false);
                source.recycle();
                int bgResId = 0;
                if (direction == 0) {
                    bgResId = R.drawable.chat_adapter_to_bg_left;
                } else {
                    bgResId = R.drawable.chat_adapter_to_bg;
                }
                Bitmap bitmap_bg = BitmapFactory.decodeResource(App.getInstance().getResources(), bgResId);
                wantToLoad = BitmapUtil.getTriangleImage(bitmap_bg, bitmap);

                bitmapCache.put(filePath + "_" + direction, wantToLoad); //未成功加载可能是图片尚未下载下来，不要放进缓存

            } else {
                if (direction == 0) {
                    wantToLoad = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.aev);
                } else {
                    wantToLoad = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.aex);
                }
            }

            imageView.setImageBitmap(wantToLoad);
        }
    }

}
