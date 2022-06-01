package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LogRemoveRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/deleteLog.php";
    private Map<String, String> parameters;

    public LogRemoveRequest(String seq, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("seq", seq);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}