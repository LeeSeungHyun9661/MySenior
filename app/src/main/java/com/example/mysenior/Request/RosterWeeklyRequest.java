package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RosterWeeklyRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getWeeklyRoster.php";
    private Map<String, String> parameters;

    public RosterWeeklyRequest(String h_id, String u_id, String r_date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("r_date", r_date);
        parameters.put("h_id", h_id);
        parameters.put("u_id", u_id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}