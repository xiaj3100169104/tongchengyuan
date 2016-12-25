package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/7.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BaseResponse<T> {
    public static final int SUCCESS = 0;

    public static final int SERVER_ERROR = 500; //服务器异常
    public static final int PARAM_MISS = 501; //传递参数有错误
    public static final int TOKEN_INVALID = 1000; //TOKEN过期
    public static final int TOKEN_EXPIRED = 1001; //TOKEN无效

    public int code;
    public String msg;
    public T data;

    @HttpResponse(parser = JsonResponseParser.class)
    public class LoginResponse extends BaseResponse {
        public static final int FAILED = 1;

        public String token;
        public UserBean userBean;
    }

    @HttpResponse(parser = JsonResponseParser.class)
    public class RegisterResponse extends BaseResponse {
        public String errField;
    }

    @HttpResponse(parser = JsonResponseParser.class)
    public class QueryUserResponse extends BaseResponse{
        public UserBean userBean;
    }
}
