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
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientAddRequest;

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

이름 : Activity_Patient_Add
역할 : 환자를 데이터베이스에 추가하기 위한 액티비티
기능 :
    1) 환자 정보 작성 가능
특이사항 :
    - 환자 신원을 조회할 수 있는 추가적인 조치를 통해 확실한 신분 확인이 가능하도록 조정
 */
public class Activity_Patient_Add extends Activity {

    private EditText edittext_name, edittext_pid, edittext_age, edittext_ward, edittext_NOK, edittext_NOKPhone, edittext_admin, edittext_addr;
    private RadioGroup radiogroup;
    private RadioButton radiobutton_male, radiobutton_female;
    private TextView textview_birthday;
    private Button button_regist, button_calender;
    private String gender;
    private Calendar cal;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_add);

        setUI();

        //달력 다이어그램을 통해 날짜를 받아온다.
        cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //라디오 버튼을 통해 성별 선택
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

        //회원 등록 버튼을 통해 환자 데이터에 새롭게 환자를 추가함
        button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력한 정보를 받아옴
                String h_id = Global.getInstance().getHospital().getH_id();
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

                //받은 정보에 대해 비어있는지 확인
                if (p_name.equals("") || p_id.equals("") || p_age.equals("") || p_gender.equals("") || p_birth.equals("") || p_ward.equals("") || p_NOK.equals("") || p_NOK_phone.equals("") || p_admin.equals("") || p_addr.equals("")) {
                    Toast.makeText(getApplicationContext(), "환자 정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    //서버에 새로운 환자 데이터 입력을 수행
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    //성공시 이전 액티비티로 돌아감
                                    Toast.makeText(getApplicationContext(), "환자 추가 성공", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "환자 추가 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PatientAddRequest patientAddRequest = new PatientAddRequest(h_id, p_name,p_id, p_age, p_gender, p_birth, p_ward,p_NOK,p_NOK_phone,p_admin,p_addr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Add.this);
                    queue.add(patientAddRequest);
                }
            }
        });

        //달력을 불러와 설정한 날짜에 맞게 생일을 지정하는 기능
        button_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Activity_Patient_Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Activity_Patient_Add.this.year = year;
                        Activity_Patient_Add.this.month = month;
                        Activity_Patient_Add.this.day = day;
                        textview_birthday.setText(Integer.toString(Activity_Patient_Add.this.year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
                    }
                }, year, month, day).show();
            }
        });
    }

    private void setUI() {
        edittext_name = (EditText) findViewById(R.id.patient_add_name);
        edittext_pid = (EditText) findViewById(R.id.patient_add_pid);
        edittext_age = (EditText) findViewById(R.id.patient_add_age);
        edittext_ward = (EditText) findViewById(R.id.patient_add_ward);
        edittext_NOK = (EditText) findViewById(R.id.patient_add_NOK);
        edittext_NOKPhone = (EditText) findViewById(R.id.patient_add_NOK_phone);
        edittext_admin = (EditText) findViewById(R.id.patient_add_admin);
        edittext_addr = (EditText) findViewById(R.id.patient_add_addr);
        radiobutton_female = (RadioButton) findViewById(R.id.patient_add_radiobutton_female);
        radiobutton_male = (RadioButton) findViewById(R.id.patient_add_radiobutton_male);
        radiogroup = (RadioGroup) findViewById(R.id.patient_add_radiogroup);
        textview_birthday = (TextView) findViewById(R.id.patient_add_birthday);
        button_calender = (Button) findViewById(R.id.patient_add_calender);
        button_regist = (Button) findViewById(R.id.patient_add_regist);
    }
}