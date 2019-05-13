package com.fossasia.unesco.popular;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * User session management using shared preferences
 */
public class Preferences {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "loginPref";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    @SuppressLint("CommitPrefEdits")
    public Preferences (Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    } // ... Check if user is logged in or not

    public void createLoginSession(String name, String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    } // ... Save logged in user details in shared preferences

    public String getCurrentUserName() {
        return pref.getString(KEY_NAME, null);
    }

    public String getCurrentUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    } // ... Clear shared preferences after logout
}
