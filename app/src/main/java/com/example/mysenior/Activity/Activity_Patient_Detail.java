package com.example.mysenior.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_LogListview;
import com.example.mysenior.BitmapController;
import com.example.mysenior.CODES;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientDeleteRequest;
import com.example.mysenior.Request.PatientImageRequest;
import com.example.mysenior.Request.PatientLogRequest;
import com.example.mysenior.Request.PatientaddLogRequest;

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

이름 : Activity_Patient_Detail
역할 : 환자의 상세 정보를 확인할 수 있는 페이지
기능 :
    1) 사용자 권한에 따라 사용자가 수정 및 제거가 가능함
    2) 사용자에게 기록을 남기긱 가능
특이사항 :
    - 환자 제거의 경우 연결된 로그와 병원 서버에 연결된 환자 이미지 또한 삭제할 수 있도록 기능 추가 예정
    - 환자 추가에 따른 객체 인식을 위한 데이터를 병원에 설치된 프로그램으로 보낼 수 있도록 조정할 예정
 */
public class Activity_Patient_Detail extends AppCompatActivity {

    private ImageView imageview_imagepatient, imageview_imageChange;
    private TextView textview_fix, textview_delete, textview_name, textview_pid, textview_age, textview_gender,
            textview_birthday, textview_ward, textview_NOK, textview_NOKPhone, textview_addr, textview_logs;
    private EditText edittext_addLog;
    private Button button_addLog;
    private ListView listview_logs;
    private Adapter_LogListview adapter_loglistview;
    private ArrayList<Patient_Log> logs;
    private Patient patient;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        
        setUI();

        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("Patient");

        //환자 이미지가 없을 경우 기본 이미지를, 아니면 환자 이미지를 표시한다.
        if (patient.getP_image().equals("")) imageview_imagepatient.setImageResource(R.drawable.default_user_128);
        else imageview_imagepatient.setImageBitmap(patient.getP_imageBitmap());

        //환자 정보 수정 시에 반환된 수정값을 받아 액티비티를 수정하는 기능
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    patient = (Patient) intent.getSerializableExtra("Patient");

                    if (patient.getP_image() == null) {
                        imageview_imagepatient.setImageResource(R.drawable.default_user_128);
                    } else {
                        imageview_imagepatient.setImageBitmap(patient.getP_imageBitmap());
                    }
                    textview_NOKPhone.setText(patient.getP_NOK_phone());
                    textview_NOK.setText(patient.getP_NOK());
                    textview_ward.setText(patient.getP_ward());
                    textview_addr.setText(patient.getP_addr());
                    textview_birthday.setText(patient.getP_birth());
                    textview_gender.setText(patient.getP_gender());
                    textview_age.setText(Integer.toString(patient.getP_age()));
                    textview_name.setText(patient.getP_name());
                    textview_pid.setText(patient.getP_id());
                } else if (false) {

                }
            }
        });

        //이미지 변경 버튼을 누른 경우 갤러리에 접근
        if(Global.getInstance().getUser().getIsAdmin()){
            imageview_imageChange.setVisibility(View.VISIBLE);
            imageview_imageChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, CODES.REQUEST_CODE);
                }
            });
        }else{
            imageview_imageChange.setVisibility(View.INVISIBLE);
        }

        //수정 버튼을 눌렀을 경우 수정 페이지로 이동
        textview_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Global.getInstance().getUser().getIsAdmin()) {
                    Intent intent = new Intent(Activity_Patient_Detail.this, Activity_Patient_Fix.class);
                    intent.putExtra("Patient", patient);
                    launcher.launch(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //삭제 버튼을 눌렀을 경우 환자 삭제를 진행
        textview_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.getInstance().getUser().getIsAdmin()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Patient_Detail.this);
                    dlg.setTitle("삭제")
                            .setMessage("환자 정보를 삭제하시겠습니까?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String p_id = patient.getP_id();
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    PatientDeleteRequest patientDeleteRequest = new PatientDeleteRequest(p_id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
                                    queue.add(patientDeleteRequest);
                                }
                            }).show();
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        //환자에 대한 기록 목록으로 이동
        textview_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Patient_Detail.this, Activity_Patient_Log.class);
                intent.putExtra("Patient", patient);
                startActivity(intent);
            }
        });

        //환자 기록을 새롭게 작성하기 위한 기능 수행
        button_addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_id = Global.getInstance().getUser().getU_id();
                String p_id = patient.getP_id();
                String pl_contents = edittext_addLog.getText().toString();
                if (pl_contents.equals("")) {
                    return;
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    edittext_addLog.setText("");
                                    getPatientLog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PatientaddLogRequest patientaddLogRequest = new PatientaddLogRequest(p_id, u_id, pl_contents, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
                    queue.add(patientaddLogRequest);
                }

            }
        });
        
        //환자 정보를 채워넣음
        textview_NOKPhone.setText(patient.getP_NOK_phone());
        textview_NOK.setText(patient.getP_NOK());
        textview_ward.setText(patient.getP_ward());
        textview_addr.setText(patient.getP_addr());
        textview_birthday.setText(patient.getP_birth());
        textview_gender.setText(patient.getP_gender());
        textview_age.setText(Integer.toString(patient.getP_age()));
        textview_name.setText(patient.getP_name());
        textview_pid.setText(patient.getP_id());

        //환자 기록을 불러와 리스트에 적용하는 기능을 수행함
        getPatientLog();
    }

    private void setUI() {
        textview_fix = (TextView) findViewById(R.id.patient_detail_fix);
        imageview_imagepatient = (ImageView) findViewById(R.id.patient_detail_image);
        imageview_imageChange = (ImageView) findViewById(R.id.patient_detail_image_change);
        textview_delete = (TextView) findViewById(R.id.patient_detail_delete);
        textview_logs = (TextView) findViewById(R.id.patient_detail_log_more);
        edittext_addLog = (EditText) findViewById(R.id.patient_detial_add_log);
        button_addLog = (Button) findViewById(R.id.patient_detial_add_log_button);
        textview_name = (TextView) findViewById(R.id.patient_detail_name);
        textview_pid = (TextView) findViewById(R.id.patient_detail_id);
        textview_age = (TextView) findViewById(R.id.patient_detail_age);
        textview_gender = (TextView) findViewById(R.id.patient_detail_gender);
        textview_birthday = (TextView) findViewById(R.id.patient_detail_birthday);
        textview_addr = (TextView) findViewById(R.id.patient_detail_addr);
        textview_ward = (TextView) findViewById(R.id.patient_detail_ward);
        textview_NOK = (TextView) findViewById(R.id.patient_detail_NOK);
        textview_NOKPhone = (TextView) findViewById(R.id.patient_detail_NOK_phone);
        listview_logs = (ListView) findViewById(R.id.patient_detail_log_listview);
    }

    //환자 기록 불러오기 수행
    private void getPatientLog() {
        logs = new ArrayList<>();
        adapter_loglistview = new Adapter_LogListview(Activity_Patient_Detail.this, logs);
        listview_logs.setAdapter(adapter_loglistview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("log");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String seq = item.getString("seq");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String pl_contents = item.getString("pl_contents");
                        String pl_time = item.getString("pl_time");
                        if (logs.size() < 4) {
                            logs.add(new Patient_Log(seq,patient.getP_id(), patient.getP_name(), u_id, u_name, pl_contents, pl_time));
                            adapter_loglistview.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientLogRequest patientLogRequest = new PatientLogRequest(patient.getP_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
        queue.add(patientLogRequest);

    }

    protected void onResume() {
        super.onResume();
        getPatientLog();
    }

    //갤러리에서 받아온 이미지를 적용하고 새롭게 데이터베이스에 수정
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm;
        String bms;
        if (requestCode == CODES.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapController bitmapController = new BitmapController(getContentResolver().openInputStream(data.getData()),getResources().getConfiguration());
                    imageview_imagepatient.setImageBitmap(bitmapController.resize());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                                    imageview_imagepatient.setImageBitmap(bitmapController.resize());
                                } else {
                                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PatientImageRequest patientImageRequest = new PatientImageRequest(patient.getP_id(), bitmapController.toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
                    queue.add(patientImageRequest);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
    
}