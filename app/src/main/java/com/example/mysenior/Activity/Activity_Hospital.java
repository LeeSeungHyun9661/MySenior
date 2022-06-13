package com.example.mysenior.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.PagerAdapter;
import com.example.mysenior.BitmapController;
import com.example.mysenior.CODES;
import com.example.mysenior.Fragment.Fragment_home;
import com.example.mysenior.Fragment.Fragment_monitor;
import com.example.mysenior.Fragment.Fragment_patient;
import com.example.mysenior.Fragment.Fragment_roster;
import com.example.mysenior.Fragment.Fragment_worker;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.HospitalImageRequest;
import com.example.mysenior.Request.PatientImageRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 :  Activity_Hospital
역할 : 병원에 대한 전체 이미지 및 설정, 알람에 대해 접근하는 '홈 페이지' 역할을 수행
기능 :
    1) 각 페이지에 대한 프래그먼트를 연결
    2) 사용자가 관리자일 경우 병원 이미지에 대한 교체 가능
특이사항 :
    - 알림 기능 추가 구현 필요
    - 병원 이미지를 추가적인 리사이클러 뷰로 구현하여 여러장을 볼 수 있도록 추가
    - 병원 구성에 대한 추가적인 제한 사항 등에 대한 설정을 추가할 예정
 */
public class Activity_Hospital extends AppCompatActivity {

    private ImageView imageview_hospitalImage, imageview_imageChange;
    private TextView textview_hospitalName;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        setUI();

        //현재 병원의 이미지를 설정
        if (Global.getInstance().getHospital().getH_image().equals(""))
            imageview_hospitalImage.setImageResource(R.drawable.no_image);
        else
            imageview_hospitalImage.setImageBitmap(Global.getInstance().getHospital().getH_imageBitmap());

        //이미지 변경 버튼 클릭
        if (Global.getInstance().getUser().getIsAdmin()) {
            imageview_imageChange.setVisibility(View.VISIBLE);
            imageview_imageChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //갤러리에 접근하고 선택한 이미지를 액티비티에 반환
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, CODES.REQUEST_CODE);
                }
            });
        } else {
            imageview_imageChange.setVisibility(View.INVISIBLE);
        }


        //병원 제목 설정
        textview_hospitalName.setText(Global.getInstance().getHospital().getH_name());

        //각 프래그먼트를 정의하고 값을 전달함
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new Fragment_home(Global.getInstance().getUser(), Global.getInstance().getHospital()), "홈");
        pagerAdapter.addFragment(new Fragment_patient(Global.getInstance().getUser(), Global.getInstance().getHospital()), "환자");
        pagerAdapter.addFragment(new Fragment_worker(Global.getInstance().getUser(), Global.getInstance().getHospital()), "직원");
        pagerAdapter.addFragment(new Fragment_roster(Global.getInstance().getUser(), Global.getInstance().getHospital()), "근무");
        pagerAdapter.addFragment(new Fragment_monitor(Global.getInstance().getUser(), Global.getInstance().getHospital()), "모니터");
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
    }

    private void setUI() {
        imageview_hospitalImage = (ImageView) findViewById(R.id.hospital_image);
        imageview_imageChange = (ImageView) findViewById(R.id.hospital_image_change);
        textview_hospitalName = (TextView) findViewById(R.id.hospital_name);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tab = findViewById(R.id.tabLayout);
    }

    //뒤로가기를 눌렀을 경우 확인 다이얼로그 출력
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

    //갤러리에서 전달 받은 이미지로 서버에 갱신함
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODES.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapController bitmapController = new BitmapController(getContentResolver().openInputStream(data.getData()), getResources().getConfiguration());
                    imageview_hospitalImage.setImageBitmap(bitmapController.resize());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE",response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                                    //병원 이미지를 수정하고 현재 병원의 이미지값을 변경함
                                    imageview_hospitalImage.setImageBitmap(bitmapController.resize());
                                    Global.getInstance().getHospital().setH_image(bitmapController.toString());
                                } else {
                                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    HospitalImageRequest hospitalImageRequest = new HospitalImageRequest(Global.getInstance().getHospital().getH_id(), bitmapController.toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Hospital.this);
                    queue.add(hospitalImageRequest);
                } catch (Exception e) {
                }
                //사진 선택 취소의 경우
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}
