package com.juns.wechat.chat.xmpp.util;


import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.InviteMsg;
import com.juns.wechat.chat.bean.OfflineVideoMsg;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.chat.bean.TextMsg;
import com.juns.wechat.chat.bean.VoiceMsg;
import com.juns.wechat.chat.xmpp.XmppManagerImpl;
import com.juns.wechat.common.BASE64;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.database.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ThreadPoolUtil;


import org.jivesoftware.smack.packet.id.StanzaIdUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

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
    private static MessageDao messageDao = MessageDao.getInstance();
    private static BASE64 base64 = new BASE64();

    /**
     * 发送普通的文字消息
     *
     * @param content
     */
    public static void sendTextMsg(final String otherName, final String content) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                TextMsg textMsg = new TextMsg();
                textMsg.content = content;

                messageBean.setMsg(textMsg.toJson());
                messageBean.setOtherName(otherName);
                messageBean.setType(MsgType.MSG_TYPE_TEXT);
                messageBean.setTypeDesc(content);
                sendMsg(messageBean);

            }
        });

    }

    @SuppressWarnings("unchecked")
    public static void sendPictureMsg(final String otherName, final File file, final int width, final int height) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (file == null || !file.exists()) return;

                String imgName = file.getName();
                final PictureMsg pictureMsg = new PictureMsg();
                pictureMsg.imgName = imgName;
                pictureMsg.progress = 0;
                pictureMsg.width = width;
                pictureMsg.height = height;
                pictureMsg.size = (int) file.length();

                final MessageBean messageBean = new MessageBean();
                messageBean.setMsg(pictureMsg.toJson());
                messageBean.setOtherName(otherName);
                messageBean.setType(MsgType.MSG_TYPE_PICTURE);
                messageBean.setTypeDesc(MsgType.MSG_TYPE_PICTURE_DESC);
                completeMessageEntityInfo(messageBean);
                addMessageToDB(messageBean);

                if (!XmppManagerImpl.getInstance().login()) {
                    updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    return;
                }

                FileTransferManager fileTransferManager = new FileTransferManager();

                fileTransferManager.sendFile(file, otherName, new FileTransferManager.ProgressListener() {
                    @Override
                    public void progressUpdated(int progress) {
                        pictureMsg.progress = progress;
                        messageBean.setMsg(pictureMsg.toJson());
                        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", messageBean.getPacketId());
                        KeyValue keyValue = new KeyValue(MessageBean.MSG, messageBean.getMsg());
                        messageDao.update(whereBuilder, keyValue);
                    }

                    @Override
                    public void onFailed() {
                        updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    }

                    @Override
                    public void transferFinished() {
                        sendMsgDirect(messageBean);
                    }
                });
            }
        });
    }

    public static void sendOfflineVideoMsg(final String otherName, final File file) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (file == null || !file.exists()) return;

                String fileName = file.getName();
                final OfflineVideoMsg contentMsg = new OfflineVideoMsg();
                contentMsg.fileName = fileName;
                contentMsg.progress = 0;
                contentMsg.size = (int) file.length();

                final MessageBean messageBean = new MessageBean();
                messageBean.setMsg(contentMsg.toJson());
                messageBean.setOtherName(otherName);
                messageBean.setType(MsgType.MSG_TYPE_OFFLINE_VIDEO);
                messageBean.setTypeDesc(MsgType.MSG_TYPE_OFFLINE_VIDEO_DESC);
                completeMessageEntityInfo(messageBean);
                addMessageToDB(messageBean);

                if (!XmppManagerImpl.getInstance().login()) {
                    updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    return;
                }

                FileTransferManager fileTransferManager = new FileTransferManager();

                fileTransferManager.sendFile(file, otherName, new FileTransferManager.ProgressListener() {
                    @Override
                    public void progressUpdated(int progress) {
                        contentMsg.progress = progress;
                        messageBean.setMsg(contentMsg.toJson());
                        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", messageBean.getPacketId());
                        KeyValue keyValue = new KeyValue(MessageBean.MSG, messageBean.getMsg());
                        messageDao.update(whereBuilder, keyValue);
                    }

                    @Override
                    public void transferFinished() {
                        sendMsgDirect(messageBean);
                    }

                    @Override
                    public void onFailed() {
                        updateMessageState(messageBean.getPacketId(), MessageBean.State.SEND_FAILED.value);
                    }
                });
            }
        });
    }

    public static void sendVoiceMsg(final String otherName, final int seconds, final String filePath) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                VoiceMsg voiceMsg = new VoiceMsg();
                voiceMsg.seconds = seconds;
                voiceMsg.fileName = new File(filePath).getName();
                voiceMsg.encodeStr = new MsgCode().encode(filePath);

                messageBean.setMsg(voiceMsg.toJson());
                messageBean.setOtherName(otherName);
                messageBean.setType(MsgType.MSG_TYPE_VOICE);
                messageBean.setTypeDesc(MsgType.MSG_TYPE_VOICE_DESC);
                sendMsg(messageBean);
            }
        });
    }

    /**
     * 发送添加好友消息
     *
     * @param reason
     */
    public static void sendInviteMsg(final String otherName, final String reason) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                InviteMsg inviteMsg = new InviteMsg();
                inviteMsg.userName = AccountManager.getInstance().getUser().getShowName();
                inviteMsg.reason = reason;
                try {
                    messageBean.setMsg(inviteMsg.toJson());
                    messageBean.setOtherName(otherName);
                    messageBean.setType(MsgType.MSG_TYPE_SEND_INVITE);
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
    public static void sendReplyInviteMsg(final String otherName, final int reply, final String reason) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                InviteMsg inviteMsg = new InviteMsg();
                inviteMsg.userName = AccountManager.getInstance().getUser().getShowName();
                inviteMsg.reason = reason;
                inviteMsg.reply = reply;
                try {
                    messageBean.setMsg(inviteMsg.toJson());
                    messageBean.setOtherName(otherName);
                    messageBean.setType(MsgType.MSG_TYPE_REPLY_INVITE);
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

    public static void completeMessageEntityInfo(MessageBean message) {
        String myselfName = AccountManager.getInstance().getUserName();
        message.setMyselfName(myselfName);
        String packetId = StanzaIdUtil.newStanzaId();
        message.setPacketId(packetId);
        message.setDate(new Date());
        message.setState(MessageBean.State.NEW.value);
        message.setDirection(MessageBean.Direction.OUTGOING.value);
    }

    /**
     * 将消息存进本地数据库
     *
     * @return
     */
    private static void addMessageToDB(MessageBean messageBean) {
        messageDao.save(messageBean);
    }

    public static void sendMsgDirect(MessageBean message) {
        boolean send = XmppManagerImpl.getInstance().sendMessage(message);
        if (!send) {  //如果未成功发送,
            updateMessageState(message.getPacketId(), MessageBean.State.SEND_FAILED.value);
        }
    }

    /**
     * 更新消息状态，只是在消息未成功发送时调用，因为消息成功发送后，服务器会发送一条回执消息，那时可以将消息状态置为发送成功
     *
     * @param state
     */
    private static void updateMessageState(String packetId, int state) {
        messageDao.updateMessageState(packetId, state, null);
    }

}
