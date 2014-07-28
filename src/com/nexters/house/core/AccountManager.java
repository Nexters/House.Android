package com.nexters.house.core;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.nexters.house.entity.User;

public class AccountManager {
    private static AccountManager accountManager = new AccountManager();

    private AccountManager() {}

    public static AccountManager getInstance() {
        return accountManager;
    }

    public void signIn(Context context, User user) {
        setUserId(context, user.getUserId() + "");
        setEmail(context, user.getEmail());
        setUsername(context, user.getUsername());
        setToken(context, user.getToken());
    }

    public void signOut(Context context) {
        setToken(context, "");
        setUserId(context, "");
        setUsername(context, "");
        setEmail(context, "");
    }

    public boolean isSignedIn(Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }

    public String getUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(User.USER_ID, "");
    }

    public String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(User.TOKEN, "");
    }

    private void setUserId(Context context, String userId) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(User.USER_ID, userId).commit();
    }

    private void setEmail(Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(User.EMAIL, email).commit();
    }

    private void setUsername(Context context, String username) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(User.USERNAME, username).commit();
    }

    private void setToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(User.TOKEN, token).commit();
    }
}
