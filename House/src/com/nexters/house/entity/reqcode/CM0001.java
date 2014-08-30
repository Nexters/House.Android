package com.nexters.house.entity.reqcode;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CM0001 implements Serializable {
    public String usrId;
    public String usrPw;
    public String regNo;
    public String custName;
    public String firstLogin;
    public String token;

    @JsonProperty("_usr_id")
    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    @JsonIgnore
    public void setUsrPw(String usrPw) {
        this.usrPw = usrPw;
    }

    @JsonProperty("_regno")
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    @JsonProperty("_custname")
    public void setCustName(String custName) {
        this.custName = custName;
    }

    @JsonProperty("_firstlogin")
    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    @JsonProperty("_token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("_usr_id")
    public String getUsrId() {
        return usrId;
    }

    @JsonProperty("_usr_pw")
    public String getUsrPw() {
        return usrPw;
    }

    @JsonIgnore
    public String getRegNo() {
        return regNo;
    }
    
    @JsonIgnore
    public String getCustName() {
        return custName;
    }
    
    @JsonIgnore
    public String getFirstLogin() {
        return firstLogin;
    }
    
    @JsonIgnore
    public String getToken() {
        return token;
    }
}