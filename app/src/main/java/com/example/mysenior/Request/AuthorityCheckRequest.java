package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AuthorityCheckRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/cheakAuthority.php";
    private Map<String, String> parameters;

    public AuthorityCheckRequest(String u_id,String h_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("u_id", u_id);
        parameters.put("h_id", h_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}