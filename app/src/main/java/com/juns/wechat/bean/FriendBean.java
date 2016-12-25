package com.juns.wechat.bean;

import android.text.TextUtils;

import com.juns.wechat.database.DbUtil;
import com.juns.wechat.database.FriendTable;
import com.juns.wechat.exception.UserNotFoundException;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by 王宗文 on 2016/6/20.
 */

@Table(name = FriendTable.TABLE_NAME, onCreated = FriendTable.CREATE_INDEX)
public class FriendBean {
    public static final String ID = "id";
    public static final String OWNER_ID = "ownerId";
    public static final String CONTACT_ID = "contactedId";
    public static final String SUB_TYPE = "subType";
    public static final String REMARK = "remark";
    public static final String FLAG = "flag";
    public static final String MODIFY_DATE = "modifyDate";

    @Column(name = ID, isId = true)
    private int id;
    @Column(name = OWNER_ID)
    private int ownerId;
    @Column(name = CONTACT_ID)
    private int contactedId;
    @Column(name = SUB_TYPE)
    private String subType;
    @Column(name = REMARK)
    private String remark;
    @Column(name = FLAG)
    private int flag;
    @Column(name = MODIFY_DATE)
    private long modifyDate;
    private String sortLetters;

    public FriendBean(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getContactedId() {
        return contactedId;
    }

    public void setContactedId(int contactedId) {
        this.contactedId = contactedId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public UserBean getContactUser() throws UserNotFoundException{
        try {
            return DbUtil.getDbManager().selector(UserBean.class).where("userName", "=", contactedId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            throw new UserNotFoundException(e);
        }
    }

    public String getShowName(){
        if(!TextUtils.isEmpty(remark)){
            return remark;
        }else{
            try {
                UserBean userBean = getContactUser();
                return userBean.getShowName();
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public String getHeadUrl(){
        try {
            UserBean userBean = getContactUser();
            return userBean.getHeadUrl();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
