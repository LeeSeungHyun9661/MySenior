package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;

public class Activity_Patient_Detail extends AppCompatActivity {

    ImageView patient_detail_image;
    TextView patient_detail_fix,patient_detail_delete, patient_detail_name, patient_detail_id, patient_detail_age, patient_detail_gender,
            patient_detail_birthday, patient_detail_ward, patient_detail_NOK, patient_detail_NOK_phone, patient_detail_addr,patient_detail_log_more;

    User user;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        patient = (Patient) intent.getSerializableExtra("Patient");

        patient_detail_image = (ImageView) findViewById(R.id.patient_detail_image);

        patient_detail_fix = (TextView) findViewById(R.id.patient_detail_fix);
        patient_detail_fix.setOnClickListener(patientFixOnClickListener);

        patient_detail_delete = (TextView) findViewById(R.id.patient_detail_delete);
        patient_detail_delete.setOnClickListener(patientdeleteOnClickListener);

        patient_detail_log_more = (TextView) findViewById(R.id.patient_detail_log_more);
        patient_detail_log_more.setOnClickListener(patientLogOnClickListener);

        patient_detail_name = (TextView) findViewById(R.id.patient_detail_name);
        patient_detail_name.setText(patient.getP_name());

        patient_detail_id = (TextView) findViewById(R.id.patient_detail_id);
        patient_detail_id.setText(patient.getP_id());

        patient_detail_age = (TextView) findViewById(R.id.patient_detail_age);
        patient_detail_age.setText(patient.getP_age());

        patient_detail_gender = (TextView) findViewById(R.id.patient_detail_gender);
        patient_detail_gender.setText(patient.getP_gender());

        patient_detail_birthday = (TextView) findViewById(R.id.patient_detail_birthday);
        patient_detail_birthday.setText(patient.getP_birth().toString());

        patient_detail_addr = (TextView) findViewById(R.id.patient_detail_addr);
        patient_detail_addr.setText(patient.getP_addr());

        patient_detail_ward = (TextView) findViewById(R.id.patient_detail_ward);
        patient_detail_ward.setText(patient.getP_ward());

        patient_detail_NOK = (TextView) findViewById(R.id.patient_detail_NOK);
        patient_detail_NOK.setText(patient.getP_NOK());

        patient_detail_NOK_phone = (TextView) findViewById(R.id.patient_detail_NOK_phone);
        patient_detail_NOK_phone.setText(patient.getP_NOK_phone());
    }

    View.OnClickListener patientFixOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener patientdeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener patientLogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}