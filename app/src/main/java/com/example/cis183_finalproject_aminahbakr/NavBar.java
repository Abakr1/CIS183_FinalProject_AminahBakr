package com.example.cis183_finalproject_aminahbakr;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class NavBar {

    //I probably have to add make a seperate xml screen for the nav bar to work properly
    public static final int SCREEN_HOME      = 0;
    public static final int SCREEN_SEARCH    = 1;
    public static final int SCREEN_ADDRESOURCE = 2;
    public static final int SCREEN_PROFILE   = 3;

    public static void setUpBottomNav(Activity activity, int activeScreen) {

        // ✅ CHANGE these ids to the ImageView ids in your navbar
        ImageView ivHome = activity.findViewById(R.id.iv_v_home);
        ImageView ivSearch = activity.findViewById(R.id.iv_v_search);
        ImageView ivFavorites = activity.findViewById(R.id.iv_v_addResource);
        ImageView ivProfile = activity.findViewById(R.id.iv_v_profile);

        if (ivHome == null || ivSearch == null || ivFavorites == null || ivProfile == null) return;

        ivHome.setOnClickListener(v -> {
            if (activeScreen != SCREEN_HOME) {
                activity.startActivity(new Intent(activity, Favorites.class)); // <-- change if needed
            }
            activity.finish();
        });

        ivSearch.setOnClickListener(v -> {
            if (activeScreen != SCREEN_SEARCH) {
                activity.startActivity(new Intent(activity, Search.class));
            }
            activity.finish();
        });

        ivFavorites.setOnClickListener(v -> {
            if (activeScreen != SCREEN_ADDRESOURCE) {
                activity.startActivity(new Intent(activity, AddResources.class));
            }
            activity.finish();
        });

        ivProfile.setOnClickListener(v -> {
            if (activeScreen != SCREEN_PROFILE) {
                activity.startActivity(new Intent(activity, ManageProfile.class));
            }
            activity.finish();
        });
    }
}

