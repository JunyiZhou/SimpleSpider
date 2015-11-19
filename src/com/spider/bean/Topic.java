package com.spider.bean;

public class Topic {
	private String id;
	private String title;
	private String url;
	private String discussionId;
	private String author;
	private String createDate;
	private String commentNum;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDiscussionId() {
		return discussionId;
	}
	
	public void setDiscussionId(String discussionId) {
		this.discussionId = discussionId;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getCommentNum() {
		return commentNum;
	}
	
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
}
