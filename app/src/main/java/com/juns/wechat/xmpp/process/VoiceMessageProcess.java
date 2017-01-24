package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.VoiceMsg;
import com.juns.wechat.util.AudioManager;
import com.juns.wechat.xmpp.util.MsgCode;

import java.io.File;


/*******************************************************
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class VoiceMessageProcess extends MessageProcess {
    private MsgCode msgCode = new MsgCode();

    public VoiceMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(MessageBean messageBean) {
        VoiceMsg voiceMsg = (VoiceMsg) messageBean.getMsgObj();
        File file = new File(AudioManager.RECORD_PATH, voiceMsg.fileName);
        if(!file.exists()){ //由于文件都是唯一生成的，如果本地存在，说明是本地发出去，本地接收的
            msgCode.decode(voiceMsg.encodeStr, file.getPath());
        }
        saveMessageToDB(messageBean);
        noticeShow(messageBean);
    }
}
