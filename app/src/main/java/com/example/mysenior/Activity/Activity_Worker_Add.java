package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_worker_authority_listview;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityApproveRequest;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.WorkerAuthorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Worker_Add extends AppCompatActivity {
    
    ListView worker_authority_listview;
    ArrayList<Authority> authorityArrayList;
    ArrayList<String> nameList;
    Adapter_worker_authority_listview adapter_worker_authority_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_add);

        worker_authority_listview = (ListView) findViewById(R.id.worker_authority_listview);
        worker_authority_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Worker_Add.this);
                dlg.setTitle("권한 부여")
                        .setMessage("해당 인원을 병원 직원으로 승인합니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String a_id = authorityArrayList.get(i).getA_id();
                                approveAuthority(a_id);
                            }
                        })
                        .setPositiveButton("거절", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String a_id = authorityArrayList.get(i).getA_id();
                                removeAuthority(a_id);
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuthorityList();
    }

    private void getAuthorityList() {
        String h_id = Global.getInstance().getHospital().getH_id();
        authorityArrayList = new ArrayList<>();
        nameList = new ArrayList<>();
        adapter_worker_authority_listview = new Adapter_worker_authority_listview(this, authorityArrayList,nameList);
        worker_authority_listview.setAdapter(adapter_worker_authority_listview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String h_id = item.getString("h_id");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int ischeck = item.getInt("ischeck");
                        int isadmin = item.getInt("isadmin");
                        Authority authority = new Authority(a_id, u_id, h_id, position, department, ischeck, isadmin);
                        authorityArrayList.add(authority);
                        nameList.add(item.getString("u_name"));
                    }
                    adapter_worker_authority_listview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        WorkerAuthorityRequest authorityRequest = new WorkerAuthorityRequest(h_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityRequest);
    }

    private void removeAuthority(String a_id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        getAuthorityList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRemoveRequest authorityRemoveRequest = new AuthorityRemoveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityRemoveRequest);
    }

    private void approveAuthority(String a_id){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        getAuthorityList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityApproveRequest authorityApproveRequest = new AuthorityApproveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Add.this);
        queue.add(authorityApproveRequest);
    }
}