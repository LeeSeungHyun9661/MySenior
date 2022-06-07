package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_authority_listview;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.AuthorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_Authority_List extends AppCompatActivity {
    ArrayList<Authority> authorityArrayList;
    ArrayList<Hospital> hospitalArrayList;
    Adapter_authority_listview authorityList_listview_adapter;
    ListView authorityList_listview;
    Button authorityList_authority_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_list);
        authorityArrayList = new ArrayList<>();
        authorityList_authority_write = (Button) findViewById(R.id.authorityList_authority_write);
        authorityList_authority_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Authority_Search.class);
                startActivity(intent);
            }
        });

        authorityList_listview = (ListView) findViewById(R.id.authorityList_listview);
        authorityList_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(authorityArrayList.get(position).isCheck()){
                    Intent intent = new Intent(getApplicationContext(), Activity_Hospital.class);
                    Global.getInstance().setHospital(hospitalArrayList.get(position));
                    Global.getInstance().getUser().setHospitalAccess(authorityArrayList.get(position).getDepartment(), authorityArrayList.get(position).getPosition(), authorityArrayList.get(position).isAdmin());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "병원에서 권한을 심사중입니다...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        authorityList_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Authority_List.this);
                dlg.setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String a_id = authorityArrayList.get(position).getA_id();
                                removeAuthority(a_id);
                            }
                        }).show();
                return true;
            }
        });
    }

    public void onResume() {
        super.onResume();
        getAuthority_HospitalArrayList();
    }

    private void getAuthority_HospitalArrayList() {
        authorityArrayList = new ArrayList<>();
        hospitalArrayList = new ArrayList<>();
        authorityList_listview_adapter = new Adapter_authority_listview(this, authorityArrayList,hospitalArrayList);
        authorityList_listview.setAdapter(authorityList_listview_adapter);
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
                        String h_name = item.getString("h_name");
                        String h_category = item.getString("h_category");
                        String h_location = item.getString("h_location");
                        String h_phone = item.getString("h_phone");
                        Date h_date =  new SimpleDateFormat("yyyy-MM-dd").parse(item.getString("h_date"));
                        String h_image = item.getString("h_image");

                        Authority authority = new Authority(a_id, u_id, h_id, position, department, ischeck, isadmin);
                        authorityArrayList.add(authority);
                        Hospital hospital = new Hospital(h_id, h_name, h_category, h_location, h_phone, h_image, h_date);
                        hospitalArrayList.add(hospital);

                    }
                    authorityList_listview_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRequest authorityRequest = new AuthorityRequest(Global.getInstance().getUser().getU_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
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
                        getAuthority_HospitalArrayList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRemoveRequest authorityRemoveRequest = new AuthorityRemoveRequest(a_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
        queue.add(authorityRemoveRequest);
    }
}