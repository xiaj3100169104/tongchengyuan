package com.juns.wechat.config;

/**
 * Created by 王者 on 2016/7/21
 */
public class MsgType {
    public final static int MSG_TYPE_TEXT    = 200;                  //文字
    public final static int MSG_TYPE_VOICE   = MSG_TYPE_TEXT+1; //语音
    public final static int MSG_TYPE_PICTURE = MSG_TYPE_VOICE+1;//图片
    public final static int MSG_TYPE_FILE    = MSG_TYPE_PICTURE +1;//文件
    public final static int MSG_TYPE_OFFLINE_VIDEO = MSG_TYPE_FILE+1;     //视频
    public final static int MSG_TYPE_INFO    = MSG_TYPE_OFFLINE_VIDEO +1;     //资讯类消息
    public final static int MSG_TYPE_NAME_CARD   = MSG_TYPE_INFO+1;     //名片类
    public final static int MSG_TYPE_LOCATION    = MSG_TYPE_NAME_CARD+1;     //地理位置
    public final static int MSG_TYPE_PAY       = MSG_TYPE_LOCATION+1;     //支付
    public final static int MSG_TYPE_MEETING   = MSG_TYPE_PAY+1;     //会议室请求
    public final static int MSG_TYPE_FACE      = MSG_TYPE_MEETING+1;     //表情
    public final static int MSG_TYPE_TRANSFER  = MSG_TYPE_FACE+1;     //转发消息

    //大于500的消息不会在消息界面展示
    public final static int MSG_TYPE_SEND_INVITE = 500; //发送邀请消息成为好友。
    public final static int MSG_TYPE_REPLY_INVITE = MSG_TYPE_SEND_INVITE + 1; // 是否同意添加好友请求消息

    public static final String MSG_TYPE_VOICE_DESC = "[语音]";
    public static final String MSG_TYPE_PICTURE_DESC = "[图片]";
    public static final String MSG_TYPE_OFFLINE_VIDEO_DESC = "[视频]";
    public static final String MSG_TYPE_LOCATION_DESC = "[位置]";

}
