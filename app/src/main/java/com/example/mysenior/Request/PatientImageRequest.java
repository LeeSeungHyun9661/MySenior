package com.example.mysenior.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PatientImageRequest extends StringRequest {

    final static private String URL = "https://dippingai.com/mysenior/uploadImagePatient.php";
    private Map<String, String> parameters;

    public PatientImageRequest(String p_id,String p_image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("p_id", p_id);
        parameters.put("p_image", p_image);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
