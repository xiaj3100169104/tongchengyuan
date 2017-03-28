package com.juns.wechat.chat.xmpp.process;

import android.content.Context;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.InviteMsg;

/*******************************************************
 * Created by 王者 on 2015/11/27
 *******************************************************/
public class InviteMessageProcess extends MessageProcess {

    public InviteMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(MessageBean messageBean) {
        super.processMessage(messageBean);
    }

    @Override
    public void noticeShow(MessageBean entity, String notice) {
        InviteMsg inviteMsg = (InviteMsg) entity.getMsgObj();
        String noticeStr = inviteMsg.userName + "请求添加你为好友";
        super.noticeShow(entity, noticeStr);
    }
}
