package com.nexters.house.activity;

import android.app.Activity;

import org.json.JSONObject;

import com.nexters.house.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends Activity implements View.OnClickListener {

    private EditText mHsUsername;
    private EditText mHsEmail;
    private EditText mHsPassword;
    private EditText mHsPasswordCheck;
    private Button mBtnSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        initResources();
        initEvents();
   }
	
    private void initResources() {
        mHsUsername = (EditText) findViewById(R.id.hs_sign_up_username);
        mHsEmail = (EditText) findViewById(R.id.hs_sign_up_email);
        mHsPassword = (EditText) findViewById(R.id.hs_sign_up_password);
        mHsPasswordCheck = (EditText) findViewById(R.id.hs_sign_up_password_check);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
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
//                showAlert(R.string.error_fill_in_all_forms);
            } else if (!isPasswordCheckCorrected()) {
//                showAlert(R.string.error_not_match_password_check);
            } else if (!isValidUsername()) {
//                showAlert(R.string.error_invalid_username);
            } else if (!isValidEmail()) {
//                showAlert(R.string.error_invalid_email);
            } else if (!isValidPassword()) {
//                showAlert(R.string.error_invalid_password);
            } else {
                executeSignUp();
            }
            break;
        }
    }

    private void executeSignUp() {
//        String url = URL.SIGN_UP;
//
//        RequestParams params = new RequestParams();
//        params.put(User.EMAIL, getEmail());
//        params.put(User.PASSWORD, getPassword());
//        params.put(User.USERNAME, getUsername());
//
//        HttpUtil.post(url, null, params, new APIResponseHandler(SignUpActivity.this) {
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
//			@Override
//			public void onSuccess(JSONObject response) {
//                setResult(Activity.RESULT_OK);
//                finish();
//			}
//        });
    }
}

