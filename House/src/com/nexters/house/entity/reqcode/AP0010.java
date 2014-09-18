package com.nexters.house.entity.reqcode;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0010 implements Serializable {
	private int type;
	private String brdId;
	private long brdNo;
	public String imgNm;
	public String imgOriginNm;
	public byte[] imgContent;
	public String imgType;
	public long imgSize;
	
	private String resultYn;
	
	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_brd_id")
	public String getBrdId() {
		return brdId;
	}
	@JsonProperty("_brd_no")
	public long getBrdNo() {
		return brdNo;
	}
	@JsonProperty("_img_nm")
	public String getImgNm() {
		return imgNm;
	}
	@JsonProperty("_img_origin_nm")
	public String getImgOriginNm() {
		return imgOriginNm;
	}
	@JsonProperty("_img_content")
	public byte[] getImgContent() {
		return imgContent;
	}
	@JsonProperty("_img_type")
	public String getImgType() {
		return imgType;
	}
	@JsonProperty("_img_size")
	public long getImgSize() {
		return imgSize;
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
	public void setBrdNo(long brdNo) {
		this.brdNo = brdNo;
	}
	@JsonIgnore
	public void setImgNm(String imgNm) {
		this.imgNm = imgNm;
	}
	@JsonIgnore
	public void setImgOriginNm(String imgOriginNm) {
		this.imgOriginNm = imgOriginNm;
	}
	@JsonIgnore
	public void setImgContent(byte[] imgContent) {
		this.imgContent = imgContent;
	}
	@JsonIgnore
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	@JsonIgnore
	public void setImgSize(long imgSize) {
		this.imgSize = imgSize;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}