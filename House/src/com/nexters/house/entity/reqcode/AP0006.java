package com.nexters.house.entity.reqcode;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0006 implements Serializable {
	private int type;
	private String brdId;
	private String brdSubject;
	private int brdCateNo;
	private String brdTag;
	private byte[] brdContents;
	private List<AP0006Img> brdImg;
	private long brdNo;
	
	private String resultYn;

	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_brd_id")
	public String getBrdId() {
		return brdId;
	}
	@JsonProperty("_brd_subject")
	public String getBrdSubject() {
		return brdSubject;
	}
	@JsonProperty("_brd_cate_no")
	public int getBrdCateNo() {
		return brdCateNo;
	}
	@JsonProperty("_brd_tag")
	public String getBrdTag() {
		return brdTag;
	}
	@JsonProperty("_brd_contents")
	public byte[] getBrdContents() {
		return brdContents;
	}
	@JsonProperty("_brd_img")
	public List<AP0006Img> getBrdImg() {
		return brdImg;
	}
	@JsonProperty("_brd_no")
	public long getBrdNo() {
		return brdNo;
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
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}
	@JsonIgnore
	public void setBrdSubject(String brdSubject) {
		this.brdSubject = brdSubject;
	}
	@JsonIgnore
	public void setBrdCateNo(int brdCateNo) {
		this.brdCateNo = brdCateNo;
	}
	@JsonIgnore
	public void setBrdTag(String brdTag) {
		this.brdTag = brdTag;
	}
	@JsonIgnore
	public void setBrdContents(byte[] brdContents) {
		this.brdContents = brdContents;
	}
	@JsonIgnore
	public void setBrdImg(List<AP0006Img> brdImg) {
		this.brdImg = brdImg;
	}
	@JsonIgnore
	public void setBrdNo(long articleId) {
		this.brdNo = articleId;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
	
	public static class AP0006Img {
		@JsonProperty("_img_nm")
		public String imgNm;
		@JsonProperty("_img_origin_nm")
		public String imgOriginNm;
		@JsonProperty("_img_content")
		public byte[] imgContent;
		@JsonProperty("_img_type")
		public String imgType;
		@JsonProperty("_img_size")
		public long imgSize;
	}
}