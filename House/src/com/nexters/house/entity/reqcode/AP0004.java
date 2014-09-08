package com.nexters.house.entity.reqcode;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0004 implements Serializable {
	private int type;
	private int cateNo;
	private String usrId;
	private long brdNo;
	
	private int likeCnt;
	private String resultYn;

	
	@JsonProperty("_cate_no")
	public int getCateNo() {
		return cateNo;
	}
	@JsonIgnore
	public void setCateNo(int cateNo) {
		this.cateNo = cateNo;
	}
	@JsonProperty("_type")
	public int getType() {
		return type;
	}
	@JsonProperty("_usr_id")
	public String getUsrId() {
		return usrId;
	}
	@JsonProperty("_brd_no")
	public long getBrdNo() {
		return brdNo;
	}
	@JsonIgnore	
	public int getLikeCnt() {
		return likeCnt;
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
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	@JsonIgnore
	public void setBrdNo(long reqPoNo) {
		this.brdNo = reqPoNo;
	}
	@JsonProperty("_like_cnt")
	public void setLikeCnt(int likeCnt) {
		this.likeCnt = likeCnt;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}