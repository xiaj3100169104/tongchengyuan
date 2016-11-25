package com.style.rxAndroid.newwork.core;

/**
 * Created by xiajun on 2016/10/9.
 */
public class NetJsonResult {
    private int code;

    private String body;

    private String msg;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setBody(String body){
        this.body = body;
    }
    public String getBody(){
        return this.body;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
