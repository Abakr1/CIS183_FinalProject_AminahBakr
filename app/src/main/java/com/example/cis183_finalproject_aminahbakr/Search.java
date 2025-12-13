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

    private CheckBox cbFood, cbShelter, cbHealth;
    private RadioButton rbMonroe, rbYpsi;
    private Button btnSearch;

    private LinearLayout resultsContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        dbHelper = new DatabaseHelper(this);

        // get userId from intent (pass this when navigating to Search)
        userId = getIntent().getLongExtra("user_id", -1L);

        cbFood = findViewById(R.id.cb_v_search_food);
        cbShelter = findViewById(R.id.cb_v_search_shelter);
        cbHealth = findViewById(R.id.cb_v_search_health);

        rbMonroe = findViewById(R.id.rb_v_search_monroe);
        rbYpsi = findViewById(R.id.rb_v_search_ypsi);

        btnSearch = findViewById(R.id.btn_v_search_search);

        // this must exist in your xml (LinearLayout for results)
        resultsContainer = findViewById(R.id.ll_v_search_results);

        btnSearch.setOnClickListener(v -> runSearch());
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
                long resourceId = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ID));
                String org = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME));
                String address = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS));

                TextView tv = new TextView(this);
                tv.setText(org + " — " + address);
                tv.setTextSize(16);
                tv.setPadding(16, 16, 16, 16);

                tv.setTag(resourceId);

                // click OR long click opens Details
                tv.setOnClickListener(v -> openDetails((long) v.getTag()));
                tv.setOnLongClickListener(v -> {
                    openDetails((long) v.getTag());
                    return true;
                });

                resultsContainer.addView(tv);

            } while (c.moveToNext());
            c.close();
        } else {
            if (c != null) c.close();
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDetails(long resourceId) {
        if (userId == -1L) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Details.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("resource_id", resourceId);
        startActivity(intent);
    }

    private String getSelectedCity() {
        if (rbMonroe != null && rbMonroe.isChecked()) return "Monroe";
        if (rbYpsi != null && rbYpsi.isChecked()) return "Ypsilanti";
        return null;
    }

    private long[] getSelectedCategoryIds() {
        // categories are Food / Shelter / Health in your DB defaults
        long foodId = dbHelper.getCategoryIdByName("Food");
        long shelterId = dbHelper.getCategoryIdByName("Shelter");
        long healthId = dbHelper.getCategoryIdByName("Health");

        // build dynamically
        long[] temp = new long[3];
        int count = 0;

        if (cbFood != null && cbFood.isChecked() && foodId != -1) temp[count++] = foodId;
        if (cbShelter != null && cbShelter.isChecked() && shelterId != -1) temp[count++] = shelterId;
        if (cbHealth != null && cbHealth.isChecked() && healthId != -1) temp[count++] = healthId;

        long[] out = new long[count];
        for (int i = 0; i < count; i++) out[i] = temp[i];
        return out;
    }
}


