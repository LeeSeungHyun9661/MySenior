package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Authority_List;
import com.example.mysenior.Activity.Activity_Authority_Search;
import com.example.mysenior.Activity.Activity_Authority_Write;
import com.example.mysenior.Activity.Activity_Patient_Add;
import com.example.mysenior.Activity.Activity_Patient_Detail;
import com.example.mysenior.Adapter.Adapter_patient_gridview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityCheckRequest;
import com.example.mysenior.Request.PatientRequest;
import com.example.mysenior.Request.PatientSearchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment_patient extends Fragment {
    User user;
    Hospital hospital;
    EditText fragment_patient_search;
    Button fragment_patient_add;
    GridView fragment_patient_gridview;
    ArrayList<Patient> patientArrayList;
    Adapter_patient_gridview patient_gridview_adapter;

    public Fragment_patient(User user, Hospital hospital){
        this.hospital = hospital;
        this.user = user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        fragment_patient_search = (EditText) view.findViewById(R.id.fragment_patient_search);
        fragment_patient_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = fragment_patient_search.getText().toString();
                if(search.length() >= 2){
                    searchPatient(search);
                }

            }
        });

        fragment_patient_add = (Button) view.findViewById(R.id.fragment_patient_add);
        fragment_patient_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Patient_Add.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        fragment_patient_gridview = (GridView) view.findViewById(R.id.fragment_patient_gridView);
        fragment_patient_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Patient_Detail.class);
                intent.putExtra("User", user);
                intent.putExtra("Patient",  patientArrayList.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        getPatient();
    }

    private void getPatient() {
        String h_id = hospital.getH_id();
        patientArrayList = new ArrayList<>();
        patient_gridview_adapter = new Adapter_patient_gridview(getActivity(), patientArrayList);
        fragment_patient_gridview.setAdapter(patient_gridview_adapter);
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
                        patientArrayList.add(new Patient(h_id, p_id, p_name, p_gender, p_ward, p_NOK, p_NOK_phone, p_admin, p_addr,p_image, p_qr, p_age, p_birth));
                    }
                    patient_gridview_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientRequest patientRequest = new PatientRequest(h_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(patientRequest);
    }

    private void searchPatient(String search) {
        String h_id = hospital.getH_id();
        patientArrayList = new ArrayList<>();
        patient_gridview_adapter = new Adapter_patient_gridview(getActivity(), patientArrayList);
        fragment_patient_gridview.setAdapter(patient_gridview_adapter);
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
                        patientArrayList.add(new Patient(h_id, p_id, p_name, p_gender, p_ward, p_NOK, p_NOK_phone, p_admin, p_addr,p_image, p_qr, p_age, p_birth));
                    }
                    patient_gridview_adapter.notifyDataSetChanged();
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
