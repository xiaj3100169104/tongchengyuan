package com.juns.wechat.chat.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.OfflineVideoMsg;
import com.juns.wechat.chat.offlineVideo.PlayVideoActivity;
import com.juns.wechat.util.ImageLoader;

import com.style.constant.FileConfig;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderOfflineVideoBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected OfflineVideoMsg videoMsg;

    ViewHolderOfflineVideoBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        videoMsg = (OfflineVideoMsg) messageBean.getMsgObj();
        super.updateView();
        //loadPicture(ivPicture, FileConfig.DIR_CACHE + "/" + videoMsg.fileName);

    }

    private void loadPicture(ImageView imageView, String path) {
        ImageLoader.loadTriangleImage(imageView, path, isLeftLayout() ? 0 : 1);
    }

    @Override
    protected void onClickLayoutContainer(){
        super.onClickLayoutContainer();
        String path = FileConfig.DIR_CACHE + "/" + videoMsg.fileName;
        Log.e(TAG, "path==" + path);
        Intent data = new Intent();
        data.putExtra("path", path);
        data.setClass(context, PlayVideoActivity.class);
        context.startActivity(data);
    }
}

