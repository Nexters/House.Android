package com.nexters.house.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.nexters.house.R;
//import com.nexters.house.core.App;
import com.nexters.house.core.SessionManager;


public class StartActivity extends Activity implements View.OnClickListener {
    public static final int REQUEST_SIGN_IN = 0;
    public static final int REQUEST_SIGN_UP = 1;

    private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";
    private boolean userSkippedLogin = false;
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback facebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (session.isOpened()) {
                // make request to the /me API
                Request.newMeRequest(session, new Request.GraphUserCallback() {
                    // callback after Graph API response with user
                    // object
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                        	Log.d("user", "User : " + user.getName() + " thumbnailpath : " + user.getId());
                        	SessionManager.getInstance(StartActivity.this).createLoginSession(SessionManager.FACEBOOK, user.getName(), null, com.facebook.Session.getActiveSession().getAccessToken(), user.getId(), true);
                            finish();
                        }
                    }
                }).executeAsync();
            }
        }
    };
    private final SessionCallback kakaoCallback = new SessionCallback(){
        @Override
        public void onSessionOpened() {
//            showShortToast("Session Opened");
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                protected void onSuccess(final UserProfile userProfile) {
//                	userProfile.getId()
                    // 성공.
                    Log.d("user", "User : " + userProfile.getNickname() + " thumbnailpath : " + userProfile.getThumbnailImagePath());
                    SessionManager.getInstance(StartActivity.this).createLoginSession(SessionManager.KAKAO, userProfile.getNickname(), null, com.kakao.Session.getCurrentSession().getAccessToken(), userProfile.getThumbnailImagePath(), true);
                    finish();
                }
                @Override
                protected void onNotSignedUp() {
                    // 가입 페이지로 이동
                    showShortToast("가입 페이지로 이동");
                }
                @Override
                protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                    // 다시 로그인 시도
                    showShortToast("다시 로그인 시도");
                }
                @Override
                protected void onFailure(final APIErrorResult errorResult) {
                    // 실패
                    Toast.makeText(getApplicationContext(), "failed to update profile. msg = " + errorResult, Toast.LENGTH_LONG).show();
                }
            });
        }
        @Override
        public void onSessionClosed(KakaoException exception) {
            exception.printStackTrace();
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
//            showShortToast("Session onSessionClosed");
            mBtnKakao.setVisibility(View.VISIBLE);
        }
    };
    private Button mBtnSignIn;
    private Button mBtnSignUp;
    private LoginButton mBtnKakao;
    private VideoView videoViewIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, facebookCallback);
        uiHelper.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        initResources();
        initEvents();
        playVideoIntro();
    }

    private void initResources() {
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mBtnKakao = (LoginButton) findViewById(R.id.btn_kakao_in);
        videoViewIntro = (VideoView) findViewById(R.id.vv_house_intro);
        
//      Settings
        // 세션을 초기화 한다
        if(com.kakao.Session.initializeSession(this, kakaoCallback)){
            // 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
            mBtnKakao.setVisibility(View.GONE);
        }
        
        // Check autoLogin
        SessionManager sessionManager = SessionManager.getInstance(this);
        if(sessionManager.isLoggedIn() && !sessionManager.checkAutoLogin())
        	sessionManager.logoutUser(); 
    }

    private void initEvents() {
        mBtnSignIn.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnKakao.setLoginSessionCallback(kakaoCallback);
    }

    protected void onSessionOpened(){
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                dispatchSignIn();
                break;
            case R.id.btn_sign_up:
                dispatchSignUp();
                break;
        }
    }

    private void playVideoIntro() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/house");
        videoViewIntro.setVideoURI(videoUri);
//        videoViewIntro.start();
    }

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
        uiHelper.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivitiResult", "onActivitiResult: " + (data));

        if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK) {
                dispatchSignIn();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();

        if (videoViewIntro.isPlaying())
            videoViewIntro.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        playVideoIntro();

        if (com.kakao.Session.getCurrentSession().isOpened() || com.facebook.Session.getActiveSession().isOpened() || SessionManager.getInstance(this).isLoggedIn()){
            // 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
            onSessionOpened();
        }
        // 3. else 로그인 창이 보인다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putBoolean(USER_SKIPPED_LOGIN_KEY, userSkippedLogin);
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
