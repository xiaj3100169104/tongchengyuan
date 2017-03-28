package com.style.net.core;

public class NetDataBean {
    public static final int SUCCESS = 0;

    public static final int SERVER_ERROR = 500; //服务器异常
    public static final int PARAM_MISS = 501; //传递参数有错误
    public static final int TOKEN_INVALID = 1000; //TOKEN过期
    public static final int TOKEN_EXPIRED = 1001; //TOKEN无效

    public int code;

    public String data;

    public String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }


}
