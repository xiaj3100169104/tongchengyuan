package com.style.rxAndroid.newwork.core;

/**
 * Created by xiajun on 2016/10/9.
 */
public class NetJsonResult {
    public int code;

    public String data;

    public String msg;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
