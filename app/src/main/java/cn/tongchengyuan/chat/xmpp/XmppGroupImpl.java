package cn.tongchengyuan.chat.xmpp;

import cn.tongchengyuan.chat.xmpp.event.XmppEvent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCInitialPresence;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.xdata.Form;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/7/30.
 */
public class XmppGroupImpl implements XmppGroup {

    private MultiUserChatManager multiUserChatManager;

    private static XmppGroupImpl mInatance;

    public synchronized static XmppGroupImpl getInstance(){
        if(mInatance == null){
            mInatance = new XmppGroupImpl();
        }
        return mInatance;
    }

    private XmppGroupImpl(){
        multiUserChatManager = MultiUserChatManager.getInstanceFor(XmppConnUtil.getXmppConnection());
    }

    @Override
    public boolean createGroup(String roomName, String nickName) {
        MultiUserChat multiUserChat = getMultiUserChat(roomName);
        try {
            multiUserChat.create(nickName);

            configRoom(roomName);

            XmppEvent xmppEvent = new XmppEvent(XmppEvent.CREATE_GROUP_SUCCESS, null);
            EventBus.getDefault().post(xmppEvent);
            return true;
        } catch (XMPPException.XMPPErrorException | SmackException e) {
            XmppEvent xmppEvent = new XmppEvent(XmppEvent.CREATE_GROUP_FAILED, e);
            EventBus.getDefault().post(xmppEvent);
            return false;
        }
    }

    @Override
    public boolean joinGroup(String roomName, String nickName) {
        MultiUserChat multiUserChat = getMultiUserChat(roomName);
        try {
            if(!multiUserChat.isJoined()){
                multiUserChat.join(nickName);
            }
            XmppEvent xmppEvent = new XmppEvent(XmppEvent.JOIN_CROUP_SUCCESS, null);
            EventBus.getDefault().post(xmppEvent);
            return true;
        } catch (SmackException | XMPPException.XMPPErrorException e){
            XmppEvent xmppEvent = new XmppEvent(XmppEvent.JOIN_GROUP_FAILED, e);
            EventBus.getDefault().post(xmppEvent);
            return false;
        }
    }

    @Override
    public boolean createOrJoinGroup(String roomName, String nickname) {
        StringUtils.requireNotNullOrEmpty(nickname, "Nickname must not be null or blank.");

        Presence joinPresence = new Presence(Presence.Type.available);
        joinPresence.setTo(roomName + "@conference.wangzhe/" + nickname);
        MUCInitialPresence mucInitialPresence = new MUCInitialPresence();
        joinPresence.addExtension(mucInitialPresence);

        StanzaFilter responseFilter = new AndFilter(FromMatchesFilter.createFull(roomName + "@conference.wangzhe/"
                + nickname), new StanzaTypeFilter(Presence.class));

        Presence presence;
        try {
            presence = XmppConnUtil.getXmppConnection().createPacketCollectorAndSend(responseFilter, joinPresence).nextResultOrThrow(5000);
            MUCUser mucUser = MUCUser.from(presence);

            for(MUCUser.Status status : mucUser.getStatus()){
                if(status.getCode() == MUCUser.Status.ROOM_CREATED_201.getCode()){
                    configRoom(roomName);
                    XmppEvent xmppEvent = new XmppEvent(XmppEvent.CREATE_GROUP_SUCCESS, null);
                    EventBus.getDefault().post(xmppEvent);
                    return true;
                }
            }
            //房间已经存在
            return joinGroup(roomName, nickname);

        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException  |
                SmackException.NotConnectedException e) {
            XmppEvent xmppEvent = new XmppEvent(XmppEvent.CREATE_GROUP_FAILED, e);
            EventBus.getDefault().post(xmppEvent);
            return false;
        }
    }

    @Override
    public boolean inviteUser(String roomName, String jid, String reason) {
        MultiUserChat multiUserChat = getMultiUserChat(roomName);
        try {
            multiUserChat.invite(jid, reason);
            return true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MultiUserChat getMultiUserChat(String roomName) {
        return multiUserChatManager.getMultiUserChat(roomName + "@conference.wangzhe");
    }

    private void configRoom(String roomName) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        MultiUserChat multiUserChat = getMultiUserChat(roomName);
        Form form = multiUserChat.getConfigurationForm();

        Form answerForm = form.createAnswerForm();
        answerForm.setAnswer("muc#roomconfig_roomdesc", "村长助理专用房间");
        List<String> maxNum = new ArrayList<>();
        maxNum.add("50");
        answerForm.setAnswer("muc#roomconfig_maxusers", maxNum);
        answerForm.setAnswer("muc#roomconfig_persistentroom", true);
        multiUserChat.sendConfigurationForm(answerForm);
    }
}
