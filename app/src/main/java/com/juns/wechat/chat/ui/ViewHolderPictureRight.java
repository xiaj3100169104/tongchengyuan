package com.juns.wechat.chat.ui;

import android.view.View;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureRight extends ViewHolderPictureBase {

    ViewHolderPictureRight(View view) {
        super(view);

    }

    protected boolean isLeftLayout() {
        return false;
    }

    @Override
    protected void updateView() {
        super.updateView();

    }

   /* @Override
    protected void onSendSucceed() {
        super.onSendSucceed();
        tvSendPercent.setVisibility(View.GONE);

    }

    @Override
    protected void onSendFailed() {
        super.onSendFailed();
        tvSendPercent.setVisibility(View.GONE);

    }*/

    @Override
    protected void onSending() {
        super.onSending();
        tvSendPercent.setVisibility(View.VISIBLE);
        tvSendPercent.setText(pictureMsg.progress + "%");
        if (pictureMsg.progress == 100){
            super.onSendSucceed();
        }
    }
}
