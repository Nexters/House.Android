package com.nexters.house.entity.reqcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CM0007 {
	private String usrId;
	private String pushAppId;
	private String pushTokenId;
	private String pushYn;
	
	private String resultYn;

	@JsonProperty("_usr_id")
	public String getUsrId() {
		return usrId;
	}
	@JsonProperty("_push_app_id")
	public String getPushAppId() {
		return pushAppId;
	}
	@JsonProperty("_push_token_id")
	public String getPushTokenId() {
		return pushTokenId;
	}
	@JsonProperty("_push_yn")
	public String getPushYn() {
		return pushYn;
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
	public void setPushAppId(String pushAppId) {
		this.pushAppId = pushAppId;
	}
	@JsonIgnore
	public void setPushTokenId(String pushTokenId) {
		this.pushTokenId = pushTokenId;
	}
	@JsonIgnore
	public void setPushYn(String pushYn) {
		this.pushYn = pushYn;
	}
	@JsonProperty("_push_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}
