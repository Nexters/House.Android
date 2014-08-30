package com.nexters.house.entity.reqcode;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CM0002 implements Serializable {
	private String token;
	
	private String resultYn;
	
	@JsonProperty("_token")
	public String getToken() {
		return token;
	}
	@JsonIgnore
	public String getResultYn() {
		return resultYn;
	}
	@JsonIgnore
	public void setToken(String token) {
		this.token = token;
	}
	@JsonProperty("_rslt_yn")
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
}