package com.nexters.house.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import com.facebook.Session;
import com.kakao.APIErrorResult;
import com.kakao.LogoutResponseCallback;
import com.kakao.UserManagement;
import com.nexters.house.activity.MainActivity;

/**
 * Created by BoBinLee on 2014-08-01.
 * 카카오톡, 페이스북, 서버로그인 부분
 */
public class SessionManager {
	public static final int NONE = -1;
	public static final int FACEBOOK = 1;
	public static final int KAKAO = 2;
	public static final int HOUSE = 3;
	
	SharedPreferences mPref;
	SharedPreferences.Editor mEditor;
	Context mContext;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "SessionPref";

	// User LoginType
	public static final String KEY_LOGIN_TYPE = "type";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "isLogin";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// User token
	public static final String KEY_TOKEN = "token";

	// User Profile
	public static final String KEY_PROFILE_PATH = "profile";
	
	// User AutoLogin
	public static final String KEY_AUTO_LOGIN = "autoLogin";
	
	public SessionManager(Context context) {
		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mPref.edit();
	}

	public static SessionManager getInstance(Context context) {
		return new SessionManager(context);
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(int loginType, String name, String email, String token, String profile, boolean isAuto) {
		// Storing type in pref
		mEditor.putInt(KEY_LOGIN_TYPE, loginType);
		
		// Storing login value as TRUE
		mEditor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		mEditor.putString(KEY_NAME, name);

		// Storing email in pref
		mEditor.putString(KEY_EMAIL, email);

		// Storing token in pref
		mEditor.putString(KEY_TOKEN, token);

		// Storing profile in pref
		mEditor.putString(KEY_PROFILE_PATH, profile);
		
		// Storing auto in pref
		mEditor.putBoolean(KEY_AUTO_LOGIN, isAuto);
		
		// commit changes
		mEditor.commit();
	}

	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			onSessionClosed();
		}
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, mPref.getString(KEY_NAME, null));

		// user email id
		user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));

		// user profile
		user.put(KEY_PROFILE_PATH, mPref.getString(KEY_PROFILE_PATH, null));
		
		// return user
		return user;
	}

	public void logoutUser() {
		// Clearing all data from Shared Preferences
		boolean isKakao = com.kakao.Session.getCurrentSession().isOpened();
		boolean isFacebook = com.facebook.Session.getActiveSession().isOpened();
		boolean isUser = isLoggedIn();

		Log.d("isUser", "isUser : " + isUser);
		
		mEditor.clear();
		mEditor.commit();
		if (isKakao) {
			UserManagement.requestLogout(new LogoutResponseCallback() {
				@Override
				protected void onSuccess(final long userId) {
					onSessionClosed();
				}
				@Override
				protected void onFailure(final APIErrorResult apiErrorResult) {
				}
			});
		} else if (isFacebook) {
			Session session = Session.getActiveSession();
			if (session != null && !session.isClosed()) {
				session.closeAndClearTokenInformation();
			}
			onSessionClosed();
		} else if (isUser) {
			onSessionClosed();
		}
	}

	public void onSessionClosed() {
		final Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("logout", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all
															// activities
		mContext.startActivity(intent);
	}

	public boolean checkAutoLogin(){
		boolean isAutoLogin = mPref.getBoolean(KEY_AUTO_LOGIN, false);
		return isAutoLogin;
	}
	
	public int getLoginType(){
		return mPref.getInt(KEY_LOGIN_TYPE, NONE);
	}
	
	public boolean isLoggedIn() {
		return mPref.getBoolean(IS_LOGIN, false);
	}
}
