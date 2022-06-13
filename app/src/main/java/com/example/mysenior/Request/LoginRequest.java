package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
리퀘스트 클래스

이름 : LoginRequest
역할 : 아이디와 비밀번호를 통한 계정 로그인
 */
public class LoginRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/login.php";
    private Map<String, String> parameters;

    public LoginRequest(String u_id, String u_pw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("u_pw", u_pw);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

