package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_log_listview;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.LogRemoveRequest;
import com.example.mysenior.Request.PatientLogRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Patient_Log extends AppCompatActivity {
    ListView Patient_log_listview;
    Patient patient;
    ListView patient_detail_log_listview;
    Adapter_log_listview patientlogadapter;
    ArrayList<Patient_Log> patient_logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_log);


        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("Patient");

        Patient_log_listview = (ListView) findViewById(R.id.Patient_log_listview);
        Patient_log_listview.setOnItemLongClickListener(loglistview_ItemLongClickListener);
        getPatientLog();
    }

    private void getPatientLog(){
        String p_id = patient.getP_id();
        patient_logs = new ArrayList<>();
        patientlogadapter = new Adapter_log_listview(Activity_Patient_Log.this,patient_logs);
        Patient_log_listview.setAdapter(patientlogadapter);
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
                        patient_logs.add(new Patient_Log(seq, p_id, u_id,u_name, pl_contents, pl_time));
                        patientlogadapter.notifyDataSetChanged();
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

    AdapterView.OnItemLongClickListener loglistview_ItemLongClickListener = new AdapterView.OnItemLongClickListener() {
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
                            String seq = patient_logs.get(position).getSeq();
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
    };

}