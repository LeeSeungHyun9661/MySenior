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

이름 : NotificationFixRequest
역할 : 환자 기록 수정 요청
 */
public class NotificationFixRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/fixNotification.php";
    private Map<String, String> parameters;

    public NotificationFixRequest(String seq,String title,String contents,String image1,String image2,String image3, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("seq", seq);
        parameters.put("title", title);
        parameters.put("contents", contents);
        parameters.put("image1", image1);
        parameters.put("image2", image2);
        parameters.put("image3", image3);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}