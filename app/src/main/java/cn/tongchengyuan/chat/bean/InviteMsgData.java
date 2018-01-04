package cn.tongchengyuan.chat.bean;


import org.json.JSONException;
import org.json.JSONObject;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：普通的文本消息的msg字段转换后对应的类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/24
 *******************************************************/
public class InviteMsgData extends MsgData {
    public static final String USERNAME = "userName";
    public static final String REASON = "reason";
    public static final String REPLY = "reply";

    public String userName;
    public String reason;
    public int reply;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(USERNAME, userName);
            jsonObject.put(REASON, reason);
            jsonObject.put(REPLY, reply);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public enum Reply{
        ACCEPT(1), REJECT(2);
        public int value;

        Reply(int value){
            this.value = value;
        }
    }
}
