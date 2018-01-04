package cn.tongchengyuan.net.response;

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
