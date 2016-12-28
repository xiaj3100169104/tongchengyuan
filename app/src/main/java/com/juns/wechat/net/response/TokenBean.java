package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;

import java.io.Serializable;

public class TokenBean implements Serializable {
    public String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
