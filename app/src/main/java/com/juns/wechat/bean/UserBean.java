package com.juns.wechat.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.juns.wechat.database.UserTable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

@Table(name = UserTable.TABLE_NAME, onCreated = UserTable.CREATE_INDEX)
public class UserBean implements Parcelable {
	public static final String ID = "id";
	public static final String USERNAME = "userName";
    public static final String NICKNAME = "nickName";
	public static final String PASSWORD = "passWord";
	public static final String HEADURL = "headUrl";
	public static final String SIGNATURE = "signature";
	public static final String SEX = "sex";
	public static final String LOCATION = "location";
	public static final String BIRTHDAY = "birthday";
	public static final String TYPE ="type";
	public static final String TELEPHONE = "telephone";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";

    @Column(name = "id", isId = true)
    public int id;//
    @Column(name = "userName")
    public String userName;// 用户名
    @Column(name = "nickName")
    public String nickName; //昵称
    @Column(name = "passWord")
    public String passWord;// 头像保存路径
    @Column(name = "telephone")
    public String telephone;// 手机号
    @Column(name = "headUrl")
    public String headUrl;// 头像保存路径
    @Column(name = "signature")
    public String signature;// 个性签名
    @Column(name = "sex")
    public String sex;// 性别: M男士，W女士
    @Column(name = "location")
    public String location;// 位置信息
    @Column(name = "birthday")
    public String birthday;// 生日
    @Column(name = "type")
    public String type;// N-正常用户，P-公众账号
    @Column(name = "createDate")
    public Date createDate;
    @Column(name = "modifyDate")
    public long modifyDate;

    public UserBean(){

    }

    public enum Sex{
        MAN("M"), WOMAN("W");

        public String value;

        Sex(String s){
            this.value = s;
        }

        public static boolean isMan(String male){
            if(TextUtils.isEmpty(male)){
                return true;
            }
            if(MAN.value.equals(male)){
                return true;
            }else {
                return false;
            }
        }

        @Override
        public String toString() {
            return value;
        }
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTelephone() {
		return telephone;
	}

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getShowName(){
        if(!TextUtils.isEmpty(nickName)){
            return nickName;
        }
        return userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.nickName);
        dest.writeString(this.passWord);
        dest.writeString(this.telephone);
        dest.writeString(this.headUrl);
        dest.writeString(this.signature);
        dest.writeString(this.sex);
        dest.writeString(this.location);
        dest.writeString(this.birthday);
        dest.writeString(this.type);
        dest.writeLong(this.createDate != null ? this.createDate.getTime() : -1);
        dest.writeLong(this.modifyDate);
    }

    protected UserBean(Parcel in) {
        this.id = in.readInt();
        this.userName = in.readString();
        this.nickName = in.readString();
        this.passWord = in.readString();
        this.telephone = in.readString();
        this.headUrl = in.readString();
        this.signature = in.readString();
        this.sex = in.readString();
        this.location = in.readString();
        this.birthday = in.readString();
        this.type = in.readString();
        long tmpCreateDate = in.readLong();
        this.createDate = tmpCreateDate == -1 ? null : new Date(tmpCreateDate);
        this.modifyDate = in.readLong();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
