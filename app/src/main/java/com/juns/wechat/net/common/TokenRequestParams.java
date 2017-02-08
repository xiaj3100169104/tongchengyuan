package com.juns.wechat.net.common;

import com.juns.wechat.manager.AccountManager;

import org.xutils.http.RequestParams;
/**
 *带token的请求对象，token里面包含了userId
 */
public class TokenRequestParams extends RequestParams {
    private String token;

    public TokenRequestParams(String uri) {
        super(uri);
        this.token = AccountManager.getInstance().getToken();
    }
}
