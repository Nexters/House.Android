package com.nexters.house.activity;

import java.util.HashMap;

import android.app.Activity;

import org.json.JSONObject;
import org.springframework.http.MediaType;

import com.nexters.house.R;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.reqcode.CM0001;
import com.nexters.house.entity.reqcode.CM0003;
import com.nexters.house.entity.reqcode.CM0003.CM0003Img;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AbstractAsyncActivity implements View.OnClickListener {
    private EditText mHsUsername;
    private EditText mHsEmail;
    private EditText mHsPassword;
    private EditText mHsPasswordCheck;
    private Button mBtnSignUp;
    
    PostMessageTask mSignUpTask;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        initResource();
        initEvent();
   }
	
    private void initResource() {
        mHsUsername = (EditText) findViewById(R.id.hs_sign_up_username);
        mHsEmail = (EditText) findViewById(R.id.hs_sign_up_email);
        mHsPassword = (EditText) findViewById(R.id.hs_sign_up_password);
        mHsPasswordCheck = (EditText) findViewById(R.id.hs_sign_up_password_check);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvent() {
        mBtnSignUp.setOnClickListener(this);
    }

    private String getUsername() {
        return mHsUsername.getText().toString();
    }

    private String getEmail() {
        return mHsEmail.getText().toString();
    }

    private String getPassword() {
        return mHsPassword.getText().toString();
    }

    private String getPasswordCheck() {
        return mHsPasswordCheck.getText().toString();
    }

    private boolean isAllFormFilled() {
        return !(getUsername().equals("") || getEmail().equals("") ||
                getPassword().equals("") || getPasswordCheck().equals(""));
    }

    private boolean isPasswordCheckCorrected() {
        return getPassword().equals(getPasswordCheck());
    }

    private boolean isValidUsername() {
        return getUsername().length() >= 4;
    }

    private boolean isValidEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    private boolean isValidPassword() {
        return getPassword().length() >= 6;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_sign_up:
            if (!isAllFormFilled()) {
            	showResult("isAllFormFilled");
            } else if (!isPasswordCheckCorrected()) {
            	showResult("isPasswordCheckCorrected");
            } else if (!isValidUsername()) {
            	showResult("isValidUsername");
            } else if (!isValidEmail()) {
            	showResult("isValidEmail");
            } else if (!isValidPassword()) {
            	showResult("isValidPassword");
            } else {
                executeSignUp();
            }
            break;
        }
    }

    private void executeSignUp() {
		if(mSignUpTask != null && mSignUpTask.getStatus() != mSignUpTask.getStatus().FINISHED)
			return ;
    	
    	TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CM0003 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0003.class);
				
				if(cm.getResultYn().equals("Y")){
					finish();
				} else 
					showResult("Error");
			}
		}; 
	  	CM0003 cm = new CM0003();
    	cm.setUsrId(mHsEmail.getText().toString());
    	cm.setUsrPw(mHsPassword.getText().toString());
		cm.setCustName(mHsUsername.getText().toString());
		cm.setTermsYN("Y");
		cm.setPsPlatform("AND");
		cm.setPsId("psId");
		cm.setPsRevokeYN("N");
		cm.setPsAppVer("1.0");
		cm.setDeviceNM("kitkat");
		cm.setUsrSts(1);
		
    	TransHandler<CM0003> authHandler = new TransHandler<CM0003>("CM0003", handler, cm);
    	mSignUpTask = new PostMessageTask(this, authHandler);
    	mSignUpTask.execute(MediaType.APPLICATION_JSON); 
    }
}

