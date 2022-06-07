package com.example.mysenior.Activity;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityAddRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Authority_Write extends Activity {
    EditText authorityWrite_position,authorityWrite_department;
    ImageView authorityWrite_hospitalimage;
    TextView authorityWrite_username,authorityWrite_hospitaldate,authorityWrite_hospitalname,authorityWrite_hospitalcategory,authorityWrite_hospitallocation,authorityWrite_hospitalphone;
    Button authorityWrite_button;
    RadioGroup authorityWrite_radiogroup;
    RadioButton authorityWrite_radiogroup_worker,authorityWrite_radiogroup_admin;
    boolean isAdmin;
    Hospital hospital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_write);
        Intent intent = getIntent();
        hospital = (Hospital) intent.getSerializableExtra("Hospital");

        isAdmin = false;

        authorityWrite_position = (EditText) findViewById(R.id.authorityWrite_position);
        authorityWrite_department = (EditText) findViewById(R.id.authorityWrite_department);

        authorityWrite_hospitalimage = (ImageView) findViewById(R.id.authorityWrite_hospitalimage);
        if (hospital.getH_image().equals("null")) authorityWrite_hospitalimage.setImageResource(R.drawable.no_image);
        else authorityWrite_hospitalimage.setImageBitmap(hospital.getH_imageBitmap());


        authorityWrite_username = (TextView)findViewById(R.id.authorityWrite_username);
        authorityWrite_username.setText(Global.getInstance().getUser().getU_name());

        authorityWrite_hospitaldate = (TextView) findViewById(R.id.authorityWrite_hospitaldate);
        authorityWrite_hospitaldate.setText(hospital.getH_date());

        authorityWrite_hospitalname = (TextView) findViewById(R.id.authorityWrite_hospitalname);
        authorityWrite_hospitalname.setText(hospital.getH_name());

        authorityWrite_hospitalcategory = (TextView) findViewById(R.id.authorityWrite_hospitalcategory);
        authorityWrite_hospitalcategory.setText(hospital.getH_category());

        authorityWrite_hospitallocation = (TextView) findViewById(R.id.authorityWrite_hospitallocation);
        authorityWrite_hospitallocation.setText(hospital.getH_location());

        authorityWrite_hospitalphone = (TextView) findViewById(R.id.authorityWrite_hospitalphone);
        authorityWrite_hospitalphone.setText(hospital.getH_phone());

        authorityWrite_button = (Button) findViewById(R.id.authorityWrite_button);
        authorityWrite_button.setOnClickListener(authorityWriteonClickListener);

        authorityWrite_radiogroup_worker = (RadioButton) findViewById(R.id.authorityWrite_radiogroup_worker);
        authorityWrite_radiogroup_worker.setChecked(true);
        authorityWrite_radiogroup_admin = (RadioButton) findViewById(R.id.authorityWrite_radiogroup_admin);
        authorityWrite_radiogroup = (RadioGroup) findViewById(R.id.authorityWrite_radiogroup);
        authorityWrite_radiogroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }

    View.OnClickListener authorityWriteonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String u_id = Global.getInstance().getUser().getU_id();
            String h_id = hospital.getH_id();
            String department = authorityWrite_department.getText().toString();
            String position = authorityWrite_position.getText().toString();
            String admin;
            if(isAdmin) {
                admin = "1";
            }else{
                admin = "0";
            }

            if (u_id.isEmpty() || h_id.isEmpty() || department.isEmpty() || position.isEmpty()) {
                Toast.makeText(getApplicationContext(), "가입 신청 모두 입력해주세요", Toast.LENGTH_SHORT).show();
            }else{
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w("RESPONSE", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Activity_Authority_Search activity_authority_search = (Activity_Authority_Search)Activity_Authority_Search.activity_authority_search;
                                activity_authority_search.finish();
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
                AuthorityAddRequest authorityAddRequest = new AuthorityAddRequest(u_id, h_id, department, position, admin, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Write.this);
                queue.add(authorityAddRequest);
            }

        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.authorityWrite_radiogroup_worker){
                isAdmin = false;
            }
            else if(i == R.id.authorityWrite_radiogroup_admin){
                isAdmin = true;
            }
        }
    };
}