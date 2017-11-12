package com.juns.wechat.chat.ui;

import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.greendao.mydao.GreenDaoManager;
import com.same.city.love.R;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderVoiceLeft extends ViewHolderVoiceBase {
    ImageView ivUnreadVoice;

    ViewHolderVoiceLeft(View view) {
        super(view);
        ivUnreadVoice = (ImageView) view.findViewById(R.id.iv_unread_voice);
    }

    @Override
    protected void updateView() {
        super.updateView();
        ivUnreadVoice.setVisibility(voiceMsg.playState == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        updateMsgPlayState();
    }
    private void updateMsgPlayState(){
        if(voiceMsg.playState == 0){
            voiceMsg.playState = 1;
            messageBean.setMsg(voiceMsg.toJson());
            GreenDaoManager.getInstance().save(messageBean);
        }
    }
}
