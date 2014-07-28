package com.nexters.house.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;

public class SignUpActivity extends Activity implements View.OnClickListener {
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnSignIn;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
