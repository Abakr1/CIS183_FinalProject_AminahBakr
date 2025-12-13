package com.example.cis183_finalproject_aminahbakr;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class NavBar {

    public static final int SCREEN_HOME        = 0;
    public static final int SCREEN_SEARCH      = 1;
    public static final int SCREEN_ADDRESOURCE = 2;
    public static final int SCREEN_PROFILE     = 3;

    /**
     * Call this in onCreate() of EACH Activity that has the nav bar
     *
     * @param activity      current Activity
     * @param activeScreen  which screen is currently active
     * @param userId        logged-in user id (REQUIRED)
     */
    public static void setUpBottomNav(Activity activity, int activeScreen, long userId) {

        // These IDs MUST match the ImageView IDs in nav_bar.xml
        ImageView ivHome        = activity.findViewById(R.id.iv_v_home);
        ImageView ivSearch      = activity.findViewById(R.id.iv_v_search);
        ImageView ivAddResource = activity.findViewById(R.id.iv_v_addResource);
        ImageView ivProfile     = activity.findViewById(R.id.iv_v_profile);

        if (ivHome == null || ivSearch == null || ivAddResource == null || ivProfile == null) return;

        ivHome.setOnClickListener(v -> {
            if (activeScreen != SCREEN_HOME) {
                Intent i = new Intent(activity, Favorites.class); // or Home screen if you have one
                i.putExtra("user_id", userId);
                activity.startActivity(i);
            }
        });

        ivSearch.setOnClickListener(v -> {
            if (activeScreen != SCREEN_SEARCH) {
                Intent i = new Intent(activity, Search.class);
                i.putExtra("user_id", userId);
                activity.startActivity(i);
            }
        });

        ivAddResource.setOnClickListener(v -> {
            if (activeScreen != SCREEN_ADDRESOURCE) {
                Intent i = new Intent(activity, AddResources.class);
                i.putExtra(AddResources.EXTRA_USER_ID, userId);
                activity.startActivity(i);
            }
        });

        ivProfile.setOnClickListener(v -> {
            if (activeScreen != SCREEN_PROFILE) {
                Intent i = new Intent(activity, ManageProfile.class);
                i.putExtra("user_id", userId);
                activity.startActivity(i);
            }
        });
    }
}


