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
    public static final String OWNER_NAME = "ownerName";
    public static final String CONTACT_NAME = "contactName";
    public static final String SUB_TYPE = "subType";
    public static final String REMARK = "remark";
    public static final String FLAG = "flag";
    public static final String MODIFY_DATE = "modifyDate";

    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "ownerName")
    private String ownerName;
    @Column(name = "contactName")
    private String contactName;
    @Column(name = "subType")
    private String subType;
    @Column(name = "remark")
    private String remark;
    @Column(name = "flag")
    private int flag;
    @Column(name = "modifyDate")
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
            return DbUtil.getDbManager().selector(UserBean.class).where("userName", "=", contactName).findFirst();
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
