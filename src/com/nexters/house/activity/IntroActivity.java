package com.nexters.house.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.nexters.house.R;
import com.nexters.house.core.AccountManager;

public class IntroActivity extends Activity {
	private static int INTRO_LOADING_TIME = 1000;
    private Handler mHandler = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);

		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
            public void run() {
                endIntro();
            }
        }, INTRO_LOADING_TIME);
	}

	private void endIntro() {
        if (AccountManager.getInstance().isSignedIn(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        }
	}
}
