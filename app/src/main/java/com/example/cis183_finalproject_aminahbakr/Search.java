package com.example.cis183_finalproject_aminahbakr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Search extends AppCompatActivity {

    DatabaseHelper db;
    long currentUserId;

    // Category checkboxes
    CheckBox cb_j_search_Food;
    CheckBox cb_j_search_shelter;
    CheckBox cb_j_search_health;

    // City radios
    RadioButton rb_j_search_Monroe;
    RadioButton rb_j_search_ypsi;

    // Search button
    Button btn_j_search_search;

    // Results container
    private LinearLayout resultsContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        db = new DatabaseHelper(this);

        // Session → user
        currentUserId = SessionManager.getUserId(this);

        // Bind views
        cb_j_search_Food = findViewById(R.id.cb_v_search_food);
        cb_j_search_shelter = findViewById(R.id.cb_v_search_shelter);
        cb_j_search_health = findViewById(R.id.cb_v_search_health);

        rb_j_search_Monroe = findViewById(R.id.rb_v_search_monroe);
        rb_j_search_ypsi = findViewById(R.id.rb_v_search_ypsi);

        btn_j_search_search = findViewById(R.id.btn_v_search_search);

        resultsContainer = findViewById(R.id.ll_v_search_results);

        btn_j_search_search.setOnClickListener(v -> runSearch());

        NavBar.setUpBottomNav(this, NavBar.SCREEN_SEARCH);
    }

    private void runSearch() {
        resultsContainer.removeAllViews();

        // City
        String city = null;
        if (rb_j_search_Monroe.isChecked()) city = "Monroe";
        else if (rb_j_search_ypsi.isChecked()) city = "Ypsilanti";

        if (city == null) {
            Toast.makeText(this, "Select a city", Toast.LENGTH_SHORT).show();
            return;
        }

        // Category
        long categoryId = -1;
        if (cb_j_search_Food.isChecked()) categoryId = db.getCategoryIdByName("Food");
        else if (cb_j_search_shelter.isChecked()) categoryId = db.getCategoryIdByName("Shelter");
        else if (cb_j_search_health.isChecked()) categoryId = db.getCategoryIdByName("Health");

        if (categoryId == -1) {
            Toast.makeText(this, "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = db.getResourcesByCityAndCategory(city, categoryId);
        if (c == null || !c.moveToFirst()) {
            showEmpty();
            return;
        }

        do {
            long resourceId = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ID));
            String org = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME));
            String address = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS));

            View row = buildResultRow(org, address);

            // Tap → Details
            row.setOnClickListener(v -> {
                Intent i = new Intent(Search.this, Details.class);
                i.putExtra("resource_id", resourceId);
                startActivity(i);
            });

            // Long-press → Favorite
            row.setOnLongClickListener(v -> {
                new AlertDialog.Builder(Search.this)
                        .setTitle("Add to favorites?")
                        .setPositiveButton("Add", (d, w) -> {
                            db.addFavorite(currentUserId, resourceId,
                                    new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
                            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            });

            resultsContainer.addView(row);

        } while (c.moveToNext());

        c.close();
    }

    private View buildResultRow(String org, String address) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(32, 24, 32, 24);

        TextView tvOrg = new TextView(this);
        tvOrg.setText(org);
        tvOrg.setTextSize(16f);
        tvOrg.setGravity(Gravity.START);

        TextView tvAddr = new TextView(this);
        tvAddr.setText(address);
        tvAddr.setTextSize(13f);

        row.addView(tvOrg);
        row.addView(tvAddr);

        return row;
    }

    private void showEmpty() {
        TextView tv = new TextView(this);
        tv.setText("No results found");
        tv.setPadding(32, 32, 32, 32);
        resultsContainer.addView(tv);
    }
}

