package com.juns.wechat.chat.ui;

import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.util.ImageLoader;
import com.style.constant.FileConfig;

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
        //先得到bean，在进行其他操作
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
        super.updateView();
        loadPicture(ivPicture, FileConfig.DIR_CACHE + "/" + pictureMsg.imgName);

    }

    private void loadPicture(ImageView imageView, String path) {
        ImageLoader.loadTriangleImage(imageView, path, isLeftLayout() ? 0 : 1);
    }
    @Override
    protected void onClickLayoutContainer(){
        super.onClickLayoutContainer();
    }
}

