package com.nexters.house.entity.reqcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AP0007 {
	private int type;
	private String brdId;
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
	public void setBrdNo(long articleId) {
		this.brdNo = articleId;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}
