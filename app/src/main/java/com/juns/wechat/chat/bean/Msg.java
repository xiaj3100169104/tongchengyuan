package com.juns.wechat.chat.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juns.wechat.config.MsgType;

import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/3.
 */
public abstract class Msg {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public String toJson(){
        return gson.toJson(this);
    }

    public abstract JSONObject toSendJsonObject();

    public static Msg fromJson(String json, int type){
        Msg msg = null;
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                msg = gson.fromJson(json, TextMsg.class);
                break;
            case MsgType.MSG_TYPE_PICTURE:
                msg = gson.fromJson(json, PictureMsg.class);
                break;
            case MsgType.MSG_TYPE_VOICE:
                msg = gson.fromJson(json, VoiceMsg.class);
                break;
            case MsgType.MSG_TYPE_SEND_INVITE:
            case MsgType.MSG_TYPE_REPLY_INVITE:
                msg = gson.fromJson(json, InviteMsg.class);
                break;
            default:
                break;
        }
        return msg;
    }
}
