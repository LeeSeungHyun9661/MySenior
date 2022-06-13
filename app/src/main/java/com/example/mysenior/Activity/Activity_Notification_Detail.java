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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_ImageViewpager;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationDeleteRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Notification_Detail
역할 : 병원에서 작성된 공지에 대해 내부 정보와 이미지를 확인할 수 있는 액티비티
기능 :
    1) 공지사항 내용과 작성자, 시간 확인가능
    2) 사용자 권한에 따라 공지사항의 수정 및 제거 시작 가능
    3) 공지사항의 이미지 내용 확인 가능
특이사항 :
    - 공지 사항 이미지 선택시 이미지를 자세히 보거나 이미지 저장이 가능하도록 기능 추가 필요
 */
public class Activity_Notification_Detail extends FragmentActivity {
    Notification notification;
    TextView textview_NotificationFix, textview_NotificationDelete, textview_NotificationTitle, textview_NotificationWriter, textview_NotificationTime, textview_NotificationContents;
    ViewPager2 notificationViewpager;
    Adapter_ImageViewpager adapter_ImageViewpager;
    private CircleIndicator3 indicator;
    ArrayList<Bitmap> bitmaps;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        setUI();

        //액티비티간에 전달받은 공지사항 데이터를 저장
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");

        //공지사항 수정이 완료된 경우 값을 받아와 페이지를 수정함
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    notification = (Notification) intent.getSerializableExtra("notification");
                    bitmaps = notification.getImageBitmap(Activity_Notification_Detail.this);
                    adapter_ImageViewpager = new Adapter_ImageViewpager(Activity_Notification_Detail.this, bitmaps);
                    notificationViewpager.setAdapter(adapter_ImageViewpager);
                    textview_NotificationTitle.setText(notification.getTitle());
                    textview_NotificationContents.setText(notification.getContents());
                } else {

                }
            }
        });

        //공지사항에 대한 수정 버튼 클릭의 경우 launcher를 통해 인텐트를 실행
        textview_NotificationFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Global.getInstance().getUser().getIsAdmin()) {
                    Intent intent = new Intent(Activity_Notification_Detail.this, Activity_Notification_Fix.class);
                    intent.putExtra("notification", notification);
                    launcher.launch(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //공지사항 삭제 버튼을 통해 해당 공지 게시물을 삭제함
        textview_NotificationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Global.getInstance().getUser().getIsAdmin()) {
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

        //게시물 정보 갱신
        textview_NotificationTitle.setText(notification.getTitle());
        textview_NotificationWriter.setText(notification.getU_name());
        textview_NotificationTime.setText(notification.getDate());
        textview_NotificationContents.setText(notification.getContents());

        //게시물 이미지 목록을 적용
        adapter_ImageViewpager = new Adapter_ImageViewpager(this, notification.getImageBitmap(this));
        notificationViewpager.setAdapter(adapter_ImageViewpager);

        //게시물 이미지의 순서 버튼을 적용함
        indicator.setViewPager(notificationViewpager);
        indicator.createIndicators(notification.getImageCount(),0);

        //뷰 페이저에 이미지를 지정하고
        notificationViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        notificationViewpager.setCurrentItem(1000); //시작 지점
        notificationViewpager.setOffscreenPageLimit(notification.getImageCount()); //최대 이미지 수
        //뷰페이저의 이미지 변경에 대한 지정
        notificationViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    notificationViewpager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position%notification.getImageMax());
            }
        });
    }
    private void setUI() {
        textview_NotificationFix = (TextView) findViewById(R.id.Notification_detail_fix);
        textview_NotificationContents = (TextView) findViewById(R.id.Notification_detail_contents);
        textview_NotificationTime = (TextView) findViewById(R.id.Notification_detail_time);
        textview_NotificationWriter = (TextView) findViewById(R.id.Notification_detail_writer);
        textview_NotificationTitle = (TextView) findViewById(R.id.Notification_detail_title);
        notificationViewpager = (ViewPager2) findViewById(R.id.Notification_detail_viewpager);
        textview_NotificationDelete = (TextView) findViewById(R.id.Notification_detail_delete);
        indicator = findViewById(R.id.Notification_detail_indicator);
    }
}