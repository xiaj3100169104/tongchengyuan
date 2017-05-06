package com.juns.wechat.chat.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juns.wechat.config.MsgType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/3.
 */
public abstract class Msg {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public String toJson(){
        return gson.toJson(this);
    }

    public abstract JSONObject toSendJsonObject()  throws JSONException;

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
            case MsgType.MSG_TYPE_OFFLINE_VIDEO:
                msg = gson.fromJson(json, OfflineVideoMsg.class);
                break;
            case MsgType.MSG_TYPE_SEND_INVITE:
                break;
            case MsgType.MSG_TYPE_REPLY_INVITE:
                msg = gson.fromJson(json, InviteMsg.class);
                break;
            case MsgType.MSG_TYPE_LOCATION:
                msg = gson.fromJson(json, LocationMsg.class);
                break;
            default:
                break;
        }
        return msg;
    }

    public static String getTypeDesc(int type, Msg msgObj){
        String msg = null;
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                TextMsg textMsg = (TextMsg) msgObj;
                msg = textMsg.content;
                break;
            case MsgType.MSG_TYPE_PICTURE:
                msg = MsgType.MSG_TYPE_DESC_PICTURE;
                break;
            case MsgType.MSG_TYPE_VOICE:
                msg = MsgType.MSG_TYPE_DESC_VOICE;
                break;
            case MsgType.MSG_TYPE_OFFLINE_VIDEO:
                msg = MsgType.MSG_TYPE_DESC_OFFLINE_VIDEO;
                break;
            case MsgType.MSG_TYPE_LOCATION:
                msg = MsgType.MSG_TYPE_DESC_LOCATION;
                break;
            default:
                msg = "";
                break;
        }
        return msg;
    }
}
