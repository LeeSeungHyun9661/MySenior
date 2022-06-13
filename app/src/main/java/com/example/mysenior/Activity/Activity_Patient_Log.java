package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_LogListview;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.R;
import com.example.mysenior.Request.LogRemoveRequest;
import com.example.mysenior.Request.PatientLogRequest;

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

이름 : Activity_Patient_Log
역할 : 환자에 따른 각자의 기록 사항의 목록을 확인
기능 :
    1) 길게 선택해 환자 기록을 제거할 수 있음
특이사항 :
    - 환자 기록을 일자별로 구분하거나, 내용을 검색하여 쉽게 확인할 수 있도록 기능 추가 예정
 */
public class Activity_Patient_Log extends AppCompatActivity {
    private ListView listview_log;
    private Patient patient;
    private Adapter_LogListview adapter_logListview;
    private ArrayList<Patient_Log> logs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_log);

        setUI();

        //환자 정보를 가져와 적용
        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("Patient");
        
        //환자 기록 리스트뷰의 아이템을 길게 눌러 삭제를 진행
        listview_log.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Patient_Log.this);
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
                                String seq = logs.get(position).getSeq();
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                getPatientLog();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                LogRemoveRequest logRemoveRequest = new LogRemoveRequest(seq, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Log.this);
                                queue.add(logRemoveRequest);
                            }
                        }).show();
                return true;
            }
        });
        
        //환자 기록을 불러와 적용하는 기능을 수행
        getPatientLog();
    }

    private void setUI() {
        listview_log = (ListView) findViewById(R.id.Patient_log_listview);
    }

    //환자 데이터를 서버로부터 받아옴
    private void getPatientLog(){
        String p_id = patient.getP_id();
        logs = new ArrayList<>();
        adapter_logListview = new Adapter_LogListview(Activity_Patient_Log.this, logs);
        listview_log.setAdapter(adapter_logListview);
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("log");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String seq = item.getString("seq");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String pl_contents = item.getString("pl_contents");
                        String pl_time = item.getString("pl_time");
                        logs.add(new Patient_Log(seq, patient.getP_id(),patient.getP_name(), u_id,u_name, pl_contents, pl_time));
                        adapter_logListview.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientLogRequest patientLogRequest = new PatientLogRequest(p_id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Log.this);
        queue.add(patientLogRequest);
    }
}