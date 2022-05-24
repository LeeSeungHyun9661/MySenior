package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AuthorityAddRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/addAuthority.php";
    private Map<String, String> parameters;

    public AuthorityAddRequest(String u_id,String h_id,String position,String department,String isadmin, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("h_id", h_id);
        parameters.put("position", position);
        parameters.put("department", department);
        parameters.put("isadmin", isadmin);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}