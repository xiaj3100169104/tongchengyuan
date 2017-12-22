package com.juns.wechat.chat.xmpp.util;


import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.config.ConfigUtil;
import com.juns.wechat.chat.xmpp.extensionelement.TimeElement;

import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/2.
 */
public class ConvertUtil {

    public static MessageBean convertToMessageBean(Message message) throws JSONException {
        MessageBean messageBean = new MessageBean();
        messageBean.setMyUserId(ConfigUtil.getUserName(message.getTo()));
        messageBean.setOtherUserId(ConfigUtil.getUserName(message.getFrom()));
        TimeElement time = TimeElement.from(message);
        messageBean.setDate(time.getTime());
        messageBean.setDirection(MessageBean.Direction.INCOMING);
        messageBean.setPacketId(message.getStanzaId());

        JSONObject jsonObject = new JSONObject(message.getBody());
        String msg = jsonObject.optString("MSG");
        int type = jsonObject.optInt("TYPE");
        messageBean.setMsg(msg);
        messageBean.setType(type);
        return messageBean;
    }
}
