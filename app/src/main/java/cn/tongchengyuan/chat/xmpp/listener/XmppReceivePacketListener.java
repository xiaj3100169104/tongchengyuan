package cn.tongchengyuan.chat.xmpp.listener;


import cn.tongchengyuan.chat.xmpp.process.PictureMessageProcess;
import cn.tongchengyuan.chat.xmpp.util.ConvertUtil;
import cn.tongchengyuan.app.App;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.chat.xmpp.XmppManagerUtil;
import cn.tongchengyuan.chat.xmpp.extensionelement.TimeElement;
import cn.tongchengyuan.chat.xmpp.iq.MarkAsReadIQ;
import cn.tongchengyuan.chat.xmpp.process.IQRouter;
import cn.tongchengyuan.chat.xmpp.process.InviteMessageProcess;
import cn.tongchengyuan.chat.xmpp.process.MessageProcess;
import cn.tongchengyuan.chat.xmpp.process.OfflineVideoMessageProcess;
import cn.tongchengyuan.chat.xmpp.process.ReplyInviteMessageProcess;
import cn.tongchengyuan.chat.xmpp.process.TextMessageProcess;
import cn.tongchengyuan.chat.xmpp.process.UnknownTypeMessageProcess;
import cn.tongchengyuan.chat.xmpp.process.VoiceMessageProcess;
import cn.tongchengyuan.chat.config.ConfigUtil;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.util.LogUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/*******************************************************
 * Created by 王者 on 2015/11/19
 *******************************************************/
public class XmppReceivePacketListener implements StanzaListener {
    private Map<Integer, MessageProcess> processMap = new HashMap<>();

    /**
     * 处理包：stanaza有3种子类型：Message(消息），Presence(状态),IQ（信息查询）
     *
     * @param packet
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        if (packet instanceof Message) {
            Message message = (Message) packet;
            handleMessage(message);
        } else if (packet instanceof Presence) {
            Presence presence = (Presence) packet;
            handlePresence(presence);
        } else if (packet instanceof IQ) {
            IQ iq = (IQ) packet;
            handleIQ(iq);
        }
    }

    /**
     * 处理消息，Type为normal:向服务器发送一条消息时，服务器的回执消息，表明消息已收到
     * Type为chat,别人发给我的聊天消息，
     * Type为error，消息未发送成功
     *
     * @param message
     */
    private void handleMessage(Message message) {
        if (Message.Type.normal == message.getType()) {
            updateExistMessage(message);
        } else if (Message.Type.chat == message.getType()) {
            ackToServer(message);
            handleChatMessageByType(message);
        } else if (Message.Type.error == message.getType()) {
        }
    }

    /**
     * 处理Presence
     *
     * @param presence
     */
    private void handlePresence(Presence presence) {
        LogUtil.i(presence.toString());
    }

    private void handleIQ(IQ iq) {
        IQRouter.getInstance().routeIQ(iq);
    }

    /**
     * 更新数据库已存在的消息数据的状态
     *
     * @param message
     */
    private void updateExistMessage(Message message) {
        if (("receipt." + ConfigUtil.getXmppDomain()).equals(message.getFrom())) {
            TimeElement timeElement = TimeElement.from(message);

            GreenDaoManager.getInstance().updateMessageState(message.getStanzaId(),
                    MessageBean.State.SEND_SUCCESS, timeElement.getTime());
        }
    }

    private void ackToServer(Message message) {
        MarkAsReadIQ markAsReadIQ = new MarkAsReadIQ(message.getStanzaId());
        XmppManagerUtil.sendPacket(markAsReadIQ);
    }

    /**
     * 处理聊天消息
     * 1将{类型
     * 2 根据转换后的实体类得到的type来调用不同的子类具体处理
     *
     * @param message
     */
    private void handleChatMessageByType(Message message) {
        try {
            MessageBean messageBean = ConvertUtil.convertToMessageBean(message);
            int type = messageBean.getType();
            MessageProcess messageProcess = processMap.get(type);
            if (messageProcess == null) {
                switch (type) {
                    case MessageBean.Type.TEXT:
                        messageProcess = new TextMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.LOCATION:
                        messageProcess = new TextMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.VOICE:
                        messageProcess = new VoiceMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.PICTURE:
                        messageProcess = new PictureMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.OFFLINE_VIDEO:
                        messageProcess = new OfflineVideoMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.SEND_INVITE:
                        messageProcess = new InviteMessageProcess(App.getInstance());
                        break;
                    case MessageBean.Type.REPLY_INVITE:
                        messageProcess = new ReplyInviteMessageProcess(App.getInstance());
                        break;
                    default:
                        messageProcess = new UnknownTypeMessageProcess(App.getInstance());
                        break;
                }
            }

            processMap.put(type, messageProcess);
            messageProcess.processMessage(messageBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
