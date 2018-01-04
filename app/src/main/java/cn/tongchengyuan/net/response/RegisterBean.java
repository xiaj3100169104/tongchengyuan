package cn.tongchengyuan.net.response;

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
