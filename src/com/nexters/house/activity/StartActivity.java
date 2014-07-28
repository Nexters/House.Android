package com.nexters.house.activity;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
//import com.nexters.house.core.App;


public class StartActivity extends Activity implements View.OnClickListener {
	public static final int REQUEST_SIGN_IN = 0;
    public static final int REQUEST_SIGN_UP = 1;

    private Button mBtnSignIn;
	private Button mBtnSignUp;
	private VideoView videoViewIntro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

//        checkTutorialRead();
        initResources();
        initEvents();
        
//        playVideoIntro();
	}

//    private void checkTutorialRead() {
//        if (!App.isTutorialRead(this)) {
//            App.readTutorial(this);
//
//            Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
//            startActivity(intent);
//        }
//    }

    private void initResources() {
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
//        videoViewIntro = (VideoView) findViewById(R.id.vv_vobble_intro);
    }

    private void initEvents() {
        mBtnSignIn.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
        case R.id.btn_sign_in:
            dispatchSignIn();
            break;
        case R.id.btn_sign_up:
            dispatchSignUp();
            break;
        }
    }
    
//    private void playVideoIntro(){
//    	Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/vobble");
//    	videoViewIntro.setVideoURI(videoUri);
//    	videoViewIntro.start();
//    }

    private void dispatchSignIn() {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_IN);
    }

    private void dispatchSignUp() {
        Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                this.finish();
            }
        } else if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK) {
                dispatchSignIn();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (videoViewIntro.isPlaying())
//            videoViewIntro.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        playVideoIntro();
    }
}
