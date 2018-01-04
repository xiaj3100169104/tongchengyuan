package cn.tongchengyuan.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiajun on 2017/11/1.
 */
@Entity
public class UserBasicInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String userBasicId;// Id
    private String userId;// 发表人
    String education;
    String emotion;
    String industry;
    String workArea;
    String companyInfo;
    String hometownInfo;
    String myHeart;


    public void setData(String education,
                        String emotion, String industry, String workArea, String companyInfo,
                        String hometownInfo, String myHeart) {
        this.education = education;
        this.emotion = emotion;
        this.industry = industry;
        this.workArea = workArea;
        this.companyInfo = companyInfo;
        this.hometownInfo = hometownInfo;
        this.myHeart = myHeart;
    }

    @Generated(hash = 36293911)
    public UserBasicInfo() {
    }

    @Generated(hash = 204873187)
    public UserBasicInfo(String userBasicId, String userId, String education, String emotion,
            String industry, String workArea, String companyInfo, String hometownInfo,
            String myHeart) {
        this.userBasicId = userBasicId;
        this.userId = userId;
        this.education = education;
        this.emotion = emotion;
        this.industry = industry;
        this.workArea = workArea;
        this.companyInfo = companyInfo;
        this.hometownInfo = hometownInfo;
        this.myHeart = myHeart;
    }

    public String getUserBasicId() {
        return this.userBasicId;
    }

    public void setUserBasicId(String userBasicId) {
        this.userBasicId = userBasicId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEducation() {
        return this.education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmotion() {
        return this.emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWorkArea() {
        return this.workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getCompanyInfo() {
        return this.companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public String getHometownInfo() {
        return this.hometownInfo;
    }

    public void setHometownInfo(String hometownInfo) {
        this.hometownInfo = hometownInfo;
    }

    public String getMyHeart() {
        return this.myHeart;
    }

    public void setMyHeart(String myHeart) {
        this.myHeart = myHeart;
    }

}
