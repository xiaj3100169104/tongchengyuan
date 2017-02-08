package com.juns.wechat.chat.im;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.PhotoUtil;
import com.style.manager.ImageLoadManager;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureBase extends BaseMsgViewHolder {
    ImageView ivPicture;
    ProgressBar progressBar;
    TextView tvPercent;
    protected PictureMsg pictureMsg;

    ViewHolderPictureBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        tvPercent = (TextView) view.findViewById(R.id.percentage);
    }

    @Override
    protected void updateView() {
        //super.updateView();
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
        loadPicture(ivPicture, PhotoUtil.PHOTO_PATH + "/" + pictureMsg.imgName);
        if (pictureMsg.progress == 100) {
            progressBar.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            tvPercent.setVisibility(View.VISIBLE);
            tvPercent.setText(pictureMsg.progress + "%");
        }
    }

    private void loadPicture(ImageView imageView, String path) {
        ImageLoader.loadTriangleImage(imageView, path, isLeftLayout() ? 0 : 1);
    }
    @Override
    protected void onClickLayoutContainer(){
        Log.e(TAG, "ViewHolderPictureBase");
    }
}

