package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RosterMonthlyRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/getMonthlyRoster.php";
    private Map<String, String> parameters;

    public RosterMonthlyRequest(String h_id, String u_id, String startday,String endday, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("h_id", h_id);
        parameters.put("u_id", u_id);
        parameters.put("startday", startday);
        parameters.put("endday", endday);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}