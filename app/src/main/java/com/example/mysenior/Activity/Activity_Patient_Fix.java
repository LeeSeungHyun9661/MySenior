package com.example.mysenior.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientFixRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Patient_Fix
역할 : 환자의 상세 정보를 수정할 수 있는 페이지
기능 :
    1) 사용자 권한에 따라 사용자가 수정을 요청함
특이사항 :
 */
public class Activity_Patient_Fix extends Activity {

    private EditText edittext_name, edittext_pid, edittext_age, edittext_ward, edittext_NOK, edittext_NOKPhone, edittext_admin, edittext_addr;
    private RadioGroup radiogroup;
    private RadioButton radiobutton_male, radiobutton_female;
    private TextView textview_birthday;
    private Button button_regist, button_calender;
    private String gender;
    private Calendar cal;
    private Patient patient;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_fix);

        setUI();

        //환자 정보를 받아와 저장함
        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("Patient");

        //날짜 불러오기를 위한 달력 데이터 적용
        cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //환자 정보를 받아와 적용
        edittext_name.setText(patient.getP_name());
        edittext_pid.setText(patient.getP_id());
        edittext_age.setText(Integer.toString(patient.getP_age()));
        edittext_ward.setText(patient.getP_ward());
        edittext_NOK.setText(patient.getP_NOK());
        edittext_NOKPhone.setText(patient.getP_NOK_phone());
        edittext_admin.setText(patient.getP_admin());
        edittext_addr.setText(patient.getP_addr());
        textview_birthday.setText(patient.getP_birth());

        //환자 성별에 따라 표시를 나타냄
        gender = patient.getP_gender();
        if(gender.equals("F")) radiobutton_female.setChecked(true);
        else radiobutton_male.setChecked(true);

        //라디오 버튼을 통해 환자 성별 설정
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.patient_add_radiobutton_male){
                    gender = "M";
                }
                else if(i == R.id.patient_add_radiobutton_female){
                    gender = "F";
                }
            }
        });

        //변경된 환자 정보를 서버에서 수정하고 이전 액티비티로 보내는 기능을 수행
        button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = patient.getP_id();
                String p_name = edittext_name.getText().toString();
                String p_id = edittext_pid.getText().toString();
                String p_age = edittext_age.getText().toString();
                String p_gender = gender;
                String p_birth = textview_birthday.getText().toString();
                String p_ward = edittext_ward.getText().toString();
                String p_NOK = edittext_NOK.getText().toString();
                String p_NOK_phone = edittext_NOKPhone.getText().toString();
                String p_admin = edittext_admin.getText().toString();
                String p_addr = edittext_addr.getText().toString();

                if (p_name.equals("")
                        || p_id.equals("")
                        || p_age.equals("")
                        || p_gender.equals("")
                        || p_birth.equals("")
                        || p_ward.equals("")
                        || p_NOK.equals("")
                        || p_NOK_phone.equals("")
                        || p_admin.equals("")
                        || p_addr.equals("")
                ) {
                    Toast.makeText(getApplicationContext(), "환자 정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                                    Patient  patient_fix = new Patient(patient.getH_id(),p_id,p_name,p_gender,p_ward,p_NOK,p_NOK_phone,p_admin,p_addr,patient.getP_image(),patient.getP_qr(),Integer.parseInt(p_age),p_birth);
                                    Intent intent = new Intent();
                                    intent.putExtra("Patient",patient_fix);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PatientFixRequest patientFixRequest = new PatientFixRequest(id, p_name,p_id, p_age, p_gender, p_birth, p_ward,p_NOK,p_NOK_phone,p_admin,p_addr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Fix.this);
                    queue.add(patientFixRequest);
                }
            }
        });

        //달력을 불러와 설정한 날짜에 맞게 생일을 지정하는 기능
        button_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Activity_Patient_Fix.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Activity_Patient_Fix.this.year = year;
                        month = monthOfYear;
                        day = dayOfMonth;
                        textview_birthday.setText(Integer.toString(Activity_Patient_Fix.this.year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
                    }
                }, year, month, day).show();
            }
        });
    }

    private void setUI() {
        edittext_name = (EditText) findViewById(R.id.patient_fix_name);
        edittext_pid = (EditText) findViewById(R.id.patient_fix_pid);
        edittext_age = (EditText) findViewById(R.id.patient_fix_age);
        edittext_ward = (EditText) findViewById(R.id.patient_fix_ward);
        edittext_NOK = (EditText) findViewById(R.id.patient_fix_NOK);
        edittext_NOKPhone = (EditText) findViewById(R.id.patient_fix_NOK_phone);
        edittext_admin = (EditText) findViewById(R.id.patient_fix_admin);
        edittext_addr = (EditText) findViewById(R.id.patient_fix_addr);
        textview_birthday = (TextView) findViewById(R.id.patient_fix_birthday);
        button_regist = (Button) findViewById(R.id.patient_fix_regist);
        button_calender = (Button) findViewById(R.id.patient_fix_calender);
        radiobutton_female = (RadioButton) findViewById(R.id.patient_fix_radiobutton_female);
        radiobutton_male = (RadioButton) findViewById(R.id.patient_fix_radiobutton_male);
        radiogroup = (RadioGroup) findViewById(R.id.patient_fix_radiogroup);
    }
}