package com.nexters.house.entity;


import org.json.JSONObject;

public class User {

    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    private int userId;
    private String email;
    private String username;
    private String token;

    public static User build(JSONObject json) {
        if (json == null) {
            return null;
        }

        User user = new User();
        user.userId = json.optInt(USER_ID);
        user.username = json.optString(USERNAME);
        user.email = json.optString(EMAIL);
        user.token = json.optString(TOKEN);
        return user;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
