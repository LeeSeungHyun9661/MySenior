package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_worker_authority_listview;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityApproveRequest;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.WorkerAuthorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Worker_Add
역할 : 해당 병원에 권한을 요청한 사람을 처리하는 액티비티
기능 :
    1) 권한 요청자 중 처리되지 않은 사람의 목록 불러오기 가능
    2) 권한 체크를 통해 새롭게 직원으로 변경 가능
    3) 길게 눌러 권한을 거부할 수 있음
특이사항 :
    - 이미 권한을 거부한 사람에 대한 목록을 통해 권한 거부를 상세하게 처리할 예정
    - 병원 권한이 다양하게 지정될 경우 권한 등급을 선택해줄 수 있도록 조치할 예정
 */
public class Activity_Worker_Add extends AppCompatActivity {

    private ListView listview_worker;
    private ArrayList<Authority> authorities;
    private ArrayList<String> names;
    private Adapter_worker_authority_listview adapter_worker_authority_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_add);

        setUI();

        //아직 병원 직원이 아니지만 권한을 신청한 인원에 대해 권한을 승인하고 병원 직원으로 추가함
        listview_worker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Worker_Add.this);
                dlg.setTitle("권한 부여")
                        .setMessage("해당 인원을 병원 직원으로 승인합니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String a_id = authorities.get(i).getA_id();
                                approveAuthority(a_id);
                            }
                        })
                        .setPositiveButton("거절", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String a_id = authorities.get(i).getA_id();
                                removeAuthority(a_id);
                            }
                        }).show();
            }
        });
    }

    private void setUI() {
        listview_worker = (ListView) findViewById(R.id.worker_authority_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuthorityList();
    }

    //권한 신청 목록을 받아와 리스트뷰에 적용
    private void getAuthorityList() {
        String h_id = Global.getInstance().getHospital().getH_id();
        authorities = new ArrayList<>();
        names = new ArrayList<>();
        adapter_worker_authority_listview = new Adapter_worker_authority_listview(this, authorities, names);
        listview_worker.setAdapter(adapter_worker_authority_listview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
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
                        Authority authority = new Authority(a_id, u_id, h_id, position, department, ischeck, isadmin);
                        authorities.add(authority);
                        names.add(item.getString("u_name"));
                    }
                    adapter_worker_authority_listview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        WorkerAuthorityRequest authorityRequest = new WorkerAuthorityRequest(h_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityRequest);
    }

    //권한 삭제를 통해 리스트에서 제거함
    private void removeAuthority(String a_id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        getAuthorityList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRemoveRequest authorityRemoveRequest = new AuthorityRemoveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityRemoveRequest);
    }

    //권한 승인을 통해 병원 직원으로 업로드함
    private void approveAuthority(String a_id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        getAuthorityList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityApproveRequest authorityApproveRequest = new AuthorityApproveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityApproveRequest);
    }
}