package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Favorites extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private long userId = -1L;

    private LinearLayout favContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        dbHelper = new DatabaseHelper(this);

        userId = getIntent().getLongExtra("user_id", -1L);
        if (userId == -1L) {
            Toast.makeText(this, "Missing user. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        favContainer = findViewById(R.id.ll_v_favorites_results);

        loadFavorites();

        NavBar.setUpBottomNav(this, NavBar.SCREEN_HOME, userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh list after coming back from Details (saved/unsaved)
        loadFavorites();
    }

    private void loadFavorites() {
        favContainer.removeAllViews();

        Cursor c = dbHelper.getFavoriteResourcesCursor(userId);

        if (c != null && c.moveToFirst()) {
            do {
                long resourceId = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ID));
                String org = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME));
                String address = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS));

                TextView tv = new TextView(this);
                tv.setText(org + " — " + address);
                tv.setTextSize(16);
                tv.setPadding(16, 16, 16, 16);

                tv.setTag(resourceId);

                tv.setOnClickListener(v -> openDetails((long) v.getTag()));

                favContainer.addView(tv);

            } while (c.moveToNext());
            c.close();
        } else {
            if (c != null) c.close();
            TextView empty = new TextView(this);
            empty.setText("No favorites yet.");
            empty.setPadding(16, 16, 16, 16);
            favContainer.addView(empty);
        }
    }

    private void openDetails(long resourceId) {
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("resource_id", resourceId);
        startActivity(intent);
    }
}



