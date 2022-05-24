package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AuthorityRemoveRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/removeAuthority.php";
    private Map<String, String> parameters;

    public AuthorityRemoveRequest(String a_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("a_id", a_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}