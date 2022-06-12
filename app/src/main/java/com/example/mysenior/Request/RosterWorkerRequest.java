package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RosterWorkerRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getRosterWorker.php";
    private Map<String, String> parameters;

    public RosterWorkerRequest(String h_id, String searchday, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("searchday", searchday);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}