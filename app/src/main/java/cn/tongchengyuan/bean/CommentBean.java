package cn.tongchengyuan.bean;

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

	private String commentId;// 评论id
	private String commenterId;// 评论人或者点赞人id
	private String dynamicId;// 动态id
	private String replyUserId;//被评论人用户id
	private String subType;//  类型
	private String content;// 评论内容
    private long createDate; //创建日期
    private BigInteger modifyDate;///修改信息日期
	private String commentUserName;//评论人或者点赞人
    private String replyUserName;//被评论人或者点赞人

    public CommentBean(){

    }
    public CommentBean(String content, String commentUserName, String replyUserName) {
        this.content = content;
        this.commentUserName = commentUserName;
        this.replyUserName = replyUserName;
    }
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public String getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(String replyUserId) {
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

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getCommentUserName() {
		return commentUserName;
	}

	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
}
