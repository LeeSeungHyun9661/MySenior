package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PatientLogRequest_hid extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getPatientLog_h_id.php";
    private Map<String, String> parameters;

    public PatientLogRequest_hid(String h_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}