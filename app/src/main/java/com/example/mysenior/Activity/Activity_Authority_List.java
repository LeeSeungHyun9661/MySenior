package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_authority_listview;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRequest;
import com.example.mysenior.Request.LoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Authority_List extends AppCompatActivity {

    ArrayList<Authority> authorityArrayList;
    Adapter_authority_listview authorityList_listview_adapter;
    ListView authorityList_listview;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_list);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        authorityArrayList = new ArrayList<>();

        authorityList_listview = (ListView)findViewById(R.id.authorityList_listview);
        authorityList_listview_adapter = new Adapter_authority_listview(this,authorityArrayList);
        authorityList_listview.setAdapter(authorityList_listview_adapter);
        getAuthorityArrayList();
        Log.w("","_________________________________________________________");
        Log.w("Response_authority", authorityArrayList.toString());
        Log.w("","_________________________________________________________");
    }

    private void getAuthorityArrayList() {
        String u_id = user.getU_id();
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("hospital");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String h_id =item.getString("h_id");
                        String h_name =item.getString("h_name");
                        String position =item.getString("position");
                        String department =item.getString("department");
                        int ischeck =item.getInt("ischeck");
                        int isadmin =item.getInt("isadmin");
                        Authority authority = new Authority(a_id,u_id,h_id,h_name,position,department,ischeck,isadmin);
                        authorityArrayList.add(authority);
                        authorityList_listview_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRequest authorityRequest = new AuthorityRequest(u_id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
        queue.add(authorityRequest);
    }

}