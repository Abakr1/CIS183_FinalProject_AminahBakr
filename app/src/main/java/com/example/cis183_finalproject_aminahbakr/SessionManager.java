package com.example.cis183_finalproject_aminahbakr;

import android.content.Context;

public class SessionManager {
    static final String PREF_NAME = "resources_prefs";
    static final String KEY_USERNAME = "logged_in_username";

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

    public static void clearSession(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_USERNAME)
                .apply();
    }
}
