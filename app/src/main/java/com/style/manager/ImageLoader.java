package com.style.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import cn.tongchengyuan.app.App;

import com.same.city.love.R;

import cn.tongchengyuan.chat.config.ConfigUtil;
import cn.tongchengyuan.util.BitmapUtil;
import cn.tongchengyuan.util.DisplayUtil;
import uk.viewpager.GlideAutoFitTransform;

import com.style.constant.FileConfig;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static RequestOptions defaultRequestOptions;
    private static RequestOptions bigPictureOptions;
    private static RequestOptions defaultAvatarOptions;
    private static RequestOptions autoFitOptions;

    public static CharSequence getLocalUrl(String fileName) {
        return FileConfig.DIR_CACHE + "/" + fileName;
    }

    private static CharSequence getRemoteUrl(String fileName) {
        return ConfigUtil.getDownUrl(fileName);
    }

    public static RequestOptions getDefaultRequestOptions() {
        if (defaultRequestOptions == null) {
            defaultRequestOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.empty_photo)
                    .error(R.mipmap.image_fail)
                    .priority(Priority.HIGH);
        }
        return defaultRequestOptions;
    }

    public static RequestOptions getDefaultBigPictureOptions() {
        if (bigPictureOptions == null) {
            bigPictureOptions = new RequestOptions().centerCrop().error(R.mipmap.image_fail)
                    .priority(Priority.HIGH);
        }
        return bigPictureOptions;
    }

    public static RequestOptions getDefaultAvatarOptions() {
        if (defaultAvatarOptions == null) {
            defaultAvatarOptions = new RequestOptions().centerCrop().placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .priority(Priority.HIGH);
        }
        return defaultAvatarOptions;
    }

    public static RequestOptions getAutoFitOptions() {
        if (autoFitOptions == null) {
            autoFitOptions = new RequestOptions().error(R.mipmap.image_fail)
                    .priority(Priority.HIGH);
        }
        return autoFitOptions;
    }

    public static void loadAvatar(Context context, ImageView imageView, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            Glide.with(context).load(getLocalUrl(fileName)).apply(getDefaultAvatarOptions()).into(imageView);
        }
    }

    public static void loadPictureByName(Context context, ImageView imageView, String fileName) {
        if (!TextUtils.isEmpty(fileName))
            Glide.with(context).load(getLocalUrl(fileName)).apply(getDefaultRequestOptions()).into(imageView);
    }

    public static void loadPictureByUrl(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Glide.with(context).load(url).apply(getDefaultRequestOptions()).into(imageView);
    }

    public static void loadBigPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Glide.with(context).load(getLocalUrl(url)).apply(getDefaultBigPictureOptions()).into(imageView);
    }

    public static void loadAutoFit(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Glide.with(context).load(getLocalUrl(url)).apply(getAutoFitOptions().transform(new GlideAutoFitTransform(imageView))).into(imageView);
    }

    private static LruCache<String, Bitmap> bitmapCache;

    private static int maxWidth = DisplayUtil.dip2px(App.getInstance(), 130);
    private static int minWidth = DisplayUtil.dip2px(App.getInstance(), 80);

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
