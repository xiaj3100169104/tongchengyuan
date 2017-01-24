package com.juns.wechat.chat.im;

import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.dao.MessageDao;

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
            MessageDao.getInstance().update(messageBean);
        }
    }
}
