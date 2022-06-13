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

이름 : RosterMonthlyRequest
역할 : 사용자의 월간 근무내용 요청
 */
public class RosterMonthlyRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getMonthlyRoster.php";
    private Map<String, String> parameters;

    public RosterMonthlyRequest(String h_id, String u_id, String startday,String endday, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("u_id", u_id);
        parameters.put("startday", startday);
        parameters.put("endday", endday);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}