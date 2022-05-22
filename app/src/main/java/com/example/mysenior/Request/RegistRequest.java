package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/regist.php";
    private Map<String, String> parameters;

    public RegistRequest(String u_id, String u_pw,String u_name,String u_email, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("u_pw", u_pw);
        parameters.put("u_name", u_name);
        parameters.put("u_email", u_email);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}