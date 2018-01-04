package cn.tongchengyuan.chat.ui;

import android.util.Log;
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
    protected void onSending() {
        super.onSending();
        tvSendPercent.setVisibility(View.VISIBLE);
        Log.e(TAG, "onSending progress==" + videoMsg.progress);
        tvSendPercent.setText(videoMsg.progress + "%");
        if (videoMsg.progress == 100){
            super.onSendSucceed();
        }
    }

}
