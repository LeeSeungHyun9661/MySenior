package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PatientRequest_p_id extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getPatient_p_id.php";
    private Map<String, String> parameters;

    public PatientRequest_p_id(String p_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("p_id", p_id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}