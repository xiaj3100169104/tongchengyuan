package cn.tongchengyuan.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class FriendBean {

    @Id
    public String id;
    public String ownerId;
    public String contactId;
    public String subType;
    public String remark;
    public int flag;
    public long modifyDate;

    @Transient
    public String sortLetters;
    @Transient
    public String nickName;
    @Transient
    public String headUrl;

    public FriendBean() {

    }

    public FriendBean(String ownerId, String contactId, String subType) {
        this.ownerId = ownerId;
        this.contactId = contactId;
        this.subType = subType;
    }

    @Generated(hash = 1282100691)
    public FriendBean(String id, String ownerId, String contactId, String subType,
            String remark, int flag, long modifyDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.contactId = contactId;
        this.subType = subType;
        this.remark = remark;
        this.flag = flag;
        this.modifyDate = modifyDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
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
                ", nickName=" + nickName +
                '}';
    }
}
