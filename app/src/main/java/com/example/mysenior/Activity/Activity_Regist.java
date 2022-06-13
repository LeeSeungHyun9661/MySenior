package com.example.mysenior.Activity;

import android.app.Activity;
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
import com.example.mysenior.R;
import com.example.mysenior.Request.IDCheckRequest;
import com.example.mysenior.Request.RegistRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Regist
역할 : 새로운 사용자로 등록할 수 있는 기능을 추가
기능 :
    1) 아이디, 비밀번호, 이메일에 대해 중복 및 형식 확인이 가능함
    2) 해당 데이터를 서버로 업로드
특이사항 :
    - 사용자의 이메일에 대한 중복 검사 추가 예정
    - 사용자 이메일에 메일을 보내고 신원 확인이 가능하도록 확인하는 절차를 추가로 기입할 예정
 */
public class Activity_Regist extends Activity {
    private EditText edittext_id, edittext_pw, edittext_pwcheck, edittext_name, edittext_email;
    private ImageView imageview_id, imageview_pw, imageview_pwcheck, imageview_email;
    private TextView textview_PWinfo, textview_IDinfo;
    private Button button_regist;
    boolean checkID, checkPW, checkPWsame, checkEmailsame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        setUI();

        checkID = false;
        checkPW = false;
        checkPWsame = false;
        checkEmailsame = false;

        //아이디 입력에 따라 아이디 형식과 중복을 확인함
        edittext_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String u_id = edittext_id.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                checkID = false;
                                imageview_id.setImageResource(R.drawable.check_x);
                            } else {
                                Pattern passPattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{7,11}$");
                                Matcher passMatcher = passPattern.matcher(u_id);
                                if (!passMatcher.find()) {
                                    checkID = true;
                                    textview_IDinfo.setVisibility(View.VISIBLE);
                                    imageview_id.setImageResource(R.drawable.check_x);
                                    return;
                                }
                                checkID = true;
                                imageview_id.setImageResource(R.drawable.check_v);
                                textview_IDinfo.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                IDCheckRequest idCheckRequest = new IDCheckRequest(u_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Regist.this);
                queue.add(idCheckRequest);
            }
        });

        //비밀번호 입력에 따라 비밀번호 형식을 확인함
        edittext_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String u_pw = edittext_pw.getText().toString();
                String u_pw_check = edittext_pwcheck.getText().toString();
                if (u_pw.equals(u_pw_check)) {
                    checkPWsame = true;
                    imageview_pwcheck.setImageResource(R.drawable.check_v);
                } else {
                    checkPWsame = false;
                    imageview_pwcheck.setImageResource(R.drawable.check_x);
                }
            }
        });

        //비밀번호 입력에 따라 비밀번호가 동일한지 확인함
        edittext_pwcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String u_pw = edittext_pw.getText().toString();
                Pattern passPattern = Pattern.compile("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$");
                Matcher passMatcher = passPattern.matcher(u_pw);
                if (!passMatcher.find()) {
                    checkPW = false;
                    textview_PWinfo.setVisibility(View.VISIBLE);
                    imageview_pw.setImageResource(R.drawable.check_x);
                    return;
                }
                checkPW = true;
                imageview_pw.setImageResource(R.drawable.check_v);
                textview_PWinfo.setVisibility(View.INVISIBLE);
            }
        });

        //이메일 형식이 정확한지 확인함
        edittext_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String u_email = edittext_email.getText().toString();
                Pattern passPattern = android.util.Patterns.EMAIL_ADDRESS;
                if (passPattern.matcher(u_email).matches()) {
                    checkEmailsame = true;
                    imageview_email.setImageResource(R.drawable.check_v);
                } else {
                    checkEmailsame = false;
                    imageview_email.setImageResource(R.drawable.check_x);
                }
            }
        });

        //등록 버튼을 통해 회원 정보를 새롭게 업로드함
        button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_id = edittext_id.getText().toString();
                String u_pw = edittext_pw.getText().toString();
                String u_name = edittext_name.getText().toString();
                String u_email = edittext_email.getText().toString();
                if (checkID && checkPW && checkPWsame && checkEmailsame && (!u_name.equals("")) && (!u_email.equals(""))) {
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
                } else {
                    Toast.makeText(getApplicationContext(), "회원 정보 기입을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUI() {
        edittext_id = (EditText) findViewById(R.id.register_id_input);
        imageview_id = (ImageView) findViewById(R.id.register_id_image);
        textview_IDinfo = (TextView) findViewById(R.id.register_id_info);
        edittext_pw = (EditText) findViewById(R.id.register_pw_input);
        imageview_pw = (ImageView) findViewById(R.id.register_pw_image);
        textview_PWinfo = (TextView) findViewById(R.id.register_pw_info);
        edittext_pwcheck = (EditText) findViewById(R.id.register_pw_check_input);
        imageview_pwcheck = (ImageView) findViewById(R.id.register_pw_check_image);
        edittext_name = (EditText) findViewById(R.id.register_name_input);
        edittext_email = (EditText) findViewById(R.id.register_email_input);
        imageview_email = (ImageView) findViewById(R.id.register_email_check_image);
        button_regist = (Button) findViewById(R.id.register_registbutton);
    }
}