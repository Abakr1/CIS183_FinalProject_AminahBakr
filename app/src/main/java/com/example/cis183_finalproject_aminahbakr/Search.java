package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private long userId = -1L;

    private CheckBox cb_j_search_food;
    private CheckBox cb_j_search_shelter;
    private CheckBox cb_j_search_health;

    private RadioButton rb_j_search_monroe;
    private RadioButton rb_j_search_ypsi;

    private Button btn_j_search_search;
    private LinearLayout resultsContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        dbHelper = new DatabaseHelper(this);

        // correct key + session fallback
        userId = getIntent().getLongExtra("userId", -1L);
        if (userId == -1L) userId = SessionManager.getUserId(this);

        if (userId == -1L) {
            Toast.makeText(this, "Missing user. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cb_j_search_food    = findViewById(R.id.cb_v_search_food);
        cb_j_search_shelter = findViewById(R.id.cb_v_search_shelter);
        cb_j_search_health  = findViewById(R.id.cb_v_search_health);

        rb_j_search_monroe  = findViewById(R.id.rb_v_search_monroe);
        rb_j_search_ypsi    = findViewById(R.id.rb_v_search_ypsi);

        btn_j_search_search = findViewById(R.id.btn_v_search_search);
        resultsContainer    = findViewById(R.id.ll_v_search_results);

        btn_j_search_search.setOnClickListener(v -> runSearch());

        NavBar.setUpBottomNav(this, NavBar.SCREEN_SEARCH, userId);
    }

    private void runSearch() {
        String city = getSelectedCity();

        if (city == null) {
            Toast.makeText(this, "Please choose Monroe or Ypsilanti", Toast.LENGTH_SHORT).show();
            return;
        }

        long[] categoryIds = getSelectedCategoryIds();
        if (categoryIds.length == 0) {
            Toast.makeText(this, "Select at least one category", Toast.LENGTH_SHORT).show();
            return;
        }

        resultsContainer.removeAllViews();

        Cursor c = dbHelper.getResourcesByCityAndCategories(city, categoryIds);

        if (c != null && c.moveToFirst()) {
            do {
                long resourceId = c.getLong(
                        c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ID)
                );
                String org = c.getString(
                        c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME)
                );
                String address = c.getString(
                        c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS)
                );

                TextView tv = new TextView(this);
                tv.setText(org + " — " + address);
                tv.setTextSize(16);
                tv.setPadding(16, 16, 16, 16);

                tv.setTag(resourceId);
                tv.setOnClickListener(v -> openDetails((long) v.getTag()));

                resultsContainer.addView(tv);

            } while (c.moveToNext());

            c.close();
        } else {
            if (c != null) c.close();
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDetails(long resourceId) {
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("userId", userId);
        intent.putExtra("resourceId", resourceId);
        startActivity(intent);
    }

    private String getSelectedCity() {
        if (rb_j_search_monroe != null && rb_j_search_monroe.isChecked()) return "Monroe";
        if (rb_j_search_ypsi != null && rb_j_search_ypsi.isChecked()) return "Ypsilanti";
        return null;
    }

    private long[] getSelectedCategoryIds() {
        long foodId = dbHelper.getCategoryIdByName("Food");
        long shelterId = dbHelper.getCategoryIdByName("Shelter");
        long healthId = dbHelper.getCategoryIdByName("Health");

        long[] temp = new long[3];
        int count = 0;

        if (cb_j_search_food != null && cb_j_search_food.isChecked() && foodId != -1)
            temp[count++] = foodId;

        if (cb_j_search_shelter != null && cb_j_search_shelter.isChecked() && shelterId != -1)
            temp[count++] = shelterId;

        if (cb_j_search_health != null && cb_j_search_health.isChecked() && healthId != -1)
            temp[count++] = healthId;

        long[] out = new long[count];
        for (int i = 0; i < count; i++) out[i] = temp[i];

        return out;
    }
}



