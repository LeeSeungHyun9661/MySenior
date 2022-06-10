package com.example.mysenior.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mysenior.Adapter.PagerAdapter;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Fragment.Fragment_home;
import com.example.mysenior.Fragment.Fragment_monitor;
import com.example.mysenior.Fragment.Fragment_patient;
import com.example.mysenior.Fragment.Fragment_roster;
import com.example.mysenior.Fragment.Fragment_worker;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.google.android.material.tabs.TabLayout;

public class Activity_Hospital extends AppCompatActivity {

    ImageView hospital_image;
    TextView hospital_name;
    PagerAdapter adapter_viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hospital_image = (ImageView) findViewById(R.id.hospital_image);
        if (Global.getInstance().getHospital().getH_image().equals("null")) hospital_image.setImageResource(R.drawable.no_image);
        else hospital_image.setImageBitmap(Global.getInstance().getHospital().getH_imageBitmap());
        hospital_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        hospital_name = (TextView) findViewById(R.id.hospital_name);
        hospital_name.setText(Global.getInstance().getHospital().getH_name());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter_viewpager = new PagerAdapter(getSupportFragmentManager());
        adapter_viewpager.addFragment(new Fragment_home(Global.getInstance().getUser(),Global.getInstance().getHospital()),"홈");
        adapter_viewpager.addFragment(new Fragment_patient(Global.getInstance().getUser(),Global.getInstance().getHospital()),"환자");
        adapter_viewpager.addFragment(new Fragment_worker(Global.getInstance().getUser(),Global.getInstance().getHospital()),"직원");
        adapter_viewpager.addFragment(new Fragment_roster(Global.getInstance().getUser(),Global.getInstance().getHospital()),"근무");
        adapter_viewpager.addFragment(new Fragment_monitor(Global.getInstance().getUser(),Global.getInstance().getHospital()),"모니터");
        viewPager.setAdapter(adapter_viewpager);

        TabLayout tab = findViewById(R.id.tabLayout);
        tab.setupWithViewPager(viewPager);

    };

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "종료할까요?").setPositiveButton("예", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(DialogInterface dialog, int whichButton) {
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).show();
    }
}
