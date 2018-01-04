package cn.tongchengyuan.chat.xmpp.process;

import android.content.Context;

import cn.tongchengyuan.chat.bean.InviteMsgData;
import cn.tongchengyuan.chat.bean.MessageBean;

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
        InviteMsgData inviteMsg = (InviteMsgData) entity.getMsgDataObj();
        String noticeStr = inviteMsg.userName + "请求添加你为好友";
        super.noticeShow(entity, noticeStr);
    }
}
