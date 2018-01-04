package cn.tongchengyuan.chat.xmpp.process;

import android.content.Context;

import cn.tongchengyuan.chat.bean.InviteMsgData;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.bean.TextMsgData;

import com.style.net.core.NetDataBeanCallback;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.util.Date;

/*******************************************************
 * Created by 王者 on 2015/11/27
 *******************************************************/
public class ReplyInviteMessageProcess extends MessageProcess {
    private InviteMsgData inviteMsg;

    public ReplyInviteMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(final MessageBean messageBean) {
        inviteMsg = (InviteMsgData) messageBean.getMsgDataObj();
        if(inviteMsg.reply != InviteMsgData.Reply.ACCEPT.value) return;  //非法状态
        HttpActionImpl.getInstance().queryUserData("process", messageBean.getOtherUserId(), new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                if (data != null) {
                    GreenDaoManager.getInstance().save(data);
                    saveMessageToDB(messageBean);
                    noticeShow(messageBean, null);
                }
            }

            @Override
            protected void onCodeFailure(String msg) {

            }
        });
    }

    /**
     * 这条消息会保存，不会在消息界面显示，所以构造一条普通的文本消息
     * @param messageBean
     */
    @Override
    protected void saveMessageToDB(MessageBean messageBean) {
        super.saveMessageToDB(messageBean);
        MessageBean textMessage = new MessageBean();
        textMessage.setMyUserId(messageBean.getMyUserId());
        textMessage.setOtherUserId(messageBean.getOtherUserId());

        TextMsgData textMsg = new TextMsgData();
        textMsg.content = inviteMsg.reason;
        textMessage.setMsg(textMsg.toJson());
        textMessage.setType(MessageBean.Type.TEXT);

        String packetId = StanzaIdUtil.newStanzaId();
        textMessage.setPacketId(packetId);
        textMessage.setDate(new Date());
        textMessage.setState(MessageBean.State.SEND_SUCCESS);
        textMessage.setDirection(MessageBean.Direction.INCOMING);

        GreenDaoManager.getInstance().save(textMessage);
    }

    @Override
    public void noticeShow(MessageBean entity, String notice) {
        String noticeStr = inviteMsg.userName + "同意了你的添加好友的请求";
        super.noticeShow(entity, noticeStr);
    }
}
