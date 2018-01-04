package cn.tongchengyuan.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DynamicBean implements Serializable {
    //不支持BigInteger类型，当改变数据结构时删除自动生成的有注解的构造函数，重新make project
    private static final long serialVersionUID = 1L;

    @Id
    private String dynamicId;// Id
    private String publisherId;// 发表人
    private String content;// 动态内容
    private String images;// 图片
    private String createDate; //创建日期
    private String publisherName;
    @Transient
    private UserBean user;//发布者
    @Transient
    private List<CommentBean> supportList; //点赞列表
    @Transient
    private List<CommentBean> commentList;//评论列表

    public DynamicBean() {

    }



    @Generated(hash = 392734704)
    public DynamicBean(String dynamicId, String publisherId, String content,
            String images, String createDate, String publisherName) {
        this.dynamicId = dynamicId;
        this.publisherId = publisherId;
        this.content = content;
        this.images = images;
        this.createDate = createDate;
        this.publisherName = publisherName;
    }



    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<CommentBean> getSupportList() {
        return supportList;
    }

    public void setSupportList(List<CommentBean> supportList) {
        this.supportList = supportList;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "DynamicBean{" +
                ", dynamicId=" + dynamicId +
                ", publisherId=" + publisherId +
                ", content='" + content + '\'' +
                ", images='" + images + '\'' +
                ", createDate='" + createDate + '\'' +
                ", publisherName='" + publisherName + '\'' +
                ", user=" + user +
                ", supportList=" + supportList +
                ", commentList=" + commentList +
                '}';
    }
}
