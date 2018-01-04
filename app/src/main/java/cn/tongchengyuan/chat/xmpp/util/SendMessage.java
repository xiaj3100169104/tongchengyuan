package cn.tongchengyuan.chat.xmpp.util;


import android.widget.Toast;

import cn.tongchengyuan.app.App;
import cn.tongchengyuan.chat.bean.InviteMsgData;
import cn.tongchengyuan.chat.bean.LocationMsgData;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.chat.bean.PictureMsgData;
import cn.tongchengyuan.chat.bean.VoiceMsgData;
import cn.tongchengyuan.common.BASE64;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.util.ThreadPoolUtil;
import cn.tongchengyuan.chat.bean.OfflineVideoMsgData;
import cn.tongchengyuan.chat.bean.TextMsgData;
import cn.tongchengyuan.chat.xmpp.XmppManagerImpl;
import cn.tongchengyuan.util.ToastUtil;
import com.style.constant.FileConfig;
import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.manager.ToastManager;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.io.File;
import java.util.Date;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：发送消息的工具类，可以发送不同类型的消息
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class SendMessage {
    private static GreenDaoManager messageDao = GreenDaoManager.getInstance();
    private static BASE64 base64 = new BASE64();

    /**
     * 发送普通的文字消息
     *
     * @param content
     */
    public static void sendTextMsg(final String otherUserId, final String content) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                TextMsgData textMsg = new TextMsgData();
                textMsg.content = content;
                messageBean.setMsg(textMsg.toJson());
                messageBean.setOtherUserId(otherUserId);
                messageBean.setType(MessageBean.Type.TEXT);
                sendMsg(messageBean);

            }
        });
    }

    /**
     * 发送地理位置消息
     */
    public static void sendLocationMsg(final String otherUserId, final double latitude, final double longitude, final String address) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                LocationMsgData msg = new LocationMsgData();
                msg.latitude = latitude;
                msg.longitude = longitude;
                msg.address = address;
                messageBean.setMsg(msg.toJson());
                messageBean.setOtherUserId(otherUserId);
                messageBean.setType(MessageBean.Type.LOCATION);
                sendMsg(messageBean);

            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void sendPictureMsg(final String otherUserId, final String path, final int width, final int height) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                if (!file.exists()) {
                    ToastUtil.showToast("文件已被删除", Toast.LENGTH_SHORT);
                    return;
                }

                String imgName = file.getName();
                final PictureMsgData pictureMsg = new PictureMsgData();
                pictureMsg.imgName = imgName;
                pictureMsg.progress = 0;
                pictureMsg.width = width;
                pictureMsg.height = height;
                pictureMsg.size = (int) file.length();

                final MessageBean messageBean = new MessageBean();
                messageBean.setMsg(pictureMsg.toJson());
                messageBean.setOtherUserId(otherUserId);
                messageBean.setType(MessageBean.Type.PICTURE);
                completeMessageEntityInfo(messageBean);
                addMessageToDB(messageBean);
                sendMsgDirect(messageBean);
                //uploadPicture(messageBean);
            }
        });
    }

    private static void uploadPicture(final MessageBean messageBean) {
        final PictureMsgData pictureMsg = (PictureMsgData) messageBean.getMsgDataObj();
        String filePath = FileConfig.DIR_CACHE + "/" + pictureMsg.imgName;
        File file = new File(filePath);
        if (!file.exists()) {
            ToastManager.showToast(App.getInstance(), "文件已被删除");
            return;
        }
        if (!XmppManagerImpl.getInstance().login()) {
            updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED);
            return;
        }

        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.sendFile(file, messageBean.getOtherUserId(), new FileTransferManager.ProgressListener() {
            @Override
            public void progressUpdated(int progress) {
                pictureMsg.progress = progress;
            }

            @Override
            public void onFailed() {
                updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED);
            }

            @Override
            public void transferFinished() {
                sendMsgDirect(messageBean);
            }
        });
    }

    public static void sendOfflineVideoMsg(final String otherUserId, final File file, final int width, final int height) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (file == null || !file.exists()) return;

                String fileName = file.getName();
                final OfflineVideoMsgData contentMsg = new OfflineVideoMsgData();
                contentMsg.fileName = fileName;
                contentMsg.progress = 0;
                contentMsg.width = width;
                contentMsg.height = height;
                contentMsg.size = (int) file.length();

                final MessageBean messageBean = new MessageBean();
                messageBean.setMsg(contentMsg.toJson());
                messageBean.setOtherUserId(otherUserId);
                messageBean.setType(MessageBean.Type.OFFLINE_VIDEO);
                completeMessageEntityInfo(messageBean);
                addMessageToDB(messageBean);
                sendMsgDirect(messageBean);

                /*if (!XmppManagerImpl.getInstance().login()) {
                    updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    return;
                }

                FileTransferManager fileTransferManager = new FileTransferManager();

                fileTransferManager.sendFile(file, otherUserId, new FileTransferManager.ProgressListener() {
                    @Override
                    public void progressUpdated(int progress) {
                    }

                    @Override
                    public void transferFinished() {
                        sendMsgDirect(messageBean);
                    }

                    @Override
                    public void onFailed() {
                        updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    }
                });*/
            }
        });
    }

    public static void sendVoiceMsg(final String otherUserId, final int seconds, final String filePath) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                VoiceMsgData voiceMsg = new VoiceMsgData();
                voiceMsg.seconds = seconds;
                voiceMsg.fileName = new File(filePath).getName();
                voiceMsg.encodeStr = new MsgCode().encode(filePath);

                messageBean.setMsg(voiceMsg.toJson());
                messageBean.setOtherUserId(otherUserId);
                messageBean.setType(MessageBean.Type.VOICE);
                sendMsg(messageBean);
            }
        });
    }

    /**
     * 发送添加好友消息
     *
     * @param reason
     */
    public static void sendInviteMsg(final String otherUserId, final String reason) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                InviteMsgData inviteMsg = new InviteMsgData();
                inviteMsg.userName = AccountManager.getInstance().getUser().getShowName();
                inviteMsg.reason = reason;
                try {
                    messageBean.setMsg(inviteMsg.toJson());
                    messageBean.setOtherUserId(otherUserId);
                    messageBean.setType(MessageBean.Type.SEND_INVITE);
                    sendMsg(messageBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 回复添加好友消息
     */
    public static void sendReplyInviteMsg(final String otherUserId, final int reply, final String reason) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                InviteMsgData inviteMsg = new InviteMsgData();
                inviteMsg.userName = AccountManager.getInstance().getUser().getShowName();
                inviteMsg.reason = reason;
                inviteMsg.reply = reply;
                try {
                    messageBean.setMsg(inviteMsg.toJson());
                    messageBean.setOtherUserId(otherUserId);
                    messageBean.setType(MessageBean.Type.REPLY_INVITE);
                    sendMsg(messageBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 发送消息分两步：1.将消息发出去
     * 2.将消息存入本地数据库
     *
     * @param message
     */
    public static void sendMsg(MessageBean message) {
        completeMessageEntityInfo(message);
        addMessageToDB(message);
        sendMsgDirect(message);
    }

    public static void reSendMsg(final MessageBean message) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                switch (message.getType()) {
                    case MessageBean.Type.PICTURE:
                        //uploadPicture(message);
                        break;
                    case MessageBean.Type.OFFLINE_VIDEO:
                        break;
                    default:
                        sendMsgDirect(message);
                        break;
                }
            }
        });
    }

    public static void completeMessageEntityInfo(MessageBean message) {
        String myselfName = AccountManager.getInstance().getUserId();
        message.setMyUserId(myselfName);
        String packetId = StanzaIdUtil.newStanzaId();
        message.setPacketId(packetId);
        message.setDate(new Date());
        message.setState(MessageBean.State.NEW);
        message.setDirection(MessageBean.Direction.OUTGOING);
    }

    /**
     * 将消息存进本地数据库
     *
     * @return
     */
    private static void addMessageToDB(MessageBean messageBean) {
        messageBean.state = MessageBean.State.SEND_SUCCESS;
        GreenDaoManager.getInstance().save(messageBean);
        EventManager.getDefault().post(EventCode.BEFORE_SEND_SUCCESS, messageBean);
        EventManager.getDefault().post(EventCode.REFRESH_CONVERSATION_LIST, messageBean);

    }

    public static void sendMsgDirect(MessageBean message) {
       /* boolean send = XmppManagerImpl.getInstance().sendMessage(message);
        if (!send) {  //如果未成功发送,
            updateMessageState(message.getPacketId(), MessageBean.State.SEND_FAILED.value);
        }*/
    }

    /**
     * 更新消息状态，只是在消息未成功发送时调用，因为消息成功发送后，服务器会发送一条回执消息，那时可以将消息状态置为发送成功
     *
     * @param state
     */
    private static void updateMessageState(String packetId, int state) {
        messageDao.updateMessageState(packetId, state, new Date());
    }

}
