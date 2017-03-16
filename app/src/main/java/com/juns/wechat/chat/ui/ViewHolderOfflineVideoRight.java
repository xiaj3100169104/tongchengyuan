package com.juns.wechat.chat.ui;

import android.view.View;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderOfflineVideoRight extends ViewHolderOfflineVideoBase {

    ViewHolderOfflineVideoRight(View view) {
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
        tvSendPercent.setText(videoMsg.progress + "%");
    }
}
