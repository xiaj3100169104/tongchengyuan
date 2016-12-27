package com.juns.wechat.chat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.LogUtil;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import org.xutils.common.Callback;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends BaseToolbarActivity {
	private ImageView photoView;
	private ProgressBar loadLocalPb;
    private String imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_show_big_image;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("我的头像");
        photoView = (ImageView) findViewById(R.id.image);
        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);

        imgName = getIntent().getStringExtra(Skip.KEY_IMG_NAME);
        String url = ConfigUtil.REAL_SERVER + "/upload/"+imgName;
        //Glide.with(this).load(url).into(photoView);

        ImageLoader.loadBigAvatar(photoView, imgName, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                LogUtil.i("success!");
                PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
                attacher.update();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
