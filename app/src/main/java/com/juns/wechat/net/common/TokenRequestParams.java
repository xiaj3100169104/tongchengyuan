package com.juns.wechat.net.common;

import com.juns.wechat.manager.AccountManager;

import org.xutils.http.RequestParams;

public class TokenRequestParams extends RequestParams {
    private String token;

    public TokenRequestParams(String uri) {
        super(uri);
        this.token = AccountManager.getInstance().getToken();
    }
}
