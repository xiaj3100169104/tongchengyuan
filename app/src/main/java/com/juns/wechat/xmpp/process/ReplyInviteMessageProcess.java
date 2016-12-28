package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.chat.InviteMsg;
import com.juns.wechat.bean.chat.TextMsg;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.util.Date;

/*******************************************************
 * Created by 王者 on 2015/11/27
 *******************************************************/
public class ReplyInviteMessageProcess extends MessageProcess {
    private InviteMsg inviteMsg;

    public ReplyInviteMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(final MessageBean messageBean) {
        inviteMsg = (InviteMsg) messageBean.getMsgObj();
        if(inviteMsg.reply != InviteMsg.Reply.ACCEPT.value) return;  //非法状态
        HttpAction.queryUserData(messageBean.getOtherName(), new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                if (data != null) {
                    UserDao.getInstance().replace(data);
                    saveMessageToDB(messageBean);
                    noticeShow(messageBean, null);
                }
            }

            @Override
            protected void onCodeFailure(String msg) {

            }
        });
       /* UserRequest.queryUserData(messageBean.getOtherName(), new QueryUserCallBack() {
            @Override
            protected void handleResponse(BaseResponse.QueryUserResponse result) {
                super.handleResponse(result);  //将用户信息存到数据库
                saveMessageToDB(messageBean);
                noticeShow(messageBean, null);
            }
        });*/
    }

    /**
     * 这条消息会保存，不会在消息界面显示，所以构造一条普通的文本消息
     * @param messageBean
     */
    @Override
    protected void saveMessageToDB(MessageBean messageBean) {
        super.saveMessageToDB(messageBean);
        MessageBean textMessage = new MessageBean();
        textMessage.setMyselfName(messageBean.getMyselfName());
        textMessage.setOtherName(messageBean.getOtherName());

        TextMsg textMsg = new TextMsg();
        textMsg.content = inviteMsg.reason;
        textMessage.setMsg(textMsg.toJson());
        textMessage.setType(MsgType.MSG_TYPE_TEXT);
        textMessage.setTypeDesc(textMsg.content);

        String packetId = StanzaIdUtil.newStanzaId();
        textMessage.setPacketId(packetId);
        textMessage.setDate(new Date());
        textMessage.setState(MessageBean.State.SEND_SUCCESS.value);
        textMessage.setDirection(MessageBean.Direction.INCOMING.value);

        MessageDao.getInstance().save(textMessage);
    }

    @Override
    public void noticeShow(MessageBean entity, String notice) {
        String noticeStr = inviteMsg.userName + "同意了你的添加好友的请求";
        super.noticeShow(entity, noticeStr);
    }
}
