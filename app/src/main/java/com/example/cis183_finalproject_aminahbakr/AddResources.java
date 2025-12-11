package com.example.cis183_finalproject_aminahbakr;

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

import java.util.ArrayList;
import java.util.List;

public class AddResources extends AppCompatActivity {
    EditText et_j_add_orgName;
    EditText et_j_add_address;
    EditText et_j_add_city;
    EditText et_j_add_contact;
    EditText et_j_add_desc;
    Spinner sp_J_add_category;
    Button btn_j_add_submit;

    DatabaseHelper dbHelper;
    long selectedCategoryId = -1L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_resource);

        et_j_add_orgName = findViewById(R.id.et_v_add_orgName);
        et_j_add_address = findViewById(R.id.et_v_add_address);
        et_j_add_contact = findViewById(R.id.et_v_add_contact);
        //add it for the city
        et_j_add_desc = findViewById(R.id.et_v_add_desc);
        sp_J_add_category = findViewById(R.id.sp_v_add_category);
        btn_j_add_submit = findViewById(R.id.btn_v_add_submit);

        dbHelper = new DatabaseHelper(this);

        loadCategoriesIntoSpinner();

        btn_j_add_submit.setOnClickListener(v -> saveResource());


    }

    private void saveResource() {
        String orgName = et_j_add_orgName.getText().toString().trim();
        String address = et_j_add_address.getText().toString().trim();
        // String city    = et_j_add_city.getText().toString().trim();
        String contact = et_j_add_contact.getText().toString().trim();
        String desc = et_j_add_desc.getText().toString().trim();

        if (orgName.isEmpty()) {
            Toast.makeText(this, "Org name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategoryId == -1L) {
            Toast.makeText(this, "Please choose a category", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void loadCategoriesIntoSpinner() {
        Cursor c = dbHelper.getALLCategories();
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
                selectedCategoryId = ids.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1L;
            }
        });
    }

    //get the user
    //need a session manager to see which exact user is logged in
    //String username = SessionManager.getLoggedInUser(this);
//    long userId = 1;
//
//
//        if (username != null) {
//        // simple lookup to get userId from username
//        userId = getUserIdByUsername(username);
//    }
//
//    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            .format(new Date());
//
//    long result = dbHelper.insertResource(userId, selectedCategoryId,
//            orgName, address, city, contact, desc, date);

//        if (result == -1) {
//        Toast.makeText(this, "Error saving resource", Toast.LENGTH_SHORT).show();
//    } else {
//        Toast.makeText(this, "Resource added", Toast.LENGTH_SHORT).show();
//        finish();
//    }
//}
//
//private long getUserIdByUsername(String username) {
//    SQLiteDatabase db = dbHelper.getReadableDatabase();
//    Cursor c = db.query(DatabaseHelper.TABLE_USERS,
//            new String[]{ DatabaseHelper.COL_USER_ID },
//            DatabaseHelper.COL_USER_USERNAME + "=?",
//            new String[]{ username }, null, null, null);
//    if (c != null && c.moveToFirst()) {
//        long id = c.getLong(0);
//        c.close();
//        return id;
//    }
//    if (c != null) c.close();
//    return 1; // fallback
//}


}



