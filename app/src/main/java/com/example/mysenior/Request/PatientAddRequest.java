package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PatientAddRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/addPatient.php";
    private Map<String, String> parameters;

    public PatientAddRequest(String h_id, String p_name,String p_id, String p_age,String p_gender,String p_birth,String p_ward,String p_NOK,String p_NOK_phone,String p_admin,String p_addr, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("p_name", p_name);
        parameters.put("p_id", p_id);
        parameters.put("p_age", p_age);
        parameters.put("p_gender", p_gender);
        parameters.put("p_birth", p_birth);
        parameters.put("p_ward", p_ward);
        parameters.put("p_NOK", p_NOK);
        parameters.put("p_NOK_phone", p_NOK_phone);
        parameters.put("p_admin", p_admin);
        parameters.put("p_addr", p_addr);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}