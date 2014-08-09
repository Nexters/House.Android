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
	// Shared Preferences
	SharedPreferences mPref;

	// Editor for Shared preferences
	SharedPreferences.Editor mEditor;

	// Context
	Context mContext;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "SessionPref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";

	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// User token
	public static final String KEY_TOKEN = "token";

	// User Profile
	public static final String KEY_PROFILE_PATH = "profile";
	
	// Constructor
	public SessionManager(Context context) {
		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mPref.edit();
	}

	// Instance
	public static SessionManager getInstance(Context context) {
		return new SessionManager(context);
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String email, String token, String profile) {
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
		
		// commit changes
		mEditor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(mContext, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			mContext.startActivity(i);
		}
	}

	/**
	 * Get stored session data
	 * */
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

	/**
	 * Clear session details
	 * */
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

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return mPref.getBoolean(IS_LOGIN, false);
	}
}
