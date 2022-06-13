package com.example.mysenior.Activity;

import androidx.annotation.IdRes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityAddRequest;

import org.json.JSONException;
import org.json.JSONObject;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 :  Activity_Authority_Write
역할 : 접근 권한 신청서를 작성
기능 :
    1) 검색 결과로 나온 병원에 대해 접근 신청서를 작성함
특이사항 :
 */
public class Activity_Authority_Write extends Activity {
    private EditText edittext_position, edittext_department;
    private ImageView imageview_hospitalimage;
    private TextView textview_username, textview_hospitalDate, textview_hospitalName, textview_hospitalCategory, textview_hospitalLocation, textview_hospitalPhone;
    private Button button_authorityWrite;
    private RadioGroup radiogroup_admin;
    private RadioButton radiobutton_worker, radiobutton_admin;
    private boolean isAdmin;
    private Hospital hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_write);

        //선택한 병원에 대한 데이터를 이전 액티비티에서 받아온다.
        Intent intent = getIntent();
        hospital = (Hospital) intent.getSerializableExtra("Hospital");

        setUI();

        //병원 이미지 설정
        if (hospital.getH_image().equals(""))
            imageview_hospitalimage.setImageResource(R.drawable.no_image);
        else imageview_hospitalimage.setImageBitmap(hospital.getH_imageBitmap());

        //병원 데이터를 받아 미리보기로 지정
        textview_username.setText(Global.getInstance().getUser().getU_name());
        textview_hospitalDate.setText(hospital.getH_date());
        textview_hospitalName.setText(hospital.getH_name());
        textview_hospitalCategory.setText(hospital.getH_category());
        textview_hospitalLocation.setText(hospital.getH_location());
        textview_hospitalPhone.setText(hospital.getH_phone());

        //회원 등급을 일반 회원으로 초기화
        isAdmin = false;
        radiobutton_worker.setChecked(true);

        //라디오 버튼의 선택에 대한 리스너를 지정
        radiogroup_admin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.authorityWrite_radiogroup_worker) {
                    isAdmin = false;
                } else if (i == R.id.authorityWrite_radiogroup_admin) {
                    isAdmin = true;
                }
            }
        });

        //권한 신청 작성 완료 버튼의 역할을 정의
        button_authorityWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_id = Global.getInstance().getUser().getU_id();
                String h_id = hospital.getH_id();
                String department = edittext_department.getText().toString();
                String position = edittext_position.getText().toString();
                String admin;
                if (isAdmin) {
                    admin = "1";
                } else {
                    admin = "0";
                }

                //사용자가 모든 정보를 작성했는지 확인
                if (u_id.isEmpty() || h_id.isEmpty() || department.isEmpty() || position.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "권한 신청 항목을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //모든 정보가 작성되었을 경우 권한 신청을 서버에 추가
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                //추가 성공시 병원 검색과 현재 액티비티를 종료함
                                if (success) {
                                    Activity_Authority_Search activity_authority_search = (Activity_Authority_Search) Activity_Authority_Search.activity_authority_search;
                                    activity_authority_search.finish();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "가입 신청 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AuthorityAddRequest authorityAddRequest = new AuthorityAddRequest(u_id, h_id, department, position, admin, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Write.this);
                    queue.add(authorityAddRequest);
                }

            }
        });
    }

    private void setUI() {
        edittext_position = (EditText) findViewById(R.id.authorityWrite_position);
        edittext_department = (EditText) findViewById(R.id.authorityWrite_department);
        imageview_hospitalimage = (ImageView) findViewById(R.id.authorityWrite_hospitalimage);
        textview_username = (TextView) findViewById(R.id.authorityWrite_username);
        textview_hospitalDate = (TextView) findViewById(R.id.authorityWrite_hospitaldate);
        textview_hospitalName = (TextView) findViewById(R.id.authorityWrite_hospitalname);
        textview_hospitalCategory = (TextView) findViewById(R.id.authorityWrite_hospitalcategory);
        textview_hospitalLocation = (TextView) findViewById(R.id.authorityWrite_hospitallocation);
        textview_hospitalPhone = (TextView) findViewById(R.id.authorityWrite_hospitalphone);
        button_authorityWrite = (Button) findViewById(R.id.authorityWrite_button);
        radiobutton_worker = (RadioButton) findViewById(R.id.authorityWrite_radiogroup_worker);
        radiobutton_admin = (RadioButton) findViewById(R.id.authorityWrite_radiogroup_admin);
        radiogroup_admin = (RadioGroup) findViewById(R.id.authorityWrite_radiogroup);
    }
}