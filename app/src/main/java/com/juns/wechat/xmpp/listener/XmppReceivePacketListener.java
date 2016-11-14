package com.juns.wechat.xmpp.listener;


import com.juns.wechat.App;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.xmpp.extensionelement.TimeElement;
import com.juns.wechat.xmpp.process.IQRouter;
import com.juns.wechat.xmpp.process.InviteMessageProcess;
import com.juns.wechat.xmpp.process.MessageProcess;
import com.juns.wechat.xmpp.process.PictureMessageProcess;
import com.juns.wechat.xmpp.process.ReplyInviteMessageProcess;
import com.juns.wechat.xmpp.process.TextMessageProcess;
import com.juns.wechat.xmpp.process.UnknownTypeMessageProcess;
import com.juns.wechat.xmpp.process.VoiceMessageProcess;
import com.juns.wechat.xmpp.util.ConvertUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import java.util.HashMap;
import java.util.Map;

/*******************************************************
 * Created by 王者 on 2015/11/19
 *******************************************************/
public class XmppReceivePacketListener implements StanzaListener {
    private Map<Integer, MessageProcess> processMap = new HashMap<>();

    /**
     * 处理包：stanaza有3种子类型：Message(消息），Presence(状态),IQ（信息查询）
     * @param packet
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        if(packet instanceof Message){
            Message message = (Message) packet;
            handleMessage(message);
        }else if(packet instanceof Presence){
            Presence presence = (Presence) packet;
            handlePresence(presence);
        }else if(packet instanceof  IQ){
            IQ iq = (IQ) packet;
            handleIQ(iq);
        }
    }

    /**
     * 处理消息，Type为normal:发送向服务器发送一条消息时，服务器的回执消息，表明消息已收到
     * Type为chat,别人发给我的聊天消息，Type为error，消息未发送成功
     * @param message
     */
    private void handleMessage(Message message) {
        if(Message.Type.normal == message.getType()){
            updateExistMessage(message);
        }else if(Message.Type.chat == message.getType()){
            handleChatMessageByType(message);
        }else if(Message.Type.error == message.getType()){ }
    }

    /**
     * 处理Presence
     * @param presence
     */
    private void handlePresence(Presence presence){
        LogUtil.i(presence.toString());
    }

    private void handleIQ(IQ iq){
        IQRouter.getInstance().routeIQ(iq);
    }

    /**
     * 更新数据库已存在的消息数据的状态
     * @param message
     */
    private void updateExistMessage(Message message){
        if(("receipt." + ConfigUtil.getXmppDomain()).equals(message.getFrom())){
            TimeElement timeElement = TimeElement.from(message);

            MessageDao.getInstance().updateMessageState(message.getStanzaId(),
                    MessageBean.State.SEND_SUCCESS.value, timeElement.getTime());
        }
    }

    /**
     * 处理聊天消息
     * 1将{类型
     * 2 根据转换后的实体类得到的type来调用不同的子类具体处理
     * @param message
     */
    private void handleChatMessageByType(Message message){
        MessageBean messageBean = ConvertUtil.convertToMessageBean(message);
        int type = messageBean.getType();
        MessageProcess messageProcess = processMap.get(type);
        if(messageProcess == null){
                switch (type){
                    case MsgType.MSG_TYPE_TEXT:
                        messageProcess = new TextMessageProcess(App.getInstance());
                        break;
                    case MsgType.MSG_TYPE_VOICE:
                        messageProcess = new VoiceMessageProcess(App.getInstance());
                        break;
                    case MsgType.MSG_TYPE_PICTURE:
                        messageProcess = new PictureMessageProcess(App.getInstance());
                        break;
                    case MsgType.MSG_TYPE_SEND_INVITE:
                        messageProcess = new InviteMessageProcess(App.getInstance());
                        break;
                    case MsgType.MSG_TYPE_REPLY_INVITE:
                        messageProcess = new ReplyInviteMessageProcess(App.getInstance());
                        break;
                    default:
                        messageProcess = new UnknownTypeMessageProcess(App.getInstance());
                        break;
                }
        }

        processMap.put(type, messageProcess);
        messageProcess.processMessage(messageBean);
    }
}
