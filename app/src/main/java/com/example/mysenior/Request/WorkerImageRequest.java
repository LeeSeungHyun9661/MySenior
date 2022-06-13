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

이름 : WorkerDeleteRequest
역할 : 직원 이미지 변경 요청
 */
public class WorkerImageRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/uploadImageWorker.php";
    private Map<String, String> parameters;

    public WorkerImageRequest(String u_id, String u_image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("u_image", u_image);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
