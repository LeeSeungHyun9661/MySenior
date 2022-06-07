package com.example.mysenior.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.LoginRequest;
import org.json.JSONException;
import org.json.JSONObject;

/*
MySenior
작성일자 : 2022-06-07
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
클래스

이름 : Activity_Login
역할 : 앱 시작시의 로그인 및 회원가입 접근

 */

public class Activity_Login extends AppCompatActivity {
    TextView login_regist;
    EditText login_id_input, login_pw_input;
    Button login_loginbutton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id_input = (EditText) findViewById(R.id.login_id_input);
        login_pw_input = (EditText) findViewById(R.id.login_pw_input);
        login_pw_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)  {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    login();
                    return true;
                }
                return false;
            }
        });

        //로그인버튼 설정
        login_loginbutton = (Button) findViewById(R.id.login_loginbutton);
        login_loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //회원가입버튼 설정
        login_regist = (TextView) findViewById(R.id.login_regist);
        login_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Regist.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String u_id = login_id_input.getText().toString();
        String u_pw = login_pw_input.getText().toString();
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        String u_id = jsonResponse.getString("u_id");
                        String u_name = jsonResponse.getString("u_name");
                        String u_image = jsonResponse.getString("u_image");
                        Intent intent = new Intent(getApplicationContext(), Activity_Authority_List.class);
                        Global.getInstance().setUser(new User(u_id,u_name,u_image));
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(u_id,u_pw,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);
        queue.add(loginRequest);
    }

}