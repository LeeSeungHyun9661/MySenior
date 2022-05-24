package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_hospital_search_listview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityCheckRequest;
import com.example.mysenior.Request.HospitalSearchRequest;
import com.example.mysenior.Request.LoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_Authority_Search extends AppCompatActivity {
    public static Activity activity;
    EditText authoritySearch_hospitalsearch;
    Button authoritySearch_nextStep;
    ListView authoritySearch_Listview;
    ArrayList<Hospital> hospotalSearch;
    Adapter_hospital_search_listview authoritySearchadapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = Activity_Authority_Search.this;
        setContentView(R.layout.activity_authority_search);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        hospotalSearch = new ArrayList<>();

        authoritySearch_nextStep = (Button) findViewById(R.id.authoritySearch_nextStep);

        authoritySearch_hospitalsearch = (EditText) findViewById(R.id.authoritySearch_hospitalsearch);
        authoritySearch_hospitalsearch.addTextChangedListener(authoritySearchTextWatcher);

        authoritySearch_Listview = (ListView) findViewById(R.id.authoritySearch_Listview);
    }

    TextWatcher authoritySearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String search = authoritySearch_hospitalsearch.getText().toString();
            if(search.length() >= 2){
                searchHospital(search);
            }
            authoritySearch_Listview.setOnItemClickListener(searchListviewItemClickListener);
        }
    };

    private void searchHospital(String search) {
        hospotalSearch = new ArrayList<>();
        authoritySearchadapter = new Adapter_hospital_search_listview(this,hospotalSearch, user);
        authoritySearch_Listview.setAdapter(authoritySearchadapter);
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("hospital");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String h_id = item.getString("h_id");
                        String h_name = item.getString("h_name");
                        String h_category = item.getString("h_category");
                        String h_location = item.getString("h_location");
                        String h_phone = item.getString("h_phone");
                        Date h_date =  new SimpleDateFormat("yyyy-MM-dd").parse(item.getString("h_date"));
                        String h_image = item.getString("h_image");
                        hospotalSearch.add(new Hospital(h_id,h_name,h_category,h_location,h_phone,h_image,h_date));
                        authoritySearchadapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        HospitalSearchRequest hospitalSearchRequest = new HospitalSearchRequest(search,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Search.this);
        queue.add(hospitalSearchRequest);
    }

    AdapterView.OnItemClickListener searchListviewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Response.Listener<String> responseListener  = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            Toast.makeText(getApplicationContext(), "해당 병원에 대해 권한을 보유하고 있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Intent intent = new Intent(getApplicationContext(), Activity_Authority_Write.class);
                            intent.putExtra("User", user);
                            intent.putExtra("Hospital",  hospotalSearch.get(i));
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            AuthorityCheckRequest authorityCheckRequest = new AuthorityCheckRequest(user.getU_id(),hospotalSearch.get(i).getH_id(),responseListener);
            RequestQueue queue = Volley.newRequestQueue(Activity_Authority_Search.this);
            queue.add(authorityCheckRequest);
        }
    };
}