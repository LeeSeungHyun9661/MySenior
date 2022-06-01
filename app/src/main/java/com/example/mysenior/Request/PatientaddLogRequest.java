package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PatientaddLogRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/addPatientLog.php";
    private Map<String, String> parameters;

    public PatientaddLogRequest(String p_id, String u_id, String pl_contents, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("p_id", p_id);
        parameters.put("u_id", u_id);
        parameters.put("pl_contents", pl_contents);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}