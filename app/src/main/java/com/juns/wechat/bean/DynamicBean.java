package com.juns.wechat.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


public class DynamicBean implements Serializable{

	private Integer dynamicId;// Id
	private Integer publisherId;// 发表人
	private String content;// 动态内容
	private String images;// 图片
	private Date createDate; //创建日期
	private BigInteger modifyDate;///修改信息日期
	private UserBean user;//发布者
	private List<CommentBean> supportList; //点赞列表
	private List<CommentBean> commentList;//评论列表

    public DynamicBean(){

    }

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public Integer getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Integer publisherId) {
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigInteger getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(BigInteger modifyDate) {
		this.modifyDate = modifyDate;
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

}
