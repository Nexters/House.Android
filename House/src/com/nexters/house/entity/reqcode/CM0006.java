package com.nexters.house.entity.reqcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CM0006 {
	private String usrId;
	private String usrPw;
	private String custName;
	private int usrSts;
	private CM0006Img usrImg;
	
	private String resultYn;
	
	@JsonProperty("_usr_id")
	public String getUsrId() {
		return usrId;
	}
	@JsonProperty("_usr_pw")
	public String getUsrPw() {
		return usrPw;
	}
	@JsonProperty("_custname")
	public String getCustName() {
		return custName;
	}
	@JsonProperty("_sts")
	public int getUsrSts() {
		return usrSts;
	}
	@JsonProperty("_usr_img")
	public CM0006Img getUsrImg() {
		return usrImg;
	}
	@JsonIgnore
	public String getResultYn() {
		return resultYn;
	}
	
	@JsonIgnore
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	@JsonIgnore
	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
	@JsonIgnore
	public void setCustName(String custName) {
		this.custName = custName;
	}
	@JsonIgnore
	public void setUsrSts(int usrSts) {
		this.usrSts = usrSts;
	}
	@JsonIgnore
	public void setUsrImg(CM0006Img usrImg) {
		this.usrImg = usrImg;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
	
	public static class CM0006Img {
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
