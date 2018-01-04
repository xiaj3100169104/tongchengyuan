package cn.tongchengyuan.chat.xmpp;

import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by 王宗文 on 2016/7/30.
 */
public interface XmppGroup {
    boolean createGroup(String roomName, String nickName);
    boolean joinGroup(String roomName, String nickName);
    boolean createOrJoinGroup(String roomName, String nickName);
    boolean inviteUser(String roomName, String jid, String reason);
    MultiUserChat getMultiUserChat(String roomName);
}
