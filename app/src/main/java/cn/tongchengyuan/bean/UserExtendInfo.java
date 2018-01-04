package cn.tongchengyuan.bean;

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
    private String userExtendId;
    private String userId;
    private String data;

    @Generated(hash = 467904249)
    public UserExtendInfo() {
    }

    @Generated(hash = 1945461388)
    public UserExtendInfo(String userExtendId, String userId, String data) {
        this.userExtendId = userExtendId;
        this.userId = userId;
        this.data = data;
    }

     public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getUserExtendId() {
        return this.userExtendId;
    }

    public void setUserExtendId(String userExtendId) {
        this.userExtendId = userExtendId;
    }

}
