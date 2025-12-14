package com.example.cis183_finalproject_aminahbakr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CoverPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(CoverPage.this, MainActivity.class));
            finish(); // prevents going back to cover
        }, 1500);
    }
}
