package com.juns.wechat.exception;

/**
 * Created by 王者 on 2016/8/7.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(){
        super();
    }

    public UserNotFoundException(String detailMessage){
        super(detailMessage);
    }

    public UserNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
