package cn.tongchengyuan.chat.ui;

import android.util.Log;
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

   /*
    @Override
    protected void onSendFailed() {
        super.onSendFailed();
        tvSendPercent.setVisibility(View.GONE);

    }*/

    @Override
    protected void onSending() {
        super.onSending();
        tvSendPercent.setVisibility(View.VISIBLE);
        Log.e(TAG, "onSending progress==" + pictureMsg.progress);
        tvSendPercent.setText(pictureMsg.progress + "%");
        if (pictureMsg.progress == 100){
            super.onSendSucceed();
        }
    }

}
