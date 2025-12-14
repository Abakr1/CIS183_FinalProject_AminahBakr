package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddResources extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";

    private EditText et_j_add_orgName;
    private EditText et_j_add_address;
    private EditText et_j_add_contact;
    private EditText et_j_add_desc;
    private Spinner sp_J_add_category;
    private Button btn_j_add_submit;

    private DatabaseHelper dbHelper;
    private long selectedCategoryId = -1L;

    private long userId = -1L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_resource);

        dbHelper = new DatabaseHelper(this);

        // grab userId from Intent
        userId = getIntent().getLongExtra("userId", -1L);
        if(userId == -1L) userId = SessionManager.getUserId(this);

        if (userId == -1L) {
            Toast.makeText(this, "Please log in again (missing user).", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        et_j_add_orgName = findViewById(R.id.et_v_add_orgName);
        et_j_add_address = findViewById(R.id.et_v_add_address);
        et_j_add_contact = findViewById(R.id.et_v_add_contact);
        et_j_add_desc = findViewById(R.id.et_v_add_desc);
        sp_J_add_category = findViewById(R.id.sp_v_add_category);
        btn_j_add_submit = findViewById(R.id.btn_v_add_submit);

        loadCategoriesIntoSpinner();

        btn_j_add_submit.setOnClickListener(v -> saveResource());

        NavBar.setUpBottomNav(this, NavBar.SCREEN_ADDRESOURCE, userId);
    }

    private void saveResource() {
        String orgName = et_j_add_orgName.getText().toString().trim();
        String address = et_j_add_address.getText().toString().trim();
        String contact = et_j_add_contact.getText().toString().trim();
        String desc = et_j_add_desc.getText().toString().trim();

        if (orgName.isEmpty()) {
            Toast.makeText(this, "Org name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategoryId == -1L) {
            Toast.makeText(this, "Please choose a category", Toast.LENGTH_SHORT).show();
            return;
        }

        String city = extractCityFromAddress(address);
        String dateAdded = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        long result = dbHelper.insertResource(
                userId,
                selectedCategoryId,
                orgName,
                address,
                city,
                contact,
                desc,
                dateAdded
        );

        if (result == -1) {
            Toast.makeText(this, "That resource already exists.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Resource added!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String extractCityFromAddress(String address) {
        String a = address.toLowerCase();
        if (a.contains("ypsilanti") || a.contains("ypsi")) return "Ypsilanti";
        if (a.contains("monroe")) return "Monroe";
        return "Monroe";
    }

    private void loadCategoriesIntoSpinner() {
        Cursor c = dbHelper.getAllCategories();
        List<String> names = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        if (c != null && c.moveToFirst()) {
            do {
                ids.add(c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
                names.add(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME)));
            } while (c.moveToNext());
            c.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, names
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_J_add_category.setAdapter(adapter);

        sp_J_add_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = (position >= 0 && position < ids.size()) ? ids.get(position) : -1L;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1L;
            }
        });
    }
}




