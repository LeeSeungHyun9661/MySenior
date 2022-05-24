package com.example.mysenior.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mysenior.Adapter.PagerAdapter;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Fragment.Fragment_home;
import com.example.mysenior.Fragment.Fragment_monitor;
import com.example.mysenior.Fragment.Fragment_patient;
import com.example.mysenior.Fragment.Fragment_roster;
import com.example.mysenior.Fragment.Fragment_worker;
import com.example.mysenior.R;
import com.google.android.material.tabs.TabLayout;

public class Activity_Hospital extends AppCompatActivity {

    User user;
    PagerAdapter adapter_viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter_viewpager = new PagerAdapter(getSupportFragmentManager());
        adapter_viewpager.addFragment(new Fragment_home(user),"홈");
        adapter_viewpager.addFragment(new Fragment_patient(user),"환자");
        adapter_viewpager.addFragment(new Fragment_worker(user),"직원");
        adapter_viewpager.addFragment(new Fragment_roster(user),"근무");
        adapter_viewpager.addFragment(new Fragment_monitor(user),"모니터");
        viewPager.setAdapter(adapter_viewpager);

        TabLayout tab = findViewById(R.id.tabLayout);
        tab.setupWithViewPager(viewPager);


    };
}
