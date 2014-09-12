package com.nexters.house.activity;

import java.util.HashMap;

import org.springframework.http.MediaType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.nexters.house.R;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.UserEntity;
import com.nexters.house.entity.reqcode.CM0001;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class SignInActivity extends AbstractAsyncActivity implements View.OnClickListener {
    private EditText mHsEmail;
    private EditText mHsPassword;
    private Button mBtnSignIn;
    private CheckBox mCbAutoLogin;
    private boolean mAutoLogin;
    
    PostMessageTask mSignInTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);

        initResource();
        initEvent();
    }

    private void initResource() {
        mHsEmail = (EditText) findViewById(R.id.hs_sign_in_email);
        mHsPassword = (EditText) findViewById(R.id.hs_sign_in_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
    }

    private void initEvent() {
        mBtnSignIn.setOnClickListener(this);
        mCbAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mAutoLogin = isChecked;
			}
        });
    }

    public void onClick(View v) {
//        if (!isAllFormsFilled()) {
//            showAlert(R.string.error_fill_in_all_forms);
//        } else if (!isValidEmail()) {
//            showAlert(R.string.error_invalid_email);
//        } else {
            executeSignIn();
//        }
    }

    private String getPassword() {
        return mHsPassword.getText().toString();
    }

    private String getEmail() {
        return mHsEmail.getText().toString();
    }

    private boolean isAllFormsFilled() {
        return !getEmail().equals("") && !getPassword().equals("");
    }

    private boolean isValidEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    private void testLogin(){
    	SessionManager sessionManager = SessionManager.getInstance(this.getApplicationContext());
		sessionManager.createLoginSession(SessionManager.HOUSE, "test", "test", "test", null, mAutoLogin);
		finish();
    }
    
    private void executeSignIn() {
		if(mSignInTask != null && mSignInTask.getStatus() != mSignInTask.getStatus().FINISHED)
			return ;
		
    	//testLogin();
    	TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CM0001 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0001.class);
				SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
				
				String baseUrl = getString(R.string.base_uri);
				String profileImg = null;
				if(cm.getProfileImg() != null)
					profileImg = baseUrl + cm.getProfileImg();
				sessionManager.createLoginSession(SessionManager.HOUSE, cm.getCustName(), cm.getUsrId(), cm.getToken(), null, mAutoLogin);
				
				if(sessionManager.isLoggedIn()){
					finish();
				} else 
					showResult("Error");
			}
		}; 
	  	CM0001 cm = new CM0001();
    	cm.setUsrId(mHsEmail.getText().toString());
    	cm.setUsrPw(mHsPassword.getText().toString());
		
    	TransHandler<CM0001> authHandler = new TransHandler<CM0001>("CM0001", handler, cm);
    	mSignInTask = new PostMessageTask(this, authHandler);
    	mSignInTask.execute(MediaType.APPLICATION_JSON); 
    }
}
