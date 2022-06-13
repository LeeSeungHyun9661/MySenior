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

이름 : RosterWorkerRequest
역할 : 날짜별 근무 인원 요청
 */
public class RosterWorkerRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getRosterWorker.php";
    private Map<String, String> parameters;

    public RosterWorkerRequest(String h_id, String searchday, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("searchday", searchday);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}