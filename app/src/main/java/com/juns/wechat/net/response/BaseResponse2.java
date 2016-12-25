package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/7.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BaseResponse2 {

    public int code;
    public String msg;
    public String data;

}
