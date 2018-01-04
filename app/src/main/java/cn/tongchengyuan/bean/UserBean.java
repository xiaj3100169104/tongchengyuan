package cn.tongchengyuan.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    public String userId;// 用户id
    public String userName;// 用户名
    public String nickName; //昵称
    public String passWord;// 头像保存路径
    public String telephone;// 手机号
    public String headUrl;// 头像保存路径
    public String signature;// 个性签名
    public String sex;// 性别: M男士，W女士
    public String location;// 位置信息
    public String birthday;// 生日
    public String type;// N-正常用户，P-公众账号
    public Date createDate;
    public long modifyDate;
    @Transient
    public List<UserPropertyBean> userProperties;


    public UserBean(){

    }

    public UserBean(String userId, String nickName, String headUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.headUrl = headUrl;
    }

    @Generated(hash = 272149172)
    public UserBean(String userId, String userName, String nickName,
            String passWord, String telephone, String headUrl, String signature,
            String sex, String location, String birthday, String type,
            Date createDate, long modifyDate) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.passWord = passWord;
        this.telephone = telephone;
        this.headUrl = headUrl;
        this.signature = signature;
        this.sex = sex;
        this.location = location;
        this.birthday = birthday;
        this.type = type;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<UserPropertyBean> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(List<UserPropertyBean> userProperties) {
        this.userProperties = userProperties;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", telephone='" + telephone + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", signature='" + signature + '\'' +
                ", sex='" + sex + '\'' +
                ", location='" + location + '\'' +
                ", birthday='" + birthday + '\'' +
                ", type='" + type + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", userProperties=" + userProperties +
                '}';
    }
}
