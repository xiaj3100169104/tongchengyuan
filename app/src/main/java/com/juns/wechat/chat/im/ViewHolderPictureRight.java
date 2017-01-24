package com.juns.wechat.chat.im;

import android.view.View;

import com.juns.wechat.chat.bean.MessageBean;

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
        if (messageBean.getState() == MessageBean.State.SEND_FAILED.value) {
            tvPercent.setVisibility(View.GONE);
        } else if (messageBean.getState() == MessageBean.State.SEND_SUCCESS.value) {
            tvPercent.setVisibility(View.GONE);
        } else {
            tvPercent.setVisibility(View.VISIBLE);
            tvPercent.setText(pictureMsg.progress + "%");
        }
    }
}
