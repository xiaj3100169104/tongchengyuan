package com.juns.wechat.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
public class CommentBean implements Serializable{
	
	public enum SubType{
		SUPPORT("support"), COMMENT("comment");
		
		private String subType;
		
		private SubType(String type){
			this.subType = type;
		}
		
		@Override
		public String toString() {
			return subType;
		}
	}

	private Integer commentId;// 评论id
	private Integer commenterId;// 评论人或者点赞人id
	private Integer dynamicId;// 动态id
    private String subType;//  类型
	private String content;// 评论内容
    private Date createDate; //创建日期
    private BigInteger modifyDate;///修改信息日期
	private UserBean user;//评论人或者点赞人

    public CommentBean(){

    }

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(Integer commenterId) {
		this.commenterId = commenterId;
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
}
