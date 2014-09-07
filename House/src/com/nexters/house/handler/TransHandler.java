package com.nexters.house.handler;

import java.util.ArrayList;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.UserEntity;

public class TransHandler <T> {
	protected APICode<T> reqCode, resCode;
	private ArrayList<T> tranData;
	private Handler mHandler;
	
	public TransHandler(String tranCd, Handler handler) {
		tranData = new ArrayList<T>();
		reqCode = new APICode<T>();
		tranData = new ArrayList<T>();
		reqCode.setTranData(tranData);
		reqCode.setTranCd(tranCd);
		mHandler = handler;
	}
	
	public TransHandler(String tranCd, Handler handler, T obj) {
		tranData = new ArrayList<T>();
		reqCode = new APICode<T>();
		tranData = new ArrayList<T>();
		reqCode.setTranData(tranData);
		reqCode.setTranCd(tranCd);
		mHandler = handler;
		setOneTranData(obj);
	}
	
	public void handle(){
		mHandler.handle(resCode);
	}
	
	public void addTranData(T obj){
		tranData.add(obj);
	}
	
	public void setOneTranData(T obj){
		if(tranData.size() == 0)
			tranData.add(obj);
		else 
			tranData.set(0, obj);
	}
	
	public void removeTranData(T obj){
		tranData.remove(obj);
	}
	
	public APICode<T> getReqCode() {
		return reqCode;
	}

	public APICode<T> getResCode() {
		return resCode;
	}

	public void setResCode(APICode<T> resCode) {
		this.resCode = resCode;
	}
	
	public interface Handler {
		public void handle(APICode resCode);
	}
}
