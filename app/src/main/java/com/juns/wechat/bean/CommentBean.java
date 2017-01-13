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
	private Integer replyUserId;//被评论人用户id
	private String subType;//  类型
	private String content;// 评论内容
    private long createDate; //创建日期
    private BigInteger modifyDate;///修改信息日期
	private UserBean commentUser;//评论人或者点赞人
    private UserBean replyUser;//被评论人或者点赞人

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

	public Integer getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(Integer replyUserId) {
		this.replyUserId = replyUserId;
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

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public BigInteger getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(BigInteger modifyDate) {
		this.modifyDate = modifyDate;
	}

    public UserBean getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserBean commentUser) {
        this.commentUser = commentUser;
    }

    public UserBean getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserBean replyUser) {
        this.replyUser = replyUser;
    }
}
