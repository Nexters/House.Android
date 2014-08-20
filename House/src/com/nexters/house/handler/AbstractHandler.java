package com.nexters.house.handler;

import java.util.ArrayList;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.entity.APICode;

public abstract class AbstractHandler <T> {
	protected AbstractAsyncActivity mAbstractAsyncActivity;
	protected APICode<T> reqCode, resCode;
	private ArrayList<T> tranData;
	
	public AbstractHandler(AbstractAsyncActivity abstractAsyncActivity, String tranCd) {
		mAbstractAsyncActivity = abstractAsyncActivity;
		tranData = new ArrayList<T>();
		reqCode = new APICode<T>();
		reqCode.setTranData(tranData);
		reqCode.setTranCd(tranCd);
	}
	
	abstract public void handle(int method);
	
	public void addTranData(T obj){
		tranData.add(obj);
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
}
