package com.juns.wechat.net.callback;

import com.juns.wechat.net.JsonResponseParser;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.DynamicResponse;
import com.juns.wechat.util.LogUtil;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by 王者 on 2016/12/13.
 */

@HttpResponse(parser = JsonResponseParser.class)
public class AddDynamicCallBack extends BaseCallBack<BaseResponse>{
    @Override
    protected void handleResponse(BaseResponse result) {
        LogUtil.i("result:" + result);
    }
}
