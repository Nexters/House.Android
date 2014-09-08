package com.nexters.house.entity.reqcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0009 {
	private int type;
	private String commentId;
	private long commentNo;
	
	private String resultYn;

	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_comment_id")
	public String getCommentId() {
		return commentId;
	}
	@JsonProperty("_comment_no")
	public long getCommentNo() {
		return commentNo;
	}
	@JsonIgnore
	public String getResultYn() {
		return resultYn;
	}
	
	@JsonIgnore
	public void setType(int type) {
		this.type = type;
	}
	@JsonIgnore
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	@JsonIgnore
	public void setCommentNo(long commentNo) {
		this.commentNo = commentNo;
	}
	@JsonProperty("_type")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}
