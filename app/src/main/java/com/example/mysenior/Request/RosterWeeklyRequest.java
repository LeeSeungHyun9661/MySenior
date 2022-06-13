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

이름 : RosterWeeklyRequest
역할 : 사용자의 주간 근무내용 요청
 */
public class RosterWeeklyRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getWeeklyRoster.php";
    private Map<String, String> parameters;

    public RosterWeeklyRequest(String h_id, String u_id, String r_date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("r_date", r_date);
        parameters.put("h_id", h_id);
        parameters.put("u_id", u_id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}