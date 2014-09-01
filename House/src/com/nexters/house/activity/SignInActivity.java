package com.nexters.house.activity;

import org.springframework.http.MediaType;

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
import com.nexters.house.entity.reqcode.CM0001;
import com.nexters.house.handler.AuthHandler;
import com.nexters.house.thread.PostMessageTask;

public class SignInActivity extends AbstractAsyncActivity implements View.OnClickListener {
    private EditText mHsEmail;
    private EditText mHsPassword;
    private Button mBtnSignIn;
    private CheckBox mCbAutoLogin;
    private boolean mAutoLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);

        initResources();
        initEvents();
    }

    private void initResources() {
        mHsEmail = (EditText) findViewById(R.id.hs_sign_in_email);
        mHsPassword = (EditText) findViewById(R.id.hs_sign_in_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
    }

    private void initEvents() {
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
    	testLogin();
//    	CM0001 cm = new CM0001();
//    	cm.setUsrId(mHsEmail.getText().toString());
//    	cm.setUsrPw(mHsPassword.getText().toString());
//
//    	AuthHandler<CM0001> authHandler = new AuthHandler<CM0001>(this, "CM0001");
//    	authHandler.setLoginAuto(mAutoLogin);
//    	authHandler.addTranData(cm);
//    	
//    	PostMessageTask signInTask = new PostMessageTask(this, authHandler, AuthHandler.LOGIN_METHOD);
//    	signInTask.execute(MediaType.APPLICATION_JSON); 
    }
}
