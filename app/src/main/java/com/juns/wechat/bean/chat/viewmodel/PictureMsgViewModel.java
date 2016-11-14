package com.juns.wechat.bean.chat.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.PictureMsg;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.util.DisplayUtil;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.util.TimeUtil;


/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：文本消息的viewModel
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/30
 *******************************************************/
public class PictureMsgViewModel extends MsgViewModel implements View.OnClickListener, View.OnLongClickListener {
    private final Integer[] mResIds = {R.layout.chat_item_received_picture, R.layout.chat_item_sent_picture};

    private PictureMsg pictureMsg;

    public PictureMsgViewModel(Context context, MessageBean messageBean) {
        super(context, messageBean);
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
    }

    @Override
    public View fillView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            int resId = !isShowMyself() ? mResIds[0] : mResIds[1];
            convertView =  mInflater.inflate(resId, viewGroup, false);
        }

        TextView tvDate = ViewHolder.get(convertView, R.id.tv_date);
        ImageView ivAvatar = ViewHolder.get(convertView, R.id.iv_avatar);
        ImageView ivSendPicture = ViewHolder.get(convertView, R.id.ivPicture);
        ProgressBar progressBar = ViewHolder.get(convertView, R.id.progressBar);
        TextView tvPercent = ViewHolder.get(convertView, R.id.percentage);

        if(isShowTime()){
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        }else {
            tvDate.setVisibility(View.GONE);
        }

        if(isShowMyself()){
            loadUrl(ivAvatar, myselfAvatar);
            loadPicture(ivSendPicture, true, PhotoUtil.PHOTO_PATH + "/" + pictureMsg.imgName);
            ImageView ivSendState = ViewHolder.get(convertView, R.id.iv_send_failed);
            if(messageBean.getState() == MessageBean.State.SEND_FAILED.value){
                ivSendState.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
            }else if(messageBean.getState() == MessageBean.State.SEND_SUCCESS.value){
                ivSendState.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
            } else {
                ivSendState.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tvPercent.setVisibility(View.VISIBLE);
                tvPercent.setText(pictureMsg.progress + "%");
            }

        }else {
            loadUrl(ivAvatar, otherAvatar);
            loadPicture(ivSendPicture, false, PhotoUtil.PHOTO_PATH + "/" + pictureMsg.imgName);
            if(pictureMsg.progress == 100){
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
                tvPercent.setVisibility(View.VISIBLE);
                tvPercent.setText(pictureMsg.progress + "%");
            }
        }

        ivAvatar.setOnClickListener(this);
        return convertView;
    }

    private void loadPicture(ImageView imageView, boolean isShowMyself, String path){
        ImageLoader.loadTriangleImage(imageView, path, isShowMyself ? 1 : 0);
    }

    @Override
    public void onClick(View v) {
        if(isShowMyself()){
            super.onUserPhotoClick(myselfName);
        }else {
            super.onUserPhotoClick(otherName);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        popupShow();
        return true;
    }

    /**
     * 触发弹出显示
     */
    private void popupShow(){

    }

    @Override
    public int getType() {
        return MsgType.MSG_TYPE_PICTURE;
    }
}
