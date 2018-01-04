package cn.tongchengyuan.net.response;

import cn.tongchengyuan.bean.UserBean;
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
