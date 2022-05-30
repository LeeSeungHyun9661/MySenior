package com.example.mysenior.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Login extends AppCompatActivity {
    EditText login_id_input, login_pw_input;
    Button login_loginbutton, login_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id_input = (EditText) findViewById(R.id.login_id_input);
        login_pw_input = (EditText) findViewById(R.id.login_pw_input);

        //로그인버튼
        login_loginbutton = (Button) findViewById(R.id.login_loginbutton);
        login_loginbutton.setOnClickListener(loginClickListener);

        //회원가입버튼
        login_regist = (Button) findViewById(R.id.login_regist);
        login_regist.setOnClickListener(registerClickListener);
    }

    View.OnClickListener loginClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String u_id = login_id_input.getText().toString();
            String u_pw = login_pw_input.getText().toString();
            Response.Listener<String> responseListener  = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            String u_id = jsonResponse.getString("u_id");
                            String u_name = jsonResponse.getString("u_name");
                            String u_image = jsonResponse.getString("u_image");
                            User user = new User(u_id,u_name,u_image);
                            Intent intent = new Intent(getApplicationContext(), Activity_Authority_List.class);
                            intent.putExtra("User", user);
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
    };

    View.OnClickListener registerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Activity_Regist.class);
            startActivity(intent);
        }
    };

}