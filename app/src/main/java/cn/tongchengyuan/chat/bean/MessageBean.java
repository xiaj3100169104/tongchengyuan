package cn.tongchengyuan.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/*******************************************************
 * Created by 王宗文 on 2015/11/16
 *******************************************************/

@Entity
public class MessageBean {

    @Id
    public String packetId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    public String myUserId; //自己;
    public String otherUserId; //对方;
    public String msg; //对应表中msg字段
    public int type; //消息类型
    public Date date; //发送消息时间
    public int direction; // 发出去还是收到了消息
    public int state; //判断消息是否已发送或者已读
    public int flag; //判断消息是否有效
    @Transient
    private MsgData msgData; //msg字段对应的对象

    public MessageBean() {
    }

    @Generated(hash = 914378335)
    public MessageBean(String packetId, String myUserId, String otherUserId,
                       String msg, int type, Date date, int direction, int state, int flag) {
        this.packetId = packetId;
        this.myUserId = myUserId;
        this.otherUserId = otherUserId;
        this.msg = msg;
        this.type = type;
        this.date = date;
        this.direction = direction;
        this.state = state;
        this.flag = flag;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getMsg() {
        return msg;
    }

    //应先设置消息类型，不然会有强制类型转换报错，除非不加类型转换
    public void setMsg(String msg) {
        this.msg = msg;
        //msgObj = Msg.fromJson(msg, type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTypeDesc() {
        return MsgData.getTypeDesc(type, getMsgDataObj());
    }

    public MsgData getMsgDataObj() {
        if (msgData == null) {
            msgData = MsgData.fromJson(msg, type);
        }
        return msgData;
    }

    public String toSendJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MSG", getMsgDataObj().toSendJsonObject());
        jsonObject.put("TYPE", type);
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                ", packetId='" + packetId + '\'' +
                ", myUserId=" + myUserId +
                ", otherUserId=" + otherUserId +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", direction=" + direction +
                ", state=" + state +
                ", flag=" + flag +
                ", msgObj=" + msgData +
                '}';
    }


    public String getMyUserId() {
        return this.myUserId;
    }


    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }


    public String getOtherUserId() {
        return this.otherUserId;
    }


    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public final class Flag {
        public static final int VALID = 0;
        public static final int INVALID = -1;
    }

    public final class Direction {
        public static final int INCOMING = 0;
        public static final int OUTGOING = 1;
    }

    public final class State {
        public static final int NEW = 0;
        public static final int SEND_SUCCESS = 1;
        public static final int SEND_FAILED = 2;
        public static final int READ = 3;
    }

    public final class Type {
        //大于500的消息不会在消息界面展示
        public final static int SEND_INVITE = 500; //发送邀请消息成为好友。
        public final static int REPLY_INVITE = 501; // 是否同意添加好友请求消息

        public final static int TEXT = 0;                  //文字
        public final static int VOICE = 1; //语音
        public final static int PICTURE = 2;//图片
        public final static int OFFLINE_VIDEO = 3;     //视频
        public final static int LOCATION = 4;     //地理位置

    }

    public final class TypeDesc {
        public static final String VOICE = "[语音]";
        public static final String PICTURE = "[图片]";
        public static final String OFFLINE_VIDEO = "[视频]";
        public static final String LOCATION = "[位置]";

    }

}
