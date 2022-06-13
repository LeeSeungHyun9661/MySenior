package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_AuthorityListview;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.AuthorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 :  Activity_Authority_List
역할 : 사용자가 접근 가능한 병원에 대해 권한을 확인하고 이를 목록으로 확인
기능 :
    1) 병원에 대한 권한 접근
    2) 병원에 대한 권한 취소
    3) 병원에 대한 권한 신청서 작성을 위한 병원 검색 화면으로 연결 (Activity_Authority_Search)
특이사항:
 */

public class Activity_Authority_List extends AppCompatActivity {
    private ArrayList<Authority> authoritys; //권한 목록 배열
    private ArrayList<Hospital> hospitals; //권한과 연결된 병원 배열
    private Adapter_AuthorityListview adapter_authorityList;
    private ListView listview_authorityList;
    private Button button_athorityWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_list);

        setUI();

        //병원에 대한 권한 신청 작성을 위한 병원 검색 화면으로 연결되는 버튼
        button_athorityWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //병원 검색 화면 시작
                Intent intent = new Intent(getApplicationContext(), Activity_Authority_Search.class);
                startActivity(intent);
            }
        });

        //리스트뷰 아이템 선택 시 병원에 대한 접근을 시도
        listview_authorityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //해당 권한이 체크된 경우 병원 페이지로 이동
                if (authoritys.get(position).isCheck()) {
                    Intent intent = new Intent(getApplicationContext(), Activity_Hospital.class);
                    //글로벌 변수에 병원 정보를 저장
                    Global.getInstance().setHospital(hospitals.get(position));
                    //현재 사용자의 정보에 권한으로부터 받은 직책과 소속을 추가함
                    Global.getInstance().getUser().setHospitalAccess(authoritys.get(position).getDepartment(), authoritys.get(position).getPosition(), authoritys.get(position).isAdmin());
                    //Activity_Hospital을 시작하면서 현재 액티비티를 종료함
                    startActivity(intent);
                    finish();
                } else {//해당 권한이 체크되지 않은 경우 접근 거부
                    Toast.makeText(getApplicationContext(), "병원에서 권한을 심사중입니다...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //리스트뷰 아이템 을 길게 선택 시 병원에 대한 접근 제거
        listview_authorityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //AlertDialog를 통해 삭제를 확인
                AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Authority_List.this);
                dlg.setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제 확인 시 해당 접근 데이터 삭제
                                String a_id = authoritys.get(position).getA_id();
                                removeAuthority(a_id);
                            }
                        }).show();
                return true;
            }
        });
    }

    private void setUI() {
        listview_authorityList = (ListView) findViewById(R.id.authorityList_listview);
        button_athorityWrite = (Button) findViewById(R.id.authorityList_authority_write);
    }

    //액티비티 시작 및 다시 돌아온 경우 권한 목록 정보를 다시 받아옴
    public void onResume() {
        super.onResume();
        getAuthoritys();
    }

    //서버로부터 사용자와 연결된 권한 정보 목록을 받아오는 기능
    private void getAuthoritys() {
        //배열 초기화
        authoritys = new ArrayList<>();
        hospitals = new ArrayList<>();
        
        //어댑터 설정 및 리스트뷰와 연결
        adapter_authorityList = new Adapter_AuthorityListview(this, authoritys, hospitals);
        listview_authorityList.setAdapter(adapter_authorityList);
        
        //서버에 데이터를 요청 및 처리
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String h_id = item.getString("h_id");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int ischeck = item.getInt("ischeck");
                        int isadmin = item.getInt("isadmin");
                        String h_name = item.getString("h_name");
                        String h_category = item.getString("h_category");
                        String h_location = item.getString("h_location");
                        String h_phone = item.getString("h_phone");
                        Date h_date = new SimpleDateFormat("yyyy-MM-dd").parse(item.getString("h_date"));
                        String h_image = item.getString("h_image");

                        //응답받은 내용을 배열에 추가
                        authoritys.add(new Authority(a_id, u_id, h_id, position, department, ischeck, isadmin));
                        hospitals.add(new Hospital(h_id, h_name, h_category, h_location, h_phone, h_image, h_date));
                    }
                    //리스트를 새로 갱신
                    adapter_authorityList.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRequest authorityRequest = new AuthorityRequest(Global.getInstance().getUser().getU_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
        queue.add(authorityRequest);
    }

    //해당 권한을 확인하고 제거하는 기능
    private void removeAuthority(String a_id) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //제거에 성공한 경우
                    if (success) {
                        //권한에 대한 목록을 새롭게 갱신
                        getAuthoritys();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRemoveRequest authorityRemoveRequest = new AuthorityRemoveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
        queue.add(authorityRemoveRequest);
    }
}