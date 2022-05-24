package com.example.mysenior.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.mysenior.DTO.User;
import com.example.mysenior.R;

public class Activity_Hospital extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
    };
}
