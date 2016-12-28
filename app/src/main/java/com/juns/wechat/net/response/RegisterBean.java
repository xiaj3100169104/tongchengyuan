package com.juns.wechat.net.response;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

public class RegisterBean implements Serializable {
    public String errField;

    public String getErrField() {
        return errField;
    }

    public void setErrField(String errField) {
        this.errField = errField;
    }
}
