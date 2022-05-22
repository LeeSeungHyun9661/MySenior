package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.IDCheckRequest;
import com.example.mysenior.Request.LoginRequest;
import com.example.mysenior.Request.RegistRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_Regist extends AppCompatActivity {
    EditText register_id_input, register_pw_input, register_pw_check_input, register_name_input, register_email_input;
    ImageView register_id_image, register_pw_image,register_pw_check_image;
    TextView register_pw_info,register_id_info;
    Button register_registbutton;
    boolean checkID, checkPW, checkPWsame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        checkID = false;
        checkPW = false;
        checkPWsame = false;

        register_id_input = (EditText) findViewById(R.id.register_id_input);
        register_id_image = (ImageView) findViewById(R.id.register_id_image);
        register_id_info = (TextView) findViewById(R.id.register_id_info);
        register_id_input.addTextChangedListener(idTextWatcher);

        register_pw_input = (EditText) findViewById(R.id.register_pw_input);
        register_pw_image = (ImageView) findViewById(R.id.register_pw_image);
        register_pw_info = (TextView) findViewById(R.id.register_pw_info);
        register_pw_input.addTextChangedListener(pwTextWatcher);

        register_pw_check_input = (EditText) findViewById(R.id.register_pw_check_input);
        register_pw_check_image = (ImageView) findViewById(R.id.register_pw_check_image);
        register_pw_check_input.addTextChangedListener(pwcheckTextWatcher);

        register_name_input = (EditText) findViewById(R.id.register_name_input);
        register_email_input = (EditText) findViewById(R.id.register_email_input);

        register_registbutton = (Button) findViewById(R.id.register_registbutton);
        register_registbutton.setOnClickListener(registerClickListener);
    }

    View.OnClickListener registerClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String u_id = register_id_input.getText().toString();
            String u_pw = register_pw_input.getText().toString();
            String u_name = register_name_input.getText().toString();
            String u_email = register_email_input.getText().toString();
            if (checkID && checkPW && checkPWsame && (!u_name.equals("")) && (u_email.equals(""))) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w("RESPONSE", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                finish();
                                //화면 나가는 기능 추가
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegistRequest registRequest = new RegistRequest(u_id, u_pw, u_name, u_email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Regist.this);
                queue.add(registRequest);
            }else{
                Toast.makeText(getApplicationContext(), "회원 정보 기입을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    };

    TextWatcher idTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String u_id = register_id_input.getText().toString();
            Response.Listener<String> responseListener  = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            checkID = false;
                            register_id_image.setImageResource(R.drawable.check_x);
                        }else{
                            Pattern passPattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{7,11}$");
                            Matcher passMatcher = passPattern.matcher(u_id);
                            if(!passMatcher.find()){
                                checkID = true;
                                register_id_info.setVisibility(View.VISIBLE);
                                register_id_image.setImageResource(R.drawable.check_x);
                                return;
                            }
                            checkID= true;
                            register_id_image.setImageResource(R.drawable.check_v);
                            register_id_info.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            IDCheckRequest idCheckRequest = new IDCheckRequest(u_id,responseListener);
            RequestQueue queue = Volley.newRequestQueue(Activity_Regist.this);
            queue.add(idCheckRequest);
        }
    };

    TextWatcher pwTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String u_pw = register_pw_input.getText().toString();
            Pattern passPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
            Matcher passMatcher = passPattern.matcher(u_pw);
            if(!passMatcher.find()){
                checkPW= false;
                register_pw_info.setVisibility(View.VISIBLE);
                register_pw_image.setImageResource(R.drawable.check_x);
                return;
            }
            checkPW= true;
            register_pw_image.setImageResource(R.drawable.check_v);
            register_pw_info.setVisibility(View.INVISIBLE);
        }
    };

    TextWatcher pwcheckTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable editable) {
            String u_pw = register_pw_input.getText().toString();
            String u_pw_check = register_pw_check_input.getText().toString();
            if (u_pw.equals(u_pw_check)){
                checkPWsame = true;
                register_pw_check_image.setImageResource(R.drawable.check_v);
            }else{
                checkPWsame = false;
                register_pw_check_image.setImageResource(R.drawable.check_x);
            }
        }
    };

}