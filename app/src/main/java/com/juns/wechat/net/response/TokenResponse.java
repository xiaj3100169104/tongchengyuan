package com.juns.wechat.net.response;

import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/12.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TokenResponse extends BaseResponse{

    public String token;
}
