package com.juns.wechat.chat.im;

import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.PhotoUtil;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected PictureMsg pictureMsg;

    ViewHolderPictureBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        super.updateView();
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
        loadPicture(ivPicture, PhotoUtil.PHOTO_PATH + "/" + pictureMsg.imgName);

    }

    private void loadPicture(ImageView imageView, String path) {
        ImageLoader.loadTriangleImage(imageView, path, isLeftLayout() ? 0 : 1);
    }
    @Override
    protected void onClickLayoutContainer(){
        super.onClickLayoutContainer();
    }
}

