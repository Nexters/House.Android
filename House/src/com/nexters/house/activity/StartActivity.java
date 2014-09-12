package com.nexters.house.activity;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.nexters.house.R;
//import com.nexters.house.core.App;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.reqcode.CM0001;
import com.nexters.house.entity.reqcode.CM0003;
import com.nexters.house.entity.reqcode.CM0003.CM0003Img;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.CommonTask;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class StartActivity extends AbstractAsyncActivity implements View.OnClickListener {
	public static final int REQUEST_SIGN_IN = 0;
	public static final int REQUEST_SIGN_UP = 1;

	private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";
	private boolean userSkippedLogin = false;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;

	private Button mBtnSignIn;
	private Button mBtnSignUp;
	private com.facebook.widget.LoginButton mBtnFacebook;
	private com.kakao.widget.LoginButton mBtnKakao;
	private ImageView imageViewIntro;

	PostMessageTask mSignUpTask;
	PostMessageTask mSignInTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, facebookCallback);
		uiHelper.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

		initResource();
		// 로그인 관련 초기화 
		initLogin();
		initEvent();
	}

	private void initResource() {
		mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
		mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
		mBtnKakao = (com.kakao.widget.LoginButton) findViewById(R.id.btn_kakao_in);
		mBtnFacebook = (com.facebook.widget.LoginButton) findViewById(R.id.btn_facebook_in);
		imageViewIntro = (ImageView) findViewById(R.id.iv_house_intro);
	}

	private void initLogin(){
		// Settings
		// 세션을 초기화 한다
		if (com.kakao.Session.initializeSession(this, kakaoCallback)) {
			// 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
			mBtnKakao.setVisibility(View.GONE);
		}
		// Check autoLogin
		SessionManager sessionManager = SessionManager.getInstance(this);
		if (sessionManager.isLoggedIn() && !sessionManager.checkAutoLogin()){
			sessionManager.logoutUser();
			finish();
		}
	}
	
	private void initEvent() {
		mBtnSignIn.setOnClickListener(this);
		mBtnSignUp.setOnClickListener(this);
		mBtnKakao.setLoginSessionCallback(kakaoCallback);
	}

	protected void onSessionOpened() {
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		if (SessionManager.getInstance(this).isLoggedIn()) {
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
	
	// -------------------------- 연동 로그인 부분
	private void register(final String usrId, final String userPw, final String name, final String imgUrl, final int sessionType, byte[] imgs){
		if(mSignUpTask != null && mSignUpTask.getStatus() != mSignUpTask.getStatus().FINISHED)
			return ;
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			@Override
			public void handle(APICode resCode) {
				CM0003 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0003.class);
				
				if(cm.getResultYn().equals("Y")){
					login(usrId, userPw, name, imgUrl, sessionType);
				} else 
					showResult("Error");
			}
		}; 
	  	CM0003 cm = new CM0003();
    	cm.setUsrId(usrId);
    	cm.setUsrPw(userPw);
		cm.setCustName(name);
		cm.setTermsYN("Y");
		cm.setPsPlatform("AND");
		cm.setPsId("psId");
		cm.setPsRevokeYN("N");
		cm.setPsAppVer("1.0");
		cm.setDeviceNM("kitkat");
		cm.setUsrSts(1);
		
		CM0003Img profileImg = new CM0003Img();
		Log.e("img", "imgs : " + imgUrl);
		
		profileImg.imgContent = imgs;
		profileImg.imgNm = profileImg.imgOriginNm = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
		profileImg.imgSize = imgs.length;
		profileImg.imgType = imgUrl.substring(imgUrl.lastIndexOf('.') + 1);
		cm.setUsrImg(profileImg);
		
    	TransHandler<CM0003> authHandler = new TransHandler<CM0003>("CM0003", handler, cm);
    	mSignUpTask = new PostMessageTask(this, authHandler);
    	mSignUpTask.execute(MediaType.APPLICATION_JSON); 
	}
	
	private void login(final String usrId, final String userPw, final String name, final String imgUrl, final int sessionType){
			if(mSignInTask != null && mSignInTask.getStatus() != mSignInTask.getStatus().FINISHED)
				return ;
		
	  		TransHandler.Handler handler = new TransHandler.Handler() {
				@Override
				public void handle(APICode resCode) {
					Log.d("login", "login 1");
					if(resCode.getTranData().size() > 0){
						CM0001 cm = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), CM0001.class);
						SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
						
						String baseUrl = getString(R.string.base_uri);
						String profileImg = null;
						if(cm.getProfileImg() != null)
							profileImg = baseUrl + cm.getProfileImg();
						sessionManager.createLoginSession(sessionType, cm.getCustName(), cm.getUsrId(), cm.getToken(), profileImg, true);
						onSessionOpened();
					} else {
						final Bundle bundle = new Bundle();
						
						CommonTask.Handler handler = new CommonTask.Handler() {
							@Override
							public void handle() {
								HttpHeaders requestHeaders = new HttpHeaders();
						        HttpEntity requestEntity = new HttpEntity(requestHeaders);

						        // Create a new RestTemplate instance
						        RestTemplate restTemplate = new RestTemplate();
						        restTemplate
						                .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
						        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
						        ResponseEntity<byte[]> response = null;
						        response = restTemplate.getForEntity(imgUrl, byte[].class);
						        bundle.putByteArray("imgs", response.getBody());
							}
						};
						CommonTask.PostHandler postHandler = new CommonTask.PostHandler() {
							@Override
							public void handle() {
								register(usrId, userPw, name, imgUrl, sessionType, bundle.getByteArray("imgs"));
							}
						};
						new CommonTask(null, handler, postHandler).execute();
					}
				}
			}; 
		  	CM0001 cm = new CM0001();
		  	cm.setUsrId(usrId);
	    	cm.setUsrPw(userPw);
			
	    	TransHandler<CM0001> authHandler = new TransHandler<CM0001>("CM0001", handler, cm);
	    	mSignInTask = new PostMessageTask(this, authHandler);
	    	mSignInTask.execute(MediaType.APPLICATION_JSON); 
	}
	
	private Session.StatusCallback facebookCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session.isOpened()) {
				// make request to the /me API
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (user != null) {
									// facebook Id 로 - email은 ㄴㄴ
									Log.d("email", 	"email : "
													+ user.getName()
													+ ","
													+ user.getUsername()
													+ ","
													+ user.getId()
													+ ","
													+ user.getLink()
													+ ","
													+ user.getFirstName()
													+ ","
													+ user.getProperty("gender"));
									if(!SessionManager.getInstance(StartActivity.this).isLoggedIn())
										login("facebook" + user.getId(), "facebook" + user.getId(), user.getName(), "http://graph.facebook.com/" + user.getId() + "/picture", SessionManager.FACEBOOK);
								}
							}
						});
				request.executeAsync();
			}
		}
	};
	private final SessionCallback kakaoCallback = new SessionCallback() {
		@Override
		public void onSessionOpened() {
//			showShortToast("Session Opened");
			UserManagement.requestMe(new MeResponseCallback() {
				@Override
				protected void onSuccess(final UserProfile userProfile) {
					Log.d("user",
							"Email kakao : " + userProfile.getId() + " User : "
									+ userProfile.getNickname()
									+ " thumbnailpath : "
									+ userProfile.getThumbnailImagePath());
					if(!SessionManager.getInstance(StartActivity.this).isLoggedIn())
						login("kakao" + userProfile.getId(), "kakao" + userProfile.getId(), userProfile.getNickname(), userProfile.getThumbnailImagePath(), SessionManager.KAKAO);
				}

				@Override
				protected void onNotSignedUp() {
					showShortToast("가입 페이지로 이동");
				}

				@Override
				protected void onSessionClosedFailure(
						final APIErrorResult errorResult) {
					showShortToast("다시 로그인 시도");
				}

				@Override
				protected void onFailure(final APIErrorResult errorResult) {
					Toast.makeText(getApplicationContext(),
							"failed to update profile. msg = " + errorResult,
							Toast.LENGTH_LONG).show();
				}
			});
		}

		@Override
		public void onSessionClosed(KakaoException exception) {
			exception.printStackTrace();
			// 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
//			showShortToast("Session onSessionClosed");
			mBtnKakao.setVisibility(View.VISIBLE);
		}
	};
}
