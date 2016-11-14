package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UpdateUserResponse extends BaseResponse {
    public static final int REFUSE_MODIFY = 1; //用户名、用户ID不允许修改
    public static final int MODIFIED_VALUE_INVALID = 2; //被修改的值无效

    public UserBean userBean;
}
