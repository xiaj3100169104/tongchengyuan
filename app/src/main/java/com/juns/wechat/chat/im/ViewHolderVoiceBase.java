package com.juns.wechat.chat.im;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.VoiceMsg;
import com.juns.wechat.util.AudioManager;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderVoiceBase extends BaseMsgViewHolder {
    ImageView ivVoice;
    TextView tvVoicePadding;
    TextView tvVoiceLength;
    protected VoiceMsg voiceMsg;

    ViewHolderVoiceBase(View view) {
        super(view);
        ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
        tvVoicePadding = (TextView) view.findViewById(R.id.tv_voice_padding);
        tvVoiceLength = (TextView) view.findViewById(R.id.tv_length);
    }

    @Override
    protected void updateView() {
        super.updateView();
        voiceMsg = (VoiceMsg) messageBean.getMsgObj();
        tvVoicePadding.setText(getPaddingByVoiceLength(voiceMsg.seconds));
        tvVoiceLength.setText(voiceMsg.seconds + "''");
    }

    private String getPaddingByVoiceLength(int voiceLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= voiceLength; i++) {
            if (i % 3 == 0) {
                sb.append("   ");
            }
        }
        return sb.toString();
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        ChatMediaPlayer chatMediaPlayer = ChatMediaPlayer.getInstance();
        if (chatMediaPlayer.isRunning()) {
            chatMediaPlayer.stopVoice();
        }
        chatMediaPlayer.setVoiceView(ivVoice);
        chatMediaPlayer.setVoiceDir(!isLeftLayout());
        chatMediaPlayer.playVoice(AudioManager.RECORD_PATH + "/" + voiceMsg.fileName);

    }
}
