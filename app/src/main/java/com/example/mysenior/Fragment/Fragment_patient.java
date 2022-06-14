package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Patient_Add;
import com.example.mysenior.Activity.Activity_Patient_Detail;
import com.example.mysenior.Adapter.Adapter_patient_gridview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientRequest;
import com.example.mysenior.Request.PatientSearchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
MySenior
작성일자 : 2022-06-14
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
프래그먼트 클래스

이름 :  Fragment_patient
역할 : 병원 환자들의 목록을 확인하고 접근하기 위한 프래그먼트 클래스
기능 :
    1) 환자 각각에 대한 목록 확인
    2) 새로운 환자 추가
    3) 환자 이름을 통한 검색
특이사항 :
    - 병원 공지사항의 경우 읽음 여부에 따라 계속 보여질건지를 확인하고 반영할 예정
    - 병원 모니터와 이에 따른 감지 기록은 미구현(이후 추가 구현할 예정임)
 */
public class Fragment_patient extends Fragment {
    private User user;
    private Hospital hospital;
    private EditText edittext_search;
    private Button button_add;
    private GridView gridview_patient;
    private ArrayList<Patient> patients;
    private Adapter_patient_gridview adapter_patient_gridview;
    public Fragment_patient(User user, Hospital hospital){
        this.hospital = hospital;
        this.user = user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        setUI(view);

        //환자 검색에 따른 검색 결과를 나타내기위한 검색창 기능
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = edittext_search.getText().toString();
                if(search.length() >= 2){
                    searchPatient(search);
                }

            }
        });

        //환자 추가 버튼으로 환자 추가 액티비티로 이동함
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Patient_Add.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        //환자 아이템을 선택하여 환자 자세히보기 페이지로 이동
        gridview_patient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Patient_Detail.class);
                intent.putExtra("User", user);
                intent.putExtra("Patient",  patients.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    private void setUI(View view) {
        edittext_search = (EditText) view.findViewById(R.id.fragment_patient_search);
        button_add = (Button) view.findViewById(R.id.fragment_patient_add);
        gridview_patient = (GridView) view.findViewById(R.id.fragment_patient_gridView);
    }

    public void onResume() {
        super.onResume();
        getPatient();
    }

    //병원 소속 환자들의 목록을 불러오는 기능
    private void getPatient() {
        String h_id = hospital.getH_id();
        patients = new ArrayList<>();
        adapter_patient_gridview = new Adapter_patient_gridview(getActivity(), patients);
        gridview_patient.setAdapter(adapter_patient_gridview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("patient");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String h_id = item.getString("h_id");
                        String p_id = item.getString("p_id");
                        String p_name = item.getString("p_name");
                        String p_gender = item.getString("p_gender");
                        String p_ward = item.getString("p_ward");
                        String p_NOK = item.getString("p_NOK");
                        String p_NOK_phone = item.getString("p_NOK_phone");
                        String p_admin = item.getString("p_admin");
                        String p_addr = item.getString("p_addr");
                        String p_qr = item.getString("p_qr");
                        String p_image = item.getString("p_image");
                        int p_age = Integer.parseInt(item.getString("p_age"));
                        String p_birth =  item.getString("p_birth");
                        patients.add(new Patient(h_id, p_id, p_name, p_gender, p_ward, p_NOK, p_NOK_phone, p_admin, p_addr,p_image, p_qr, p_age, p_birth));
                    }
                    adapter_patient_gridview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientRequest patientRequest = new PatientRequest(h_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(patientRequest);
    }

    //검색어에 속하는 이름의 환자들의 목록을 불러오는 기능
    private void searchPatient(String search) {
        String h_id = hospital.getH_id();
        patients = new ArrayList<>();
        adapter_patient_gridview = new Adapter_patient_gridview(getActivity(), patients);
        gridview_patient.setAdapter(adapter_patient_gridview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("patient");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String h_id = item.getString("h_id");
                        String p_id = item.getString("p_id");
                        String p_name = item.getString("p_name");
                        String p_gender = item.getString("p_gender");
                        String p_ward = item.getString("p_ward");
                        String p_NOK = item.getString("p_NOK");
                        String p_NOK_phone = item.getString("p_NOK_phone");
                        String p_admin = item.getString("p_admin");
                        String p_addr = item.getString("p_addr");
                        String p_qr = item.getString("p_qr");
                        String p_image = item.getString("p_image");
                        int p_age = Integer.parseInt(item.getString("p_age"));
                        String p_birth =  item.getString("p_birth");
                        patients.add(new Patient(h_id, p_id, p_name, p_gender, p_ward, p_NOK, p_NOK_phone, p_admin, p_addr,p_image, p_qr, p_age, p_birth));
                    }
                    adapter_patient_gridview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientSearchRequest patientSearchRequest = new PatientSearchRequest(h_id,search, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(patientSearchRequest);
    }
}
