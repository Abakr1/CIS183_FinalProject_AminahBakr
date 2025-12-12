package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageProfile extends AppCompatActivity {
    //maybe I should add org role to this page so users can selct whether theyre an org or not
    TextView tv_j_manage_username;
    TextView tv_j_manage_email;
    TextView tv_j_manage_city;
    Button btn_j_manage_logout;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);   // your xml name

        dbHelper   = new DatabaseHelper(this);

        tv_j_manage_username = findViewById(R.id.tv_v_manage_username);
        tv_j_manage_email    = findViewById(R.id.tv_v_manage_email);
        tv_j_manage_city     = findViewById(R.id.tv_v_manage_city);
        btn_j_manage_logout  = findViewById(R.id.btn_v_manage_logout);

        loadUserInfo();

        btn_j_manage_logout.setOnClickListener(v -> {
            SessionManager.clearSession(this);
            Intent i = new Intent(ManageProfile.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void loadUserInfo() {
        String username = SessionManager.getLoggedInUser(this);
        if (username == null) {
            Toast.makeText(this, "No logged in user", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COL_USER_USERNAME + "=?",
                new String[]{ username }, null, null, null);

        if (c != null && c.moveToFirst()) {
            String email   = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL));
            String city    = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_CITY));
            String orgRole = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ORG_ROLE));

            tv_j_manage_username.setText(username);
            tv_j_manage_email.setText(email != null ? email : "");
            tv_j_manage_city.setText(city != null ? city : "");


            c.close();
        }
    }
}

