package com.juns.wechat.bean;

/**
 * Created by 王者 on 2016/8/3.
 */
public enum Flag {
    VALID(0), INVALID(-1);
    int value;
    Flag(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
