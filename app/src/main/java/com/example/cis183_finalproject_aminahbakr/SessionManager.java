package com.example.cis183_finalproject_aminahbakr;

import android.content.Context;

public class SessionManager {

    private static final String PREF_NAME = "resources_prefs";
    private static final String KEY_USERNAME = "logged_in_username";
    private static final String KEY_USER_ID = "logged_in_user_id";

    // this is needed
    public static void saveLoggedInUser(Context context, long userId, String username) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    // this has to go after to not break it
    public static void savedLoggedInUser(Context context, String username) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_USERNAME, username)
                .apply();
    }

    public static String getLoggedInUser(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_USERNAME, null);
    }

    public static long getUserId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getLong(KEY_USER_ID, -1);
    }

    public static void clearSession(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_USERNAME)
                .remove(KEY_USER_ID)
                .apply();
    }
}
