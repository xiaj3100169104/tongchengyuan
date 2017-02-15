package com.juns.wechat.chat.im;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
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

    }

    @Override
    protected void onSendSucceed() {
        super.onSendSucceed();
        tvSendPercent.setVisibility(View.GONE);

    }

    @Override
    protected void onSendFailed() {
        super.onSendFailed();
        tvSendPercent.setVisibility(View.GONE);

    }

    @Override
    protected void onSendOtherStatus() {
        super.onSendOtherStatus();
        tvSendPercent.setVisibility(View.VISIBLE);
        tvSendPercent.setText(pictureMsg.progress + "%");
    }
}
