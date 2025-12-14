package com.example.cis183_finalproject_aminahbakr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText et_j_register_username;
    private EditText et_j_register_password;
    private EditText et_j_register_email;
    private Spinner sp_j_register_city;
    private RadioGroup rg_j_register_rgOrg;
    private RadioButton rb_j_register_yes;
    private RadioButton rb_j_register_no;
    private Button btn_j_register_reg;

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        dbHelper = new DatabaseHelper(this);

        et_j_register_username = findViewById(R.id.et_v_register_username);
        et_j_register_password = findViewById(R.id.et_v_register_password);
        //have to add a textView for email on the register page
        et_j_register_email    = findViewById(R.id.et_v_register_email);
        sp_j_register_city     = findViewById(R.id.sp_v_register_city);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Monroe", "Ypsilanti"}
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_j_register_city.setAdapter(cityAdapter);




        rg_j_register_rgOrg    = findViewById(R.id.rg_v_register_radioGroup);
        rb_j_register_yes      = findViewById(R.id.rb_v_register_yes);
        rb_j_register_no       = findViewById(R.id.rb_v_register_no);
        //why did i forget this
        btn_j_register_reg     = findViewById(R.id.btn_v_register_reg);

        //fixes the button
        if(btn_j_register_reg == null) {
            Toast.makeText(this, "REGISTER BUTTON ID NOT FOUND", Toast.LENGTH_SHORT).show();
            return;
        }

        btn_j_register_reg.setOnClickListener(v -> registerUser());


    }

    private void registerUser() {
        String username = et_j_register_username.getText().toString().trim();
        String password = et_j_register_password.getText().toString().trim();
        //for email as well
        String email    = et_j_register_email.getText().toString().trim();
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

        if(username.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Username,Password, and Email Required", Toast.LENGTH_SHORT).show();
            return;
        }
        //have to fix this
        long result = dbHelper.insertUser(username,password, " ",email, city, orgRole);

        if(result == -1) {
           Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show();
        }
        else{
           Toast.makeText(this, "Registered Scuccefully, Please log in", Toast.LENGTH_SHORT).show();
           finish();
        }
    }
}
