package com.style.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.juns.wechat.R;

public class ImageLoadManager {
    public static void loadNormalAvatar(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
        Glide.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView);
    }

    public static void loadNormalPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
        Glide.with(context).load(url).placeholder(R.mipmap.empty_photo).error(R.mipmap.image_fail).into(imageView);
    }

    public static void loadBigPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
        Glide.with(context).load(url).error(R.mipmap.image_fail).into(imageView);
    }
}
