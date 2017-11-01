package com.juns.wechat.chat.bean;

import com.juns.wechat.chat.bean.Msg;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.database.ChatTable;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/*******************************************************
 * Created by 王宗文 on 2015/11/16
 *******************************************************/

@Table(name = ChatTable.TABLE_NAME, onCreated = ChatTable.CREATE_INDEX)
public class MessageBean {
    public static final String ID = "id";
    public static final String MYSELF_NAME = "myselfName";
    public static final String OTHER_NAME = "otherName";
    public static final String MSG = "msg";
    public static final String TYPE = "type";
    public static final String PACKET_ID = "packetId";
    public static final String DATE = "date";
    public static final String DIRECTION = "direction"; //消息方向，是发出去还是接受到的
    public static final String STATE = "state";
    public static final String FLAG = "flag";

    @Column(name = ID, isId = true)
    private int id; //主键，子增长
    @Column(name = MYSELF_NAME)
    private String myselfName; //自己;
    @Column(name = OTHER_NAME)
    private String otherName; //对方;
    @Column(name = MSG)
    private String msg; //对应表中msg字段
    @Column(name = TYPE)
    private int type; //消息类型
    @Column(name = PACKET_ID)
    private String packetId; //每一条消息在网络上发送时都表现为一个消息包，这个packetId与每条消息的消息Id是一致的
    @Column(name = DATE)
    private Date date; //发送消息时间
    @Column(name = DIRECTION)
    private int direction; // 发出去还是收到了消息
    @Column(name = STATE)
    private int state; //判断消息是否已发送或者已读
    @Column(name = FLAG)
    private int flag; //判断消息是否有效
    private Msg msgObj; //msg字段对应的对象

    public MessageBean() {
    }


    public String toSendJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MSG, getMsgObj().toSendJsonObject());
        jsonObject.put(TYPE, type);
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
                "id=" + id +
                ", myselfName='" + myselfName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", packetId='" + packetId + '\'' +
                ", date=" + date +
                ", direction=" + direction +
                ", state=" + state +
                ", flag=" + flag +
                ", msgObj=" + msgObj +
                '}';
    }
}
