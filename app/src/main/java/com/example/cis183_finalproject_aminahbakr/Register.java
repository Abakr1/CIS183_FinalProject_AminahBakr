package com.example.cis183_finalproject_aminahbakr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText et_j_register_username;
    EditText et_j_register_password;
    EditText et_j_register_email;
    Spinner sp_j_register_city;
    RadioGroup rg_j_register_rgOrg;
    RadioButton rb_j_register_yes;
    RadioButton rb_j_register_no;
    Button btn_j_register_reg;

    DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        dbHelper = new DatabaseHelper(this);

        et_j_register_username = findViewById(R.id.et_v_register_username);
        et_j_register_password = findViewById(R.id.et_v_register_password);
        //have to add a textView for email on the register page
        sp_j_register_city     = findViewById(R.id.sp_v_register_city);
        rg_j_register_rgOrg    = findViewById(R.id.rg_v_register_radioGroup);
        rb_j_register_yes      = findViewById(R.id.rb_v_register_yes);
        rb_j_register_no       = findViewById(R.id.rb_v_register_no);

        btn_j_register_reg.setOnClickListener(v -> registerUser());


    }

    private void registerUser() {
        String username = et_j_register_username.getText().toString().trim();
        String password = et_j_register_password.getText().toString().trim();
        //for email as well
        String city     = sp_j_register_city.getSelectedItem() != null ? sp_j_register_city.getSelectedItem().toString() : "";

        int checkedId = rg_j_register_rgOrg.getCheckedRadioButtonId();
        String orgRole = "";
        if(checkedId == R.id.rb_v_register_yes) {
            orgRole = "Org";
        }
        else if (checkedId == R.id.rb_v_register_no)
        {
            orgRole = "Individual";

        }

        if(username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Username and Password Required", Toast.LENGTH_SHORT).show();
            return;
        }
        //have to fix this
        //long result = dbHelper.insertUser(username, password, city, orgRole);

       // if(result == -1) {
       //     Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show();
      //  }
       // else{
        //    Toast.makeText(this, "Registered Scuccefully, Please log in", Toast.LENGTH_SHORT).show();
        //    finish();
        //}
    }
}
