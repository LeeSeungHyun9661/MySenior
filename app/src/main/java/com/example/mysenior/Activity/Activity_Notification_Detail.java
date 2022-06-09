package com.example.mysenior.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mysenior.Adapter.Adapter_notification_image_viewpager;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.R;

import me.relex.circleindicator.CircleIndicator3;

public class Activity_Notification_Detail extends FragmentActivity {
    Notification notification;
    TextView Notification_detail_fix,Notification_detail_delete, Notification_detail_title,Notification_detail_writer,Notification_detail_time,Notification_detail_contents;
    ViewPager2 Notification_detail_viewpager;
    Adapter_notification_image_viewpager adapter_notification_imageViewpager;
    private CircleIndicator3 Notification_detail_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");

        Notification_detail_fix = (TextView) findViewById(R.id.Notification_detail_fix);
        Notification_detail_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Notification_detail_delete = (TextView) findViewById(R.id.Notification_detail_delete);
        Notification_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Notification_detail_title = (TextView) findViewById(R.id.Notification_detail_title);
        Notification_detail_title.setText(notification.getTitle());
        Notification_detail_writer = (TextView) findViewById(R.id.Notification_detail_writer);
        Notification_detail_writer.setText(notification.getU_name());
        Notification_detail_time = (TextView) findViewById(R.id.Notification_detail_time);
        Notification_detail_time.setText(notification.getDate());
        Notification_detail_contents = (TextView) findViewById(R.id.Notification_detail_contents);
        Notification_detail_contents.setText(notification.getContents());

        Notification_detail_viewpager = findViewById(R.id.Notification_detail_viewpager);
        adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(this, notification.getImageBitmap(this));
        Notification_detail_viewpager.setAdapter(adapter_notification_imageViewpager);

        Notification_detail_indicator = findViewById(R.id.Notification_detail_indicator);
        Notification_detail_indicator.setViewPager(Notification_detail_viewpager);
        Notification_detail_indicator.createIndicators(notification.getImageMax(),0);
        Notification_detail_viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        Notification_detail_viewpager.setCurrentItem(1000); //시작 지점
        Notification_detail_viewpager.setOffscreenPageLimit(3); //최대 이미지 수
        Notification_detail_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    Notification_detail_viewpager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Notification_detail_indicator.animatePageSelected(position%notification.getImageMax());
            }
        });
    }
}