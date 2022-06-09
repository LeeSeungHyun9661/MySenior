package com.example.mysenior.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import com.bumptech.glide.Glide;
import com.example.mysenior.Adapter.Adapter_log_listview;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.HospitalSearchRequest;
import com.example.mysenior.Request.PatientAddRequest;
import com.example.mysenior.Request.PatientDeleteRequest;
import com.example.mysenior.Request.PatientFixRequest;
import com.example.mysenior.Request.PatientImageRequest;
import com.example.mysenior.Request.PatientLogRequest;
import com.example.mysenior.Request.PatientSearchRequest;
import com.example.mysenior.Request.PatientaddLogRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_Patient_Detail extends AppCompatActivity {

    private static final int REQUEST_CODE = 99;
    ImageView patient_detail_image;
    TextView patient_detail_fix, patient_detail_delete, patient_detail_name, patient_detail_id, patient_detail_age, patient_detail_gender,
            patient_detail_birthday, patient_detail_ward, patient_detail_NOK, patient_detail_NOK_phone, patient_detail_addr, patient_detail_log_more;
    EditText patient_detial_add_log;
    Button patient_detial_add_log_button;
    ListView patient_detail_log_listview;
    Adapter_log_listview patientlogadapter;
    ArrayList<Patient_Log> patient_logs;
    Patient patient;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("Patient");
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    patient = (Patient) intent.getSerializableExtra("Patient");

                    if (patient.getP_image() == null) {
                        patient_detail_image.setImageResource(R.drawable.patient);
                    } else {
                        patient_detail_image.setImageBitmap(patient.getP_imageBitmap());
                    }
                    patient_detail_NOK_phone.setText(patient.getP_NOK_phone());
                    patient_detail_NOK.setText(patient.getP_NOK());
                    patient_detail_ward.setText(patient.getP_ward());
                    patient_detail_addr.setText(patient.getP_addr());
                    patient_detail_birthday.setText(patient.getP_birth());
                    patient_detail_gender.setText(patient.getP_gender());
                    patient_detail_age.setText(Integer.toString(patient.getP_age()));
                    patient_detail_name.setText(patient.getP_name());
                    patient_detail_id.setText(patient.getP_id());
                } else if (false) {

                }
            }
        });

        patient_detail_image = (ImageView) findViewById(R.id.patient_detail_image);
        patient_detail_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        patient_detail_image.setImageResource(R.drawable.patient);

        if (patient.getP_image().equals("null")) patient_detail_image.setImageResource(R.drawable.patient);
        else patient_detail_image.setImageBitmap(patient.getP_imageBitmap());

        patient_detail_fix = (TextView) findViewById(R.id.patient_detail_fix);
        patient_detail_fix.setOnClickListener(patientFixOnClickListener);
        patient_detail_delete = (TextView) findViewById(R.id.patient_detail_delete);
        patient_detail_delete.setOnClickListener(patientdeleteOnClickListener);
        patient_detail_log_more = (TextView) findViewById(R.id.patient_detail_log_more);
        patient_detail_log_more.setOnClickListener(patientLogOnClickListener);
        patient_detial_add_log = (EditText) findViewById(R.id.patient_detial_add_log);
        patient_detial_add_log_button = (Button) findViewById(R.id.patient_detial_add_log_button);
        patient_detial_add_log_button.setOnClickListener(patientaddLogonClickListener);

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

        patient_detail_log_listview = (ListView) findViewById(R.id.patient_detail_log_listview);
        getPatientLog();
    }

    private void getPatientLog() {
        String p_id = patient.getP_id();
        patient_logs = new ArrayList<>();
        patientlogadapter = new Adapter_log_listview(Activity_Patient_Detail.this, patient_logs);
        patient_detail_log_listview.setAdapter(patientlogadapter);
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
                        if (patient_logs.size() < 4) {
                            patient_logs.add(new Patient_Log(seq, p_id, u_id, u_name, pl_contents, pl_time));
                            patientlogadapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientLogRequest patientLogRequest = new PatientLogRequest(p_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
        queue.add(patientLogRequest);

    }

    protected void onResume() {
        super.onResume();
        getPatientLog();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm;
        String bms;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = resize(BitmapFactory.decodeStream(in));

                    patient_detail_image.setImageBitmap(img);
                    bms = BitMapToString(img);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                                    patient_detail_image.setImageBitmap(img);
                                } else {
                                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PatientImageRequest patientImageRequest = new PatientImageRequest(patient.getP_id(), bms, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Patient_Detail.this);
                    queue.add(patientImageRequest);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        return image;
    }

    View.OnClickListener patientaddLogonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String u_id = Global.getInstance().getUser().getU_id();
            String p_id = patient.getP_id();
            String pl_contents = patient_detial_add_log.getText().toString();
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
                                patient_detial_add_log.setText("");
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
    };

    View.OnClickListener patientFixOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( Global.getInstance().getUser().getIsAdmin() == 1) {
                Intent intent = new Intent(Activity_Patient_Detail.this, Activity_Patient_Fix.class);
                intent.putExtra("Patient", patient);
                launcher.launch(intent);
            } else {
                Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener patientdeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( Global.getInstance().getUser().getIsAdmin() == 1) {
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
    };

    View.OnClickListener patientLogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Activity_Patient_Detail.this, Activity_Patient_Log.class);
            intent.putExtra("Patient", patient);
            startActivity(intent);
        }
    };
}