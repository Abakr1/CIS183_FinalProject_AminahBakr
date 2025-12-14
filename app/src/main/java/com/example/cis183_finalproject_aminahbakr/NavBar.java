package com.example.cis183_finalproject_aminahbakr;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

public class NavBar {

    public static final int SCREEN_HOME = 0;
    public static final int SCREEN_SEARCH = 1;
    public static final int SCREEN_ADDRESOURCE = 2;
    public static final int SCREEN_PROFILE = 3;

    // INCLUDE THE USERID DUMMY
    public static void setUpBottomNav(Activity activity, int activeScreen, long userId) {

        ImageView iv_j_Home    = activity.findViewById(R.id.iv_v_home);
        ImageView iv_j_Search  = activity.findViewById(R.id.iv_v_search);
        ImageView iv_j_Add     = activity.findViewById(R.id.iv_v_addResource);
        ImageView iv_j_Profile = activity.findViewById(R.id.iv_v_profile);

        if (iv_j_Home == null || iv_j_Search == null || iv_j_Add == null || iv_j_Profile == null) {
            Toast.makeText(activity,
                    "NavBar IDs not found. Check nav_bar.xml",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // If userId is missing, force them to login
        if (userId <= 0) {
            Toast.makeText(activity, "Session missing. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
            activity.finish();
            return;
        }

        iv_j_Home.setOnClickListener(v -> {
            if (activeScreen != SCREEN_HOME) {
                Intent i = new Intent(activity, Favorites.class);
                i.putExtra("userId", userId);
                activity.startActivity(i);
                activity.finish();
            }
        });

        iv_j_Search.setOnClickListener(v -> {
            if (activeScreen != SCREEN_SEARCH) {
                Intent i = new Intent(activity, Search.class);
                i.putExtra("userId", userId);
                activity.startActivity(i);
                activity.finish();
            }
        });

        iv_j_Add.setOnClickListener(v -> {
            if (activeScreen != SCREEN_ADDRESOURCE) {
                Intent i = new Intent(activity, AddResources.class);
                i.putExtra("userId", userId);
                activity.startActivity(i);
                activity.finish();
            }
        });

        iv_j_Profile.setOnClickListener(v -> {
            if (activeScreen != SCREEN_PROFILE) {
                Intent i = new Intent(activity, ManageProfile.class);
                i.putExtra("userId", userId);
                activity.startActivity(i);
                activity.finish();
            }
        });
    }
}
