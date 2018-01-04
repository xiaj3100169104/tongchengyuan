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
public class TextMsgData extends MsgData {
    public static final String CONTENT = "content";

    public String content;

    @Override
    public JSONObject toSendJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CONTENT, content);
        return jsonObject;
    }
}
