package com.juns.wechat.chat.bean;


import org.json.JSONException;
import org.json.JSONObject;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：普通的文本消息的msg字段转换后对应的类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/24
 *******************************************************/
public class TextMsg extends Msg{
    public static final String CONTENT = "content";

    public String content;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
