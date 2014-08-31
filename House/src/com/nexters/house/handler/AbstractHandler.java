package com.nexters.house.handler;

import java.util.ArrayList;

import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.entity.APICode;

public abstract class AbstractHandler <T> {
	protected AbstractAsyncActivity mAbstractAsyncActivity;
	protected AbstractAsyncFragmentActivity mAbstractAsyncFragmentActivity;
	protected APICode<T> reqCode, resCode;
	private ArrayList<T> tranData;
	
	public AbstractHandler(AbstractAsyncFragmentActivity abstractAsyncFragmentActivity, String tranCd) {
		mAbstractAsyncFragmentActivity = abstractAsyncFragmentActivity;
		tranData = new ArrayList<T>();
		reqCode = new APICode<T>();
		reqCode.setTranData(tranData);
		reqCode.setTranCd(tranCd);
	}
	
	public AbstractHandler(AbstractAsyncActivity abstractAsyncActivity, String tranCd) {
		mAbstractAsyncActivity = abstractAsyncActivity;
		tranData = new ArrayList<T>();
		reqCode = new APICode<T>();
		reqCode.setTranData(tranData);
		reqCode.setTranCd(tranCd);
	}
	
	abstract public void handle(int method);
	abstract public void showError();
	
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
