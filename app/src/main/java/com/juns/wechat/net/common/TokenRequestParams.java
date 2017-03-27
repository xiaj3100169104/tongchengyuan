package com.juns.wechat.net.common;

import com.juns.wechat.manager.AccountManager;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 *带token的请求对象，token里面包含了userId
 */
public class TokenRequestParams {
    public String token;
    public Map<String, Object> map;

    public TokenRequestParams() {
        map = new HashMap<>();
        token = AccountManager.getInstance().getToken();
        map.put("token", token);
    }

    public void addParameter(String key, Object value) {
        map.put(key, value);
    }
}
