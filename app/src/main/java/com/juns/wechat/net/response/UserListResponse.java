package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by 王宗文 on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UserListResponse extends BaseResponse {
    public List<UserBean> userBeans;
}
