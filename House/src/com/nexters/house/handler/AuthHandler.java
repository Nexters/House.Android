package com.nexters.house.handler;

import java.util.HashMap;

import android.content.Intent;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode.CM0001;
import com.nexters.house.utils.JacksonUtils;

public class AuthHandler<T> extends AbstractHandler<T> {
	public final static int LOGIN_METHOD = 0;
	public final static int LOGOUT_METHOD = 1;
	private boolean isAuto;
	
	public AuthHandler(AbstractAsyncActivity abstractAsyncActivity, String tranCd) {
		super(abstractAsyncActivity, tranCd);
		isAuto = false;
	}
	
	public void setLoginAuto(boolean isAuto){
		this.isAuto = isAuto;
	}
	
	@Override
	public void handle(int method) {
		switch(method){
		case LOGIN_METHOD :
			login();
			break;
//		case LOGOUT_METHOD :
//			break;
		}
	}
	
	@Override
	public void showError() {
		mAbstractAsyncActivity.showResult("Fail");		
	}

	public void login(){
		SessionManager sessionManager = SessionManager.getInstance(mAbstractAsyncActivity.getApplicationContext());
		
		if(resCode.getTranCd().equals("CM0001")){
			CM0001 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0001.class);
			sessionManager.createLoginSession(SessionManager.HOUSE, cm.getCustName(), cm.getUsrId(), cm.getToken(), null, isAuto);
		}
		
		if(sessionManager.isLoggedIn()){
			mAbstractAsyncActivity.finish();
		} else 
			showError();
	}
}
