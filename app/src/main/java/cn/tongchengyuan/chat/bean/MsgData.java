package cn.tongchengyuan.chat.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/3.
 */
public abstract class MsgData {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public String toJson(){
        return gson.toJson(this);
    }

    public abstract JSONObject toSendJsonObject()  throws JSONException;

    public static MsgData fromJson(String json, int type){
        MsgData msgData = null;
        switch (type){
            case MessageBean.Type.TEXT:
                msgData = gson.fromJson(json, TextMsgData.class);
                break;
            case MessageBean.Type.PICTURE:
                msgData = gson.fromJson(json, PictureMsgData.class);
                break;
            case MessageBean.Type.VOICE:
                msgData = gson.fromJson(json, VoiceMsgData.class);
                break;
            case MessageBean.Type.OFFLINE_VIDEO:
                msgData = gson.fromJson(json, OfflineVideoMsgData.class);
                break;
            case MessageBean.Type.SEND_INVITE:
                break;
            case MessageBean.Type.REPLY_INVITE:
                msgData = gson.fromJson(json, InviteMsgData.class);
                break;
            case MessageBean.Type.LOCATION:
                msgData = gson.fromJson(json, LocationMsgData.class);
                break;
            default:
                break;
        }
        return msgData;
    }

    public static String getTypeDesc(int type, MsgData msgDataObj){
        String msg = null;
        switch (type){
            case MessageBean.Type.TEXT:
                TextMsgData textMsg = (TextMsgData) msgDataObj;
                msg = textMsg.content;
                break;
            case MessageBean.Type.PICTURE:
                msg = MessageBean.TypeDesc.PICTURE;
                break;
            case MessageBean.Type.VOICE:
                msg = MessageBean.TypeDesc.VOICE;
                break;
            case MessageBean.Type.OFFLINE_VIDEO:
                msg = MessageBean.TypeDesc.OFFLINE_VIDEO;
                break;
            case MessageBean.Type.LOCATION:
                msg = MessageBean.TypeDesc.LOCATION;
                break;
            default:
                msg = "";
                break;
        }
        return msg;
    }
}
