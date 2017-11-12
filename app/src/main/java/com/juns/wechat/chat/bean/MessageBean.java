package com.juns.wechat.chat.bean;

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
    private Msg msgObj; //msg字段对应的对象

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




    public String toSendJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MSG", getMsgObj().toSendJsonObject());
        jsonObject.put("TYPE", type);
        return jsonObject.toString();
    }

    public enum State {
        NEW(0), SEND_SUCCESS(1), SEND_FAILED(2), READ(3);

        public int value;

        State(int value) {
            this.value = value;
        }
    }

    public enum Direction {
        INCOMING(0), OUTGOING(1);
        public int value;

        Direction(int value) {
            this.value = value;
        }
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
        return Msg.getTypeDesc(type, getMsgObj());
    }

    public Msg getMsgObj() {
        if (msgObj == null) {
            msgObj = Msg.fromJson(msg, type);
        }
        return msgObj;
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
                ", msgObj=" + msgObj +
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
}
