package com.nexters.house.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;

import com.nexters.house.*;
import com.nexters.house.utils.*;

public class IntroActivity extends Activity {
	private static int INTRO_LOADING_TIME = 1000;
	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);

		// default font
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/NotoSansKR-Light.otf");
		
		initResource();
	}

	private void initResource(){
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				endIntro();
			}
		}, INTRO_LOADING_TIME);
	}
	
	private void endIntro() {
		Intent intent = new Intent(this, StartActivity.class); //서버 올려진 상태일때
		startActivity(intent);
		finish();
	}
}
