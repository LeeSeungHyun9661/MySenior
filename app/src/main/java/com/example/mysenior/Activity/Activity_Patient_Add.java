package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientAddRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Activity_Patient_Add extends Activity {

    EditText patient_name,patient_pid,patient_age,patient_ward,patient_NOK,patient_NOK_phone,patient_admin,patient_addr;
    RadioGroup patient_radiogroup;
    RadioButton patient_radiobutton_male, patient_radiobutton_female;
    TextView patient_birthday;
    Button patient_regist,patient_calender;
    String gender;
    Calendar cal;
    User user;

    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_add);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");

        cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        patient_name = (EditText) findViewById(R.id.patient_add_name);
        patient_pid = (EditText) findViewById(R.id.patient_add_pid);
        patient_age = (EditText) findViewById(R.id.patient_add_age);
        patient_ward = (EditText) findViewById(R.id.patient_add_ward);
        patient_NOK = (EditText) findViewById(R.id.patient_add_NOK);
        patient_NOK_phone = (EditText) findViewById(R.id.patient_add_NOK_phone);
        patient_admin = (EditText) findViewById(R.id.patient_add_admin);
        patient_addr = (EditText) findViewById(R.id.patient_add_addr);

        patient_radiobutton_female = (RadioButton) findViewById(R.id.patient_add_radiobutton_female);
        patient_radiobutton_male = (RadioButton) findViewById(R.id.patient_add_radiobutton_male);
        patient_radiogroup = (RadioGroup) findViewById(R.id.patient_add_radiogroup);
        patient_radiogroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        patient_birthday = (TextView) findViewById(R.id.patient_add_birthday);

        patient_regist = (Button) findViewById(R.id.patient_add_regist);
        patient_regist.setOnClickListener(registOnClickListener);
        patient_calender = (Button) findViewById(R.id.patient_add_calender);
        patient_calender.setOnClickListener(calenderOnClickListener);
    }

    View.OnClickListener registOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String h_id = user.getH_id();
            String p_name = patient_name.getText().toString();
            String p_id = patient_pid.getText().toString();
            String p_age = patient_age.getText().toString();
            String p_gender = gender;
            String p_birth = patient_birthday.getText().toString();
            String p_ward = patient_ward.getText().toString();
            String p_NOK = patient_NOK.getText().toString();
            String p_NOK_phone = patient_NOK_phone.getText().toString();
            String p_admin = patient_admin.getText().toString();
            String p_addr = patient_addr.getText().toString();

            if (p_name.equals("") || p_id.equals("") || p_age.equals("") || p_gender.equals("") || p_birth.equals("") || p_ward.equals("") || p_NOK.equals("") || p_NOK_phone.equals("") || p_admin.equals("") || p_addr.equals("")) {
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
                                Toast.makeText(getApplicationContext(), "가입 신청 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "가입 신청 실패", Toast.LENGTH_SHORT).show();
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
    };

    View.OnClickListener calenderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DatePickerDialog(Activity_Patient_Add.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    patient_birthday.setText(Integer.toString(mYear) + "-" + Integer.toString(mMonth) + "-" + Integer.toString(mDay));
                }
            }, mYear, mMonth, mDay).show();
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.patient_add_radiobutton_male){
                gender = "M";
            }
            else if(i == R.id.patient_add_radiobutton_female){
                gender = "F";
            }
        }
    };
}