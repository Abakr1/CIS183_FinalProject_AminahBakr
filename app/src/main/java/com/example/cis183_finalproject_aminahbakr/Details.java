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

    private TextView tvOrgName, tvAvail, tvLocation, tvContact, tvCity;
    private Button btnSaved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        dbHelper = new DatabaseHelper(this);

        // ---- extras ----
        Intent i = getIntent();
        if (i != null) {
            userId = i.getLongExtra("user_id", -1L);
            resourceId = i.getLongExtra("resource_id", -1L);
        }

        if (userId == -1L || resourceId == -1L) {
            Toast.makeText(this, "Missing user/resource data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ---- views (MATCHES YOUR IDs) ----
        tvOrgName = findViewById(R.id.tv_v_details_orgName);
        tvAvail = findViewById(R.id.tv_v_details_avail);
        tvLocation = findViewById(R.id.tv_v_details_location);
        tvContact = findViewById(R.id.tv_v_details_contact);
        tvCity = findViewById(R.id.tv_v_details_city);
        btnSaved = findViewById(R.id.btn_v_detail_saved);

        loadResource();

        // ---- favorite state ----
        isFavorite = dbHelper.isFavorite(userId, resourceId);
        updateButtonText();

        btnSaved.setOnClickListener(v -> toggleFavorite());
    }

    private void loadResource() {
        Cursor c = dbHelper.getResourceById(resourceId);

        if (c != null && c.moveToFirst()) {
            tvOrgName.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ORG_NAME)));
            tvLocation.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_ADDRESS)));
            tvCity.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_CITY)));
            tvContact.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_CONTACT)));
            tvAvail.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_RESOURCE_DESC)));

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
                Toast.makeText(this, "Removed from saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could not remove", Toast.LENGTH_SHORT).show();
            }
        } else {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            boolean added = dbHelper.addFavorite(userId, resourceId, date);
            if (added) {
                isFavorite = true;
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already saved (or error)", Toast.LENGTH_SHORT).show();
            }
        }

        updateButtonText();
    }

    private void updateButtonText() {
        btnSaved.setText(isFavorite ? "Remove from saved" : "Add to saved");
    }
}

