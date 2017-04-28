package com.juns.wechat.chat.bean;

import com.juns.wechat.database.ChatTable;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/*******************************************************
 * Created by 王宗文 on 2015/11/16
 *******************************************************/

public class MessageObject extends RealmObject{
    public static final String ID = "id";
    public static final String MYSELF_NAME = "myselfName";
    public static final String OTHER_NAME = "otherName";
    public static final String MSG = "msg";
    public static final String TYPE = "type";
    public static final String TYPE_DESC = "typeDesc";
    public static final String PACKET_ID = "packetId";
    public static final String DATE  = "date";
    public static final String DIRECTION = "direction"; //消息方向，是发出去还是接受到的
    public static final String STATE = "state";
    public static final String FLAG = "flag";

    @PrimaryKey
    private int id; //主键，子增长
    private String myselfName; //自己;
    private String otherName; //对方;
    private String msg; //对应表中msg字段
    private int type; //消息类型
    private String typeDesc;
    private String packetId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    private Date date; //发送消息时间
    private int direction; // 发出去还是收到了消息
    private int state; //判断消息是否已发送或者已读
    private int flag; //判断消息是否有效
    @Ignore
    private Msg msgObj; //msg字段对应的对象

    public MessageObject() { }

    public MessageObject(MessageBean messageBean){
        this.id = messageBean.getId();
        this.myselfName = messageBean.getMyselfName();
        this.otherName = messageBean.getOtherName();
        this.msg = messageBean.getMsg();
        this.type = messageBean.getType();
        this.typeDesc = messageBean.getTypeDesc();
        this.packetId = messageBean.getPacketId();
        this.date = messageBean.getDate();
        this.direction = messageBean.getDirection();
        this.date = messageBean.getDate();
        this.flag = messageBean.getFlag();
    }


    public String toSendJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            if(msgObj == null){
                msgObj = Msg.fromJson(msg, type);
            }
            jsonObject.put(MSG, msgObj.toSendJsonObject());
            jsonObject.put(TYPE, type);
            jsonObject.put(TYPE_DESC, typeDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public enum State{
        NEW(0), SEND_SUCCESS(1), SEND_FAILED(2), READ(3);

        public int value;
        State(int value){
            this.value = value;
        }
    }

    public enum Direction{
        INCOMING(0), OUTGOING(1);
        public int value;
        Direction(int value){
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "[id: " + id + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMyselfName() {
        return myselfName;
    }

    public void setMyselfName(String myselfName) {
        this.myselfName = myselfName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        msgObj = Msg.fromJson(msg, type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
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

    public Msg getMsgObj() {
        if(msgObj == null){
            msgObj = Msg.fromJson(msg, type);
        }
        return msgObj;
    }

    public void setMsgObj(Msg msgObj) {
        this.msgObj = msgObj;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + id;
        result = 37 * result + type;
        result = 37 * result + direction;
        result += myselfName.hashCode();
        result += otherName.hashCode();
        result += msg.hashCode();
        result += date.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object){
            return true;
        }
        if(object == null){
            return false;
        }
        if(getClass() != object.getClass()){
            return false;
        }
        MessageObject messageObject = (MessageObject) object;
        return (id == messageObject.getId() && myselfName.equals(messageObject.myselfName) && otherName.equals(messageObject.otherName)
                && msg.equals(messageObject.msg) && type == messageObject.type && packetId.equals(messageObject.packetId)
                && date.equals(messageObject.packetId) && direction == messageObject.direction
                && flag == messageObject.flag);
    }
}
