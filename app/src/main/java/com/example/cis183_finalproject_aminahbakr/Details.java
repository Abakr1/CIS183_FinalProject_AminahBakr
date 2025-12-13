package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity {

    static String EXTRA_RESOURCE_ID = "resource_id";
    static String EXTRA_ORG_NAME = "org_name";
    static String EXTRA_LOCATION = "location";
    static String EXTRA_CITY = "city";
    static String EXTRA_CONTACT = "contact";
    static String EXTRA_AVAILABILLITY = "availability";

    TextView tv_j_details_orgName;
    TextView tv_j_details_avail;
    TextView tv_j_details_location;
    TextView tv_j_details_contact;
    TextView tv_j_details_city;

    //dont forget the button dumb dumb
    Button btn_j_details_saved;

    DatabaseHelper dbHelper;
    long resourceId = -1L;
    long userId = -1L;
    boolean isFavorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        dbHelper = new DatabaseHelper(this);

        tv_j_details_orgName = findViewById(R.id.tv_v_details_orgName);
        tv_j_details_avail = findViewById(R.id.tv_v_details_avail);
        tv_j_details_location = findViewById(R.id.tv_v_details_location);
        tv_j_details_contact = findViewById(R.id.tv_v_details_contact);
        tv_j_details_city = findViewById(R.id.tv_v_details_city);
        btn_j_details_saved = findViewById(R.id.btn_v_detail_saved);

        Intent i = getIntent();
        resourceId = i.getLongExtra(EXTRA_RESOURCE_ID, -1L);

        String orgName = i.getStringExtra(EXTRA_ORG_NAME);
        String location = i.getStringExtra(EXTRA_LOCATION);
        String availability = i.getStringExtra(EXTRA_AVAILABILLITY);
        String contact = i.getStringExtra(EXTRA_CONTACT);
        String city = i.getStringExtra(EXTRA_CITY);

        tv_j_details_orgName.setText(orgName != null ? orgName : "");
        tv_j_details_location.setText(location != null ? location : "");
        tv_j_details_city.setText(city != null ? city : "");
        tv_j_details_contact.setText(contact != null ? contact : "");
        tv_j_details_avail.setText(availability != null ? availability : "");

        //to figure out the user id from the logged in user
        String username = SessionManager.getLoggedInUser(this);
        if (username != null) {
            userId = getUserIdByUsername(username);
        }

        updateFavoriteButtonText();

        btn_j_details_saved.setOnClickListener(v -> toggleFavorite());


    }

    private void updateFavoriteButtonText() {
        if (isFavorite) {
            btn_j_details_saved.setText("Remove from Saved");
        } else {
            btn_j_details_saved.setText("Add to Saved");
        }
    }

    private long getUserIdByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COL_USER_ID},
                DatabaseHelper.COL_USER_USERNAME + "=?",
                new String[]{username}, null, null, null);
        if (c != null && c.moveToFirst()) {
            long id = c.getLong(0);
            c.close();
            return id;
        }
        if (c != null) c.close();
        return -1L;
    }

    private void toggleFavorite() {
        if (userId == -1L || resourceId == -1L) {
            Toast.makeText(this, "Unable to toggle favorite", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isFavorite) {
            int rows = dbHelper.removeFavorite(userId, resourceId);
            if (rows > 0) {
                isFavorite = false;
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = dbHelper.addFavorite(userId, resourceId);
            if (result != -1) {
                isFavorite = true;
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        }
        updateFavoriteButtonText();
    }

}
