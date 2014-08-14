package com.nexters.house.activity;

import org.json.JSONObject;

import com.nexters.house.R;
import com.nexters.house.core.SessionManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SignInActivity extends FragmentActivity implements View.OnClickListener {
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

    private void executeSignIn() {
//        String url = URL.SIGN_IN;
//
//        RequestParams params = new RequestParams();
//        params.put(User.EMAIL, getEmail());
//        params.put(User.PASSWORD, getPassword());
//
//        HttpUtil.post(url, null, params, new APIResponseHandler(SignInActivity.this) {
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                showLoading();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoading();
//            }
//
//            @Override
//            public void onSuccess(JSONObject response) {
//                AccountManager.getInstance().signIn(SignInActivity.this, User.build(response));
//
//                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                startActivity(intent);
//                setResult(RESULT_OK, null);
//                finish();
//            }
//        });
        SessionManager sessionManager = SessionManager.getInstance(this);
        sessionManager.createLoginSession(SessionManager.HOUSE ,"BoBinLee", "cultist_tp@naver.com", "1234", "123.png", mAutoLogin);
        finish();
    }
}
