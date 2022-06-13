package com.example.mysenior.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.mysenior.Request.NotificationFixRequest;

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

이름 : Activity_Notification_Fix
역할 : 병원에 작성된 공지사항을 수정하기 위한 액티비티
기능 :
    1) 작성 내용을 불러와 기존의 내용을 수정
    2) 이미지를 선택해 기존 이미지를 변경 또는 추가
특이사항 :
    - 이미지 선택에 따른 이미지 제거로 필요없는 이미지만 제거하도록 기능을 강화할 예정임
 */
public class Activity_Notification_Fix extends FragmentActivity {
    private Notification notification;
    private Button button_fixImage;
    private TextView textview_done, textview_cancle, textview_writer;
    private EditText edittext_title, edittext_contents;
    private ViewPager2 viewpager;
    private Adapter_ImageViewpager adapter_imageViewpager;
    private CircleIndicator3 indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_fix);

        setUI();

        //수정할 공지 사항 내용을 불러움
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");

        //수정 완료 버튼 클릭시 이벤트
        textview_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //작성한 이미지와 내용을 받아옴
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


                //공지사항 데이터 내용을 변경함
                notification.setTitle(title);
                notification.setContents(contents);

                //공지 사항 수정 요청
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "공지 수정 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                //수정 성공시 변경한 내용을 되돌려보냄
                                intent.putExtra("notification",notification);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                //수정 실패시 반환
                                Toast.makeText(getApplicationContext(), "공지 수정 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                NotificationFixRequest notificationFixRequest = new NotificationFixRequest(notification.getSeq(),title,contents,image1,image2,image3,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Notification_Fix.this);
                queue.add(notificationFixRequest);
            }


        });
        
        //취소 버튼의 경우 이전 액티비티로 돌아감
        textview_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 선택 버튼을 누를 경우 이미지 갤러리로 이동
        button_fixImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CODES.REQUEST_CODE);
            }
        });

        //수정을 위한 정보를 불러옴
        textview_writer.setText(Global.getInstance().getUser().getU_name());
        edittext_title.setText(notification.getTitle());
        edittext_contents.setText(notification.getContents());

        //이미지 목록을 불러와 어탭터에 정의
        adapter_imageViewpager = new Adapter_ImageViewpager(this, notification.getImageBitmap(this));
        viewpager.setAdapter(adapter_imageViewpager);

        //뷰페이저에 선택 버튼을 적용함
        indicator.setViewPager(viewpager);
        indicator.createIndicators(notification.getImageCount(), 0);
        
        //뷰페이저를 적용함
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
                indicator.animatePageSelected(position % notification.getImageMax());
                viewpager = findViewById(R.id.Notification_fix_viewpager);
                Toast.makeText(getApplicationContext(), "선택", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUI() {
        viewpager = (ViewPager2) findViewById(R.id.Notification_fix_viewpager);
        textview_done = (TextView) findViewById(R.id.Notification_fix_done);
        indicator = findViewById(R.id.Notification_fix_indicator);
        edittext_contents = (EditText) findViewById(R.id.Notification_fix_contents);
        textview_writer = (TextView) findViewById(R.id.Notification_fix_writer);
        edittext_title = (EditText) findViewById(R.id.Notification_fix_title);
        button_fixImage = (Button) findViewById(R.id.Notification_fix_add_image);
        textview_cancle = (TextView) findViewById(R.id.Notification_fix_cancle);
    }

    //이미지를 선택한 이후에 뷰페이지의 변형을 줘 선택한 이미지를 볼 수 있도록 지정함
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODES.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapController bitmapController = new BitmapController(getContentResolver().openInputStream(data.getData()),getResources().getConfiguration());

                    notification.addImage(bitmapController.toString());
                    viewpager.setOffscreenPageLimit(notification.getImageCount()); //최대 이미지 수

                    //이미지 비트맵 목록을 수정하고 어탭터에 새롭게 적용함
                    adapter_imageViewpager = new Adapter_ImageViewpager(this, notification.getImageBitmap(this));

                    viewpager.setOffscreenPageLimit(notification.getImageCount()); //최대 이미지 수
                    viewpager.setAdapter(adapter_imageViewpager);

                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}