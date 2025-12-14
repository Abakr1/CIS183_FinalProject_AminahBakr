package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageProfile extends AppCompatActivity {

    private EditText et_j_manage_username;
    private EditText et_j_manage_email;
    private EditText et_j_manage_city;

    private Button btn_j_manage_update;
    private Button btn_j_manage_logout;

    private DatabaseHelper dbHelper;
    private long userId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);

        dbHelper = new DatabaseHelper(this);


        et_j_manage_username = findViewById(R.id.et_v_manage_username);
        et_j_manage_email    = findViewById(R.id.et_v_manage_email);
        et_j_manage_city     = findViewById(R.id.et_v_manage_city);

        btn_j_manage_update = findViewById(R.id.btn_v_manage_update);
        btn_j_manage_logout = findViewById(R.id.btn_v_manage_logout);

        // get logged-in user
        userId = SessionManager.getUserId(this);

        if (userId == -1L) {
            Toast.makeText(this, "No logged in user", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return;
        }

        loadUserInfo();

        btn_j_manage_update.setOnClickListener(v -> updateProfile());

        btn_j_manage_logout.setOnClickListener(v -> {
            SessionManager.clearSession(this);
            Intent i = new Intent(ManageProfile.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });


         NavBar.setUpBottomNav(this, NavBar.SCREEN_PROFILE, userId);
    }

    private void loadUserInfo() {
        Cursor c = dbHelper.getUserById(userId);

        if (c != null && c.moveToFirst()) {
            String username = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_USERNAME));
            String email    = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL));
            String city     = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_CITY));

            et_j_manage_username.setText(username != null ? username : "");
            et_j_manage_email.setText(email != null ? email : "");
            et_j_manage_city.setText(city != null ? city : "");

            c.close();
        } else {
            if (c != null) c.close();
            Toast.makeText(this, "Could not load profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        String username = et_j_manage_username.getText().toString().trim();
        String email    = et_j_manage_email.getText().toString().trim();
        String city     = et_j_manage_city.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (city.isEmpty()) {
            Toast.makeText(this, "City cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean ok = dbHelper.updateUserProfile(userId, username, email, city);

        if (ok) {
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
            loadUserInfo();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}


