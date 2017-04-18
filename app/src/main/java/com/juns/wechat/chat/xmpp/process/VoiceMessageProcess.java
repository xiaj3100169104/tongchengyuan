package com.juns.wechat.chat.xmpp.process;

import android.content.Context;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.VoiceMsg;
import com.juns.wechat.chat.xmpp.util.MsgCode;
import com.juns.wechat.util.AudioManager;

import java.io.File;


/*******************************************************
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class VoiceMessageProcess extends MessageProcess {

    public VoiceMessageProcess(Context context) {
        super(context);
    }

}
