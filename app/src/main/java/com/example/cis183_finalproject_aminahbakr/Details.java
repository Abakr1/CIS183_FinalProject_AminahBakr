package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Details extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private long userId = -1L;
    private long resourceId = -1L;
    private boolean isFavorite = false;

    private TextView tv_j_details_orgName;

    private TextView tv_j_details_avail;
    private TextView tv_j_details_location;
    private TextView tv_j_details_contact;
    private TextView tv_j_details_city;
    private Button btn_j_details_Saved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        dbHelper = new DatabaseHelper(this);


        Intent i = getIntent();
        if (i != null) {
            userId = i.getLongExtra("userId", -1L);
            resourceId = i.getLongExtra("resourceId", -1L);
        }

        if(userId == -1L) userId = SessionManager.getUserId(this);

        if (userId == -1L || resourceId == -1L) {
            Toast.makeText(this, "Missing user/resource data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        NavBar.setUpBottomNav(this, NavBar.SCREEN_SEARCH, userId);


        // ---- views  ----
        tv_j_details_orgName = findViewById(R.id.tv_v_details_orgName);
        tv_j_details_avail = findViewById(R.id.tv_v_details_avail);
        tv_j_details_location = findViewById(R.id.tv_v_details_location);
        tv_j_details_contact= findViewById(R.id.tv_v_details_contact);
        tv_j_details_city = findViewById(R.id.tv_v_details_city);
        btn_j_details_Saved = findViewById(R.id.btn_v_detail_saved);

        loadResource();

        // ---- favorite state ----
        isFavorite = dbHelper.isFavorite(userId, resourceId);
        updateButtonText();

        btn_j_details_Saved.setOnClickListener(v -> toggleFavorite());
    }

    private void loadResource() {
        Cursor c = dbHelper.getResourceById(resourceId);

        if (c != null && c.moveToFirst()) {
            tv_j_details_orgName.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME)));
            tv_j_details_location.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS)));
            tv_j_details_city.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_CITY)));
            tv_j_details_contact.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_CONTACT)));
            tv_j_details_avail.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_DESC)));
            c.close();
        } else {
            if (c != null) c.close();
            Toast.makeText(this, "Resource not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void toggleFavorite() {
        if (isFavorite) {
            boolean removed = dbHelper.removeFavorite(userId, resourceId);
            if (removed) {
                isFavorite = false;
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Remove failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            String dateAdded = String.valueOf(System.currentTimeMillis());
            boolean added = dbHelper.addFavorite(userId, resourceId, dateAdded);
            if (added) {
                isFavorite = true;
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show();
            }
        }
        updateButtonText();
    }


    private void updateButtonText() {
        btn_j_details_Saved.setText(isFavorite ? "Remove from saved" : "Add to saved");
    }
}

