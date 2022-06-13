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
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 :  Activity_Login
역할 : 애플리케이션 접근을 위한 아이디 비밀번호 입력
기능 :
    1) 아이디와 비밀번호 입력을 통해 계정 확인 및 접근
    2) 회원 가입을 위한 페이지로 이동 가능
특이사항 :
    - 자동 로그인 추가 구현 필요
    - 권한 요청에 따른 안내 다이어그램이 추가될 예정
 */
public class Activity_Login extends AppCompatActivity {
    TextView textview_regist;
    EditText edittext_IDinput, edittext_PWinput;
    Button button_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUI();

        //암호 입력칸에서 앤터 입력시 로그인 단계 실행
        edittext_PWinput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)  {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    login();
                    return true;
                }
                return false;
            }
        });

        //로그인버튼 설정
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //회원가입버튼 설정
        textview_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Regist.class);
                startActivity(intent);
            }
        });
    }

    private void setUI() {
        edittext_IDinput = (EditText) findViewById(R.id.login_id_input);
        edittext_PWinput = (EditText) findViewById(R.id.login_pw_input);
        button_login = (Button) findViewById(R.id.login_loginbutton);
        textview_regist = (TextView) findViewById(R.id.login_regist);
    }

    //로그인 단계 진행
    private void login() {
        String u_id = edittext_IDinput.getText().toString();
        String u_pw = edittext_PWinput.getText().toString();
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //로그인 과정 진행
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //로그인 성공 시
                    if(success){
                        String u_id = jsonResponse.getString("u_id");
                        String u_name = jsonResponse.getString("u_name");
                        String u_image = jsonResponse.getString("u_image");
                        
                        //받은 사용자 정보를 새로운 글로벌 변수에 저장하고 액티비티를 실행함
                        Intent intent = new Intent(getApplicationContext(), Activity_Authority_List.class);
                        Global.getInstance().setUser(new User(u_id,u_name,u_image));
                        startActivity(intent);
                        finish();

                        //로그인 실패 시
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