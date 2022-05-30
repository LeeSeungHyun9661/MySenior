package com.example.mysenior.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.PatientDeleteRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
        patient_detail_id = (TextView) findViewById(R.id.patient_detail_id);
        patient_detail_age = (TextView) findViewById(R.id.patient_detail_age);
        patient_detail_gender = (TextView) findViewById(R.id.patient_detail_gender);
        patient_detail_birthday = (TextView) findViewById(R.id.patient_detail_birthday);
        patient_detail_addr = (TextView) findViewById(R.id.patient_detail_addr);
        patient_detail_ward = (TextView) findViewById(R.id.patient_detail_ward);
        patient_detail_NOK = (TextView) findViewById(R.id.patient_detail_NOK);
        patient_detail_NOK_phone = (TextView) findViewById(R.id.patient_detail_NOK_phone);

        patient_detail_NOK_phone.setText(patient.getP_NOK_phone());
        patient_detail_NOK.setText(patient.getP_NOK());
        patient_detail_ward.setText(patient.getP_ward());
        patient_detail_addr.setText(patient.getP_addr());
        patient_detail_birthday.setText(patient.getP_birth());
        patient_detail_gender.setText(patient.getP_gender());
        patient_detail_age.setText(Integer.toString(patient.getP_age()));
        patient_detail_name.setText(patient.getP_name());
        patient_detail_id.setText(patient.getP_id());
    }

    View.OnClickListener patientFixOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Activity_Patient_Detail.this, Activity_Patient_Fix.class);
            intent.putExtra("Patient", patient);
            launcher.launch(intent);
        }
    };

    View.OnClickListener patientdeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (resultCode == 100) {

        }

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        patient = (Patient)intent.getSerializableExtra("Patient");
                        Log.w("RESULTOFACTIVITY!",patient.toString());
                        patient_detail_NOK_phone.setText(patient.getP_NOK_phone());
                        patient_detail_NOK.setText(patient.getP_NOK());
                        patient_detail_ward.setText(patient.getP_ward());
                        patient_detail_addr.setText(patient.getP_addr());
                        patient_detail_birthday.setText(patient.getP_birth());
                        patient_detail_gender.setText(patient.getP_gender());
                        patient_detail_age.setText(Integer.toString(patient.getP_age()));
                        patient_detail_name.setText(patient.getP_name());
                        patient_detail_id.setText(patient.getP_id());
                    }
                }
            });

    View.OnClickListener patientLogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}