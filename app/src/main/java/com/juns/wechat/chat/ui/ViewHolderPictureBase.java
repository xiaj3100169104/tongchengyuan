package com.juns.wechat.chat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.PictureMsg;
import com.style.constant.FileConfig;
import com.style.manager.ImageLoader;
import com.style.utils.CommonUtil;

import java.io.File;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected PictureMsg pictureMsg;
    protected String path;

    ViewHolderPictureBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
        float width = pictureMsg.width;
        float height = pictureMsg.height;
        float scale = height / width;
        if (scale >= 1) {             //高大于宽
            width = CommonUtil.dip2px(context, 80);
            height = width * scale;
        } else {
            width = CommonUtil.dip2px(context, 160);
            height = width * scale;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPicture.getLayoutParams();
        params.width = (int) width;
        params.height = (int) height;

        this.path = FileConfig.DIR_CACHE + "/" + pictureMsg.imgName;
        super.updateView();

    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
    }

    //加载图片，已发送或一接收的图片消息
    protected void loadPicture() {
        File f = new File(path);
        if (f.exists())
            ImageLoader.loadTriangleImage(ivPicture, path, isLeftLayout() ? 0 : 1);
        else
            PictureMsgLoader.getInstance().down(path, pictureMsg.size, messageBean);
    }

}

