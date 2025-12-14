package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText et_j_userName;
    private EditText et_j_passWord;
    private Button btn_j_login;
    private Button btn_j_register;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_j_userName  = findViewById(R.id.et_v_userName);
        et_j_passWord  = findViewById(R.id.et_v_passWord);
        btn_j_login    = findViewById(R.id.btn_v_logIn);
        btn_j_register = findViewById(R.id.btn_v_register);

        dbHelper = new DatabaseHelper(this);

        // LOGIN button
        btn_j_login.setOnClickListener(v -> attemptLogin());

        // REGISTER button
        btn_j_register.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        });
    }

    private void attemptLogin() {
        String username = et_j_userName.getText().toString().trim();
        String password = et_j_passWord.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = dbHelper.loginAndGetUserId(username, password);

        if (userId != -1) {

            SessionManager.saveLoggedInUser(this, userId, username);

            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

            // go to search
            Intent i = new Intent(MainActivity.this, Search.class);
            //pass userId
            i.putExtra("userId", userId);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}


