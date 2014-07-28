package com.nexters.house.activity;

import com.nexters.vobble.core.AccountManager;
import com.nexters.vobble.entity.User;
import com.nexters.vobble.network.APIResponseHandler;
import org.json.JSONObject;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.nexters.house.R;
//import com.nexters.house.network.HttpUtil;
//import com.nexters.house.network.URL;

public class SignInActivity extends Activity implements View.OnClickListener {
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnSignIn;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signin);
		
        initResources();
        initEvents();
	}


    private void initResources() {
        mEtEmail = (EditText) findViewById(R.id.et_sign_in_email);
        mEtPassword = (EditText) findViewById(R.id.et_sign_in_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
    }

    private void initEvents() {
        mBtnSignIn.setOnClickListener(this);
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
//
//    private String getPassword() {
//        return mEtPassword.getText().toString();
//    }
//
//    private String getEmail() {
//        return mEtEmail.getText().toString();
//    }
//
//    private boolean isAllFormsFilled() {
//        return !getEmail().equals("") && !getPassword().equals("");
//    }
//
//    private boolean isValidEmail() {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
//    }
//
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
               Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
//                setResult(RESULT_OK, null);
               finish();
            }
//        });
//    }
}
