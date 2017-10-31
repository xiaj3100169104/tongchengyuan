package com.juns.wechat.bean;

import android.text.TextUtils;

import com.juns.wechat.database.DbUtil;
import com.juns.wechat.database.FriendTable;
import com.juns.wechat.database.dao.UserDao;
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
    public static final String CONTACT_ID = "contactId";
    public static final String SUB_TYPE = "subType";
    public static final String REMARK = "remark";
    public static final String FLAG = "flag";
    public static final String MODIFY_DATE = "modifyDate";

    @Column(name = ID, isId = true)
    private int id;
    @Column(name = OWNER_ID)
    private int ownerId;
    @Column(name = CONTACT_ID)
    private int contactId;
    @Column(name = SUB_TYPE)
    private String subType;
    @Column(name = REMARK)
    private String remark;
    @Column(name = FLAG)
    private int flag;
    @Column(name = MODIFY_DATE)
    private long modifyDate;

    public String sortLetters;
    public UserBean contactUser;

    public FriendBean() {

    }

    public FriendBean(int ownerId, int contactId, String subType) {
        this.ownerId = ownerId;
        this.contactId = contactId;
        this.subType = subType;
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

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
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

    public void setContactUser(UserBean userBean) {
        this.contactUser = userBean;
    }

    public UserBean getContactUser() {
        if (contactUser == null)
            contactUser = UserDao.getInstance().findByUserId(contactId);
        return contactUser;

    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", contactId=" + contactId +
                ", subType='" + subType + '\'' +
                ", remark='" + remark + '\'' +
                ", flag=" + flag +
                ", modifyDate=" + modifyDate +
                ", sortLetters='" + sortLetters + '\'' +
                ", contactUser=" + contactUser +
                '}';
    }
}
