package com.nexters.house.fragment;

import java.util.HashMap;

import org.springframework.http.MediaType;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nexters.house.R;
import com.nexters.house.activity.SetActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.reqcode.CM0006;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;

public class SetNickNameFragment extends Fragment implements View.OnClickListener {
    private SetActivity mSetActivity;

    private EditText mHouseName;
    private Button mBtnName;
    
    private PostMessageTask mAuthTask;
    
    public static SetNickNameFragment newInstance() {
        SetNickNameFragment fragment = new SetNickNameFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSetActivity = (SetActivity) activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_set_nickname, container, false);
    	initResource(v);
    	initEvent();
    	return v;
    }

    public void initResource(View v){
    	mHouseName = (EditText) v.findViewById(R.id.house_name);
    	mBtnName = (Button) v.findViewById(R.id.btn_name);
    }
    
    public void initEvent(){
    	mBtnName.setOnClickListener(this);
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_name:
			saveNickName();
			break;
		}
	}
	
	public void saveNickName(){
		if(mAuthTask != null && mAuthTask.getStatus() != mAuthTask.getStatus().FINISHED)
			return ;
		String usrId = SessionManager.getInstance(mSetActivity).getUserDetails().get(SessionManager.KEY_EMAIL);
		final String name = mHouseName.getText().toString();
		
		CM0006 cm = new CM0006();
		cm.setUsrId(usrId);
		cm.setCustName(name);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				SessionManager.getInstance(mSetActivity).putString(SessionManager.KEY_NAME, name);
				mSetActivity.showResult("닉네임 수정 완료 !");
			}
		};
		// Post Aricle 
    	TransHandler<CM0006> authHandler = new TransHandler<CM0006>("CM0006", handler, cm);
    	mAuthTask = new PostMessageTask(mSetActivity, authHandler);
    	mAuthTask.execute(MediaType.APPLICATION_JSON);
	}
}
