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

이름 : HospitalImageRequest
역할 : 병원의 이미지를 수정 요청
 */
public class HospitalImageRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/uploadImageHospital.php";
    private Map<String, String> parameters;

    public HospitalImageRequest(String h_id, String h_image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("h_image", h_image);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
