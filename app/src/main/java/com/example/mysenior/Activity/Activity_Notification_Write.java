package com.example.mysenior.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_ImageViewpager;
import com.example.mysenior.BitmapController;
import com.example.mysenior.CODES;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationAddRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Notification_Write
역할 : 공지사항을 작성하기 위한 액티비티
기능 :
    1) 갤러리에 접근해 이미지를 선택하여 게시글에 이미지를 추가할 수 있음
    2) 작성한 내용에 대해 공지사항을 게시함
특이사항 :
    - 작성시 카테고리를 부여하여 게시글에 카테고리를 달거나 새로운 카테고리를 설정할 수 있도록 기능 추가 예정
    - 이미지 선택에 따른 이미지 제거로 필요없는 이미지만 제거하도록 기능을 강화할 예정임
 */
public class Activity_Notification_Write extends FragmentActivity {
    private Notification notification;
    private Button button_addImage;
    private TextView textview_done, textview_calcle, textview_writer;
    private EditText edittext_title, edittext_contents;
    private ViewPager2 viewpager;
    private Adapter_ImageViewpager adapter_imageViewpager;
    private CircleIndicator3 indicator3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_write);
        notification = new Notification();

        setUI();

        //작성 완료에 대한 기능 정의
        textview_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_id = Global.getInstance().getUser().getU_id();
                String h_id = Global.getInstance().getHospital().getH_id();
                String title = edittext_title.getText().toString();
                if (title.equals("")) title = "제목 없음";
                String contents = edittext_contents.getText().toString();
                if (contents.equals("")) contents = "내용 없음";
                String image1 = "";
                String image2 = "";
                String image3 = "";
                if (!notification.getImages().get(0).equals(""))
                    image1 = notification.getImages().get(0);
                if (!notification.getImages().get(1).equals(""))
                    image2 = notification.getImages().get(1);
                if (!notification.getImages().get(2).equals(""))
                    image3 = notification.getImages().get(2);

                //받은 내용에 대해 업로드를 진행함
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w("RESPONSE", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { 
                                Toast.makeText(getApplicationContext(), "공지 작성 성공", Toast.LENGTH_SHORT).show();
                                // 업로드 진행 완료시 액티비티 종료
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "공지 작성 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                NotificationAddRequest notificationAddRequest = new NotificationAddRequest(h_id,u_id,title,contents,image1,image2,image3,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Notification_Write.this);
                queue.add(notificationAddRequest);
            }
        });

        //작성 취소시 액티비티 종료
        textview_calcle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 추가를 위한 버튼 역할 정의
        button_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CODES.REQUEST_CODE);
            }
        });

        textview_writer.setText(Global.getInstance().getUser().getU_name());

        //이미지 뷰 어탭터 정의
        adapter_imageViewpager = new Adapter_ImageViewpager(this, notification.getImageBitmap(this));
        viewpager.setAdapter(adapter_imageViewpager);

        indicator3.setViewPager(viewpager);
        indicator3.createIndicators(notification.getImageCount(), 0);
        viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewpager.setCurrentItem(1000); //시작 지점
        viewpager.setOffscreenPageLimit(notification.getImageCount()); //최대 이미지 수
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    viewpager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator3.animatePageSelected(position % notification.getImageMax());
            }
        });
    }

    private void setUI() {
        textview_done = (TextView) findViewById(R.id.Notification_write_done);
        indicator3 = findViewById(R.id.Notification_write_indicator);
        viewpager = findViewById(R.id.Notification_write_viewpager);
        edittext_title = (EditText) findViewById(R.id.Notification_write_title);
        edittext_contents = (EditText) findViewById(R.id.Notification_write_contents);
        textview_writer = (TextView) findViewById(R.id.Notification_write_writer);
        button_addImage = (Button) findViewById(R.id.Notification_write_add_image);
        textview_calcle = (TextView) findViewById(R.id.Notification_write_cancle);
    }
    
    //추가한 이미지를 보여주도록 갤러리에서 받은 이미지를 보여줌
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODES.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapController bitmapController = new BitmapController(getContentResolver().openInputStream(data.getData()),getResources().getConfiguration());
                    notification.addImage(bitmapController.toString());

                    viewpager.setOffscreenPageLimit(notification.getImageCount()); //최대 이미지 수

                    adapter_imageViewpager = new Adapter_ImageViewpager(this, notification.getImageBitmap(this));
                    viewpager.setAdapter(adapter_imageViewpager);
                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}