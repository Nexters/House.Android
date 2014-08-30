package com.nexters.house.entity.reqcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CM0005 {
	private String usrId;
	private String usrPw;
	private String custName;
	private int usrSts;
	private String profileImg;
	
	@JsonProperty("_usr_id")
	public String getUsrId() {
		return usrId;
	}
	@JsonProperty("_usr_pw")
	public String getUsrPw() {
		return usrPw;
	}
	@JsonIgnore
	public String getCustName() {
		return custName;
	}
	@JsonIgnore
	public int getUsrSts() {
		return usrSts;
	}
	@JsonIgnore
	public String getProfileImg() {
		return profileImg;
	}
	
	@JsonProperty("_usr_id")
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	@JsonIgnore
	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
	@JsonProperty("_custname")
	public void setCustName(String custName) {
		this.custName = custName;
	}
	@JsonProperty("_sts")
	public void setUsrSts(int usrSts) {
		this.usrSts = usrSts;
	}
	@JsonProperty("_profile_img")
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
}
