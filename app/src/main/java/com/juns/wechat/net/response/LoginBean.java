package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

public class LoginBean implements Serializable {
    public String token;
    public UserBean userBean;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
