package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_HospitalSearchListview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityCheckRequest;
import com.example.mysenior.Request.HospitalSearchRequest;

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

이름 :  Activity_Authority_Search
역할 : 사용자가 새로운 접근 권한을 신청할 병원을 검색
기능 :
    1) 병원 목록을 검색하고 해당 병원에 대해 권한을 신청함
    1) 병원에 이미 권한을 보유한 경우 거부
특이사항 :
    - 병원의 경우 공공데이터에서 "전국병원데이터"를 불러와 갱신함
 */
public class Activity_Authority_Search extends AppCompatActivity {
    public static Activity activity_authority_search; //권한 신청이 성공할 경우 같이 종료하기 위한 신호를 받기위해 해당 액티비티를 선언함
    private EditText edittext_hospitalsearch;
    private ListView listview_authoritySearch;
    private ArrayList<Hospital> hospitals;
    private Adapter_HospitalSearchListview adapter_hospitalSearchListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_search);
        //권한 신청이 성공할 경우 같이 종료하기 위한 신호를 받기위해 해당 액티비티를 선언함
        activity_authority_search = Activity_Authority_Search.this;

        //인터페이스 정의
        setUI();

        //검색창에 입력된 검색어에 따라 실시간으로 병원 결과를 반환함
        edittext_hospitalsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = edittext_hospitalsearch.getText().toString();
                if(search.length() >= 2){
                    searchHospital(search);
                }

            }
        });

        //검색된 병원 선택시에 해당 병원에 대해 권한 신청 작성 페이지로 이동
        listview_authoritySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Response.Listener<String> responseListener  = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //이미 해당 병원에 권한을 보유하고 있는지 확인
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                //권한 보유시 접근 거부
                                Toast.makeText(getApplicationContext(), "해당 병원에 대해 권한을 보유하고 있습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                //권한 신청 단계로 이동
                                Intent intent = new Intent(getApplicationContext(), Activity_Authority_Write.class);
                                intent.putExtra("Hospital", hospitals.get(i));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AuthorityCheckRequest authorityCheckRequest = new AuthorityCheckRequest(Global.getInstance().getUser().getU_id(), hospitals.get(i).getH_id(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Search.this);
                queue.add(authorityCheckRequest);
            }
        });

    }

    private void setUI() {
        edittext_hospitalsearch = (EditText) findViewById(R.id.authoritySearch_hospitalsearch);
        listview_authoritySearch = (ListView) findViewById(R.id.authoritySearch_Listview);
    }

    //검색어에 따른 병원 목록을 서버로부터 받아오는 기능을 수행함
    private void searchHospital(String search) {
        hospitals = new ArrayList<>();
        adapter_hospitalSearchListview = new Adapter_HospitalSearchListview(this, hospitals, Global.getInstance().getUser());
        listview_authoritySearch.setAdapter(adapter_hospitalSearchListview);
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("hospital");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String h_id = item.getString("h_id");
                        String h_name = item.getString("h_name");
                        String h_category = item.getString("h_category");
                        String h_location = item.getString("h_location");
                        String h_phone = item.getString("h_phone");
                        Date h_date =  new SimpleDateFormat("yyyy-MM-dd").parse(item.getString("h_date"));
                        String h_image = item.getString("h_image");
                        hospitals.add(new Hospital(h_id,h_name,h_category,h_location,h_phone,h_image,h_date));
                    }
                    adapter_hospitalSearchListview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        HospitalSearchRequest hospitalSearchRequest = new HospitalSearchRequest(search,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Search.this);
        queue.add(hospitalSearchRequest);
    }

}