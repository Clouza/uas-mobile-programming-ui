package com.example.uiclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static String SHARED_PREFERENCES_NAME = "UserInfo";
    private static String USER_ID = "userId";
    private static String API_KEY = "apiKey";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context _context;

    public Storage(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void removeUserId() {
        editor.remove(USER_ID);
        editor.apply();
    }

    public void saveApiKey(String apiKey) {
        editor.putString(API_KEY, apiKey);
        editor.apply();
    }

    public String getApiKey() {
        return sharedPreferences.getString(API_KEY, null);
    }

    public void removeApiKey() {
        editor.remove(API_KEY);
        editor.apply();
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
