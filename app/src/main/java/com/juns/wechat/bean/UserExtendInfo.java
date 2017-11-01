package com.juns.wechat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiajun on 2017/11/1.
 */
@Entity
public class UserExtendInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String UserExtendId;
    private Integer userId;
    private String data;

    @Generated(hash = 1915352593)
    public UserExtendInfo(String UserExtendId, Integer userId, String data) {
        this.UserExtendId = UserExtendId;
        this.userId = userId;
        this.data = data;
    }
    @Generated(hash = 467904249)
    public UserExtendInfo() {
    }

    public String getUserExtendId() {
        return this.UserExtendId;
    }
    public void setUserExtendId(String UserExtendId) {
        this.UserExtendId = UserExtendId;
    }
    public Integer getUserId() {
        return this.userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

}
