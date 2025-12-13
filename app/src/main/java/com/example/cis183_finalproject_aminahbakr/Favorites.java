package com.example.cis183_finalproject_aminahbakr;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Favorites extends AppCompatActivity {

    public static final String EXTRA_RESOURCE_ID = "extra_resource_id";

    private DatabaseHelper db;
    private LinearLayout container;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        db = new DatabaseHelper(this);


        container = findViewById(R.id.ll_v_favorites_container);

        //need to load session manager here
         currentUserId = SessionManager.getUserId(this);

         //if they cant find userID look up username
        if(currentUserId == -1) {
            String username = SessionManager.getLoggedInUser(this);
            if(username == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            currentUserId = db.getUserIdByUsername(username);
        }

        //for the bottom nav
        NavBar.setUpBottomNav(this, NavBar.SCREEN_FAVORITES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        container.removeAllViews();

        Cursor c = db.getFavoriteResourcesCursor(currentUserId);

        try {
            if (c == null || !c.moveToFirst()) {
                TextView empty = new TextView(this);
                empty.setText("No favorites yet.");
                empty.setPadding(dp(16), dp(16), dp(16), dp(16));
                container.addView(empty);
                return;
            }

            do {
                final long resourceId = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ID));
                String org = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME));
                String city = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_CITY));
                String address = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS));

                View row = buildFavoriteRow(org, city, address);

                // tap to go to details
                row.setOnClickListener(v -> {
                    Intent i = new Intent(Favorites.this, Details.class);
                    i.putExtra(EXTRA_RESOURCE_ID, resourceId);
                    startActivity(i);
                });

                // long press to delete favorite
                row.setOnLongClickListener(v -> {
                    new AlertDialog.Builder(Favorites.this)
                            .setTitle("Remove favorite?")
                            .setMessage("Delete this from favorites?")
                            .setPositiveButton("Remove", (dialog, which) -> {
                                db.removeFavorite(currentUserId, resourceId);
                                loadFavorites();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    return true;
                });

                container.addView(row);

            } while (c.moveToNext());

        } finally {
            if(c != null) c.close();
        }
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
    }

    private void addEmpty(String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setPadding(32, 32, 32, 32);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(tv);
    }

    private View buildFavoriteRow(String org, String city, String address) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(14), dp(14), dp(14));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(dp(12), dp(10), dp(12), dp(0));
        card.setLayoutParams(lp);

        TextView title = new TextView(this);
        title.setText((org == null || org.trim().isEmpty()) ? "(No name)" : org);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        title.setPadding(0, 0, 0, dp(4));

        TextView sub = new TextView(this);
        String line = "";
        if (city != null && !city.trim().isEmpty()) line += city.trim();
        if (address != null && !address.trim().isEmpty()) {

            if (!line.isEmpty()) line += " • ";
                line += address.trim();
        }

        sub.setText(line.isEmpty() ? " " : line);
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        TextView hint = new TextView(this);
        hint.setText("Tap to view • Long press to remove");
        hint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        hint.setGravity(Gravity.END);
        hint.setPadding(0, dp(8), 0, 0);

        card.addView(title);
        card.addView(sub);
        card.addView(hint);


        return card;
    }
}


