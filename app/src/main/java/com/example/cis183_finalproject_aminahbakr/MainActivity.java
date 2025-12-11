package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    EditText et_j_userName;
    EditText et_j_passWord;
    Button btn_j_login;
    Button btn_j_register;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        et_j_userName  = findViewById(R.id.et_v_userName);
        et_j_passWord  = findViewById(R.id.et_v_passWord);
        btn_j_login    = findViewById(R.id.btn_v_logIn);
        btn_j_register = findViewById(R.id.btn_v_register);

        dbHelper = new DatabaseHelper();

        //for the login button
        btn_j_login.setOnClickListener(v -> attemptLogin());

        //for the register button (takes users to register)
        btn_j_register.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        });

    }

    private void attemptLogin()
    {
        String username = et_j_userName.getText().toString().trim();
        String password = et_j_passWord.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }

        //boolean ok = dbHelper.checkUserLogin(username, password);

        //if(ok) {
          //  Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();

           // Intent i = new Intent(MainActivity.this, Search.class);
          //  startActivity(i);
           // finish();
       // }
       // else{
       //     Toast.makeText(this,"Invalid username or password", Toast.LENGTH_SHORT).show();
        //}
    }
}
