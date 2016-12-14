package com.juns.wechat.net.response;

import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王者 on 2016/12/13.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class DynamicResponse extends BaseResponse{
}
