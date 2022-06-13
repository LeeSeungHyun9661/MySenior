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

이름 : LogRemoveRequest
역할 : 환자 기록 삭제 요청
 */
public class LogRemoveRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/deleteLog.php";
    private Map<String, String> parameters;

    public LogRemoveRequest(String seq, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("seq", seq);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}