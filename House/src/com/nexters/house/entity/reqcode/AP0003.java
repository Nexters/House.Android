package com.nexters.house.entity.reqcode;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0003 implements Serializable {
	private int type;
	private long reqPoNo;
	
	private long brdNo;
	private String brdNm;
	private String brdId;
	private String brdProfileImg;
	private String brdSubject;
	private byte[] brdContents;
	private String brdTag;
	private String brdCreated;
	private String brdCateNm;
	private int brdCate;
	private int brdLikeCnt;
	private int brdScrapCnt;
	private List<String> brdImg;
	
	private int brdCommentCnt;
	private List<AP0003Comment> brdComment;
	
	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_req_po_no")
	public long getReqPoNo() {
		return reqPoNo;
	}

	@JsonIgnore
	public void setBrdNo(long brdNo) {
		this.brdNo = brdNo;
	}
	@JsonIgnore
	public String getBrdNm() {
		return brdNm;
	}
	@JsonIgnore
	public String getBrdId() {
		return brdId;
	}
	@JsonIgnore
	public String getBrdProfileImg() {
		return brdProfileImg;
	}
	@JsonIgnore
	public String getBrdSubject() {
		return brdSubject;
	}
	@JsonIgnore
	public byte[]  getBrdContents() {
		return brdContents;
	}
	@JsonIgnore
	public String getBrdTag() {
		return brdTag;
	}
	@JsonIgnore
	public String getBrdCateNm() {
		return brdCateNm;
	}
	@JsonIgnore
	public int getBrdCate() {
		return brdCate;
	}
	@JsonIgnore
	public String getBrdCreated() {
		return brdCreated;
	}
	@JsonIgnore
	public int getBrdLikeCnt() {
		return brdLikeCnt;
	}
	@JsonIgnore
	public List<String> getBrdImg() {
		return brdImg;
	}
	@JsonIgnore
	public int getBrdScrapCnt() {
		return brdScrapCnt;
	}
	@JsonIgnore
	public int getBrdCommentCnt() {
		return brdCommentCnt;
	}
	@JsonIgnore
	public List<AP0003Comment> getBrdComment() {
		return brdComment;
	}
	
	@JsonIgnore
	public void setType(int type) {
		this.type = type;
	}
	@JsonIgnore
	public void setReqPoNo(long reqPoNo) {
		this.reqPoNo = reqPoNo;
	}
	@JsonProperty("_brd_no")
	public long getBrdNo() {
		return brdNo;
	}
	@JsonProperty("_brd_nm")
	public void setBrdNm(String brdNm) {
		this.brdNm = brdNm;
	}
	@JsonProperty("_brd_id")
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}
	@JsonProperty("_brd_profile_img")
	public void setBrdProfileImg(String brdProfileImg) {
		this.brdProfileImg = brdProfileImg;
	}
	@JsonProperty("_brd_subject")
	public void setBrdSubject(String brdSubject) {
		this.brdSubject = brdSubject;
	}
	@JsonProperty("_brd_contents")
	public void setBrdContents(byte[] brdContents) {
		this.brdContents = brdContents;
	}
	@JsonProperty("_brd_tag")
	public void setBrdTag(String brdTag) {
		this.brdTag = brdTag;
	}
	@JsonProperty("_brd_cate_nm")
	public void setBrdCateNm(String brdCateNm) {
		this.brdCateNm = brdCateNm;
	}
	@JsonProperty("_brd_cate")
	public void setBrdCate(int brdCate) {
		this.brdCate = brdCate;
	}
	@JsonProperty("_brd_created")
	public void setBrdCreated(String brdCreated) {
		this.brdCreated = brdCreated;
	}
	@JsonProperty("_brd_like_cnt")
	public void setBrdLikeCnt(int brdLikeCnt) {
		this.brdLikeCnt = brdLikeCnt;
	}
	@JsonProperty("_brd_img")
	public void setBrdImg(List<String> brdImg) {
		this.brdImg = brdImg;
	}
	@JsonProperty("_brd_scrap_cnt")
	public void setBrdScrapCnt(int brdScrapCnt) {
		this.brdScrapCnt = brdScrapCnt;
	}
	@JsonProperty("_brd_comment_cnt")
	public void setBrdCommentCnt(int brdCommentCnt) {
		this.brdCommentCnt = brdCommentCnt;
	}
	@JsonProperty("_brd_comment")
	public void setBrdComment(List<AP0003Comment> brdComment) {
		this.brdComment = brdComment;
	}

	public static class AP0003Comment {
		@JsonProperty("_brd_comment_no")
		public long brdCommentNo;
		@JsonProperty("_brd_comment_nm")
		public String brdCommentNm;
		@JsonProperty("brd_comment_id")
		public String brdCommentId;
		@JsonProperty("_brd_comment_profile_img")
		public String brdCommentProfileImg;
		@JsonProperty("_brd_comment_contents")
		public byte[]  brdCommentContents;
	}
}