package com.juns.wechat.net;

/**
 * Created by 王宗文 on 2016/7/7.
 */
public class HttpEvent{
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int CANCEL = 2;
    public static final int FINISH = 3;


    private int resultCode;
    private String msg;
    private Exception exception;

    public HttpEvent() {

    }

    public HttpEvent(int resultCode, String msg, Exception exception) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.exception = exception;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
