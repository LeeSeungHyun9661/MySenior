package com.example.mysenior.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_notification_image_viewpager;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationDeleteRequest;
import com.example.mysenior.Request.PatientDeleteRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class Activity_Notification_Detail extends FragmentActivity {
    Notification notification;
    TextView Notification_detail_fix,Notification_detail_delete, Notification_detail_title,Notification_detail_writer,Notification_detail_time,Notification_detail_contents;
    ViewPager2 Notification_detail_viewpager;
    Adapter_notification_image_viewpager adapter_notification_imageViewpager;
    private CircleIndicator3 Notification_detail_indicator;
    ArrayList<Bitmap> imageBitmaps;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    notification = (Notification) intent.getSerializableExtra("notification");
                    imageBitmaps = notification.getImageBitmap(Activity_Notification_Detail.this);
                    adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(Activity_Notification_Detail.this, imageBitmaps);
                    Notification_detail_viewpager.setAdapter(adapter_notification_imageViewpager);
                    Notification_detail_title.setText(notification.getTitle());
                    Notification_detail_contents.setText(notification.getContents());
                } else {

                }
            }
        });

        Notification_detail_fix = (TextView) findViewById(R.id.Notification_detail_fix);
        Notification_detail_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Global.getInstance().getUser().getIsAdmin() == 1) {
                    Intent intent = new Intent(Activity_Notification_Detail.this, Activity_Notification_Fix.class);
                    intent.putExtra("notification", notification);
                    launcher.launch(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Notification_detail_delete = (TextView) findViewById(R.id.Notification_detail_delete);
        Notification_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Global.getInstance().getUser().getIsAdmin() == 1) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Notification_Detail.this);
                    dlg.setTitle("삭제")
                            .setMessage("환자 정보를 삭제하시겠습니까?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String seq = notification.getSeq();
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    NotificationDeleteRequest notificationDeleteRequest = new NotificationDeleteRequest(seq, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Activity_Notification_Detail.this);
                                    queue.add(notificationDeleteRequest);
                                }
                            }).show();
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
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
        imageBitmaps = notification.getImageBitmap(this);
        adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(this,imageBitmaps);
        Notification_detail_viewpager.setAdapter(adapter_notification_imageViewpager);

        Notification_detail_indicator = findViewById(R.id.Notification_detail_indicator);
        Notification_detail_indicator.setViewPager(Notification_detail_viewpager);
        Notification_detail_indicator.createIndicators(notification.getImageMax(),0);
        Notification_detail_viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        Notification_detail_viewpager.setCurrentItem(1000); //시작 지점
        Notification_detail_viewpager.setOffscreenPageLimit(notification.getImageMax()); //최대 이미지 수
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