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
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;
import com.example.mysenior.Request.AuthorityRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Authority_List extends AppCompatActivity {

    ArrayList<Authority> authorityArrayList;
    Adapter_authority_listview authorityList_listview_adapter;
    ListView authorityList_listview;
    Button authorityList_authority_write;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_list);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        authorityArrayList = new ArrayList<>();

        authorityList_authority_write = (Button) findViewById(R.id.authorityList_authority_write);
        authorityList_authority_write.setOnClickListener(authorityWriteOnClickListener);

        authorityList_listview = (ListView) findViewById(R.id.authorityList_listview);
        authorityList_listview.setOnItemClickListener(authorityList_listview_ItemClickListener);
        authorityList_listview.setOnItemLongClickListener(authorityList_listview_ItemLongClickListener);
    }

    public void onResume() {
        super.onResume();
        getAuthorityArrayList();
    }

    private void getAuthorityArrayList() {
        String u_id = user.getU_id();
        authorityArrayList = new ArrayList<>();
        authorityList_listview_adapter = new Adapter_authority_listview(this, authorityArrayList, user);
        authorityList_listview.setAdapter(authorityList_listview_adapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("hospital");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String h_id = item.getString("h_id");
                        String h_image = item.getString("h_image");
                        String h_name = item.getString("h_name");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int ischeck = item.getInt("ischeck");
                        int isadmin = item.getInt("isadmin");
                        Authority authority = new Authority(a_id, u_id, h_id, h_name, h_image, position, department, ischeck, isadmin);
                        authorityArrayList.add(authority);
                        authorityList_listview_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        AuthorityRequest authorityRequest = new AuthorityRequest(u_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Authority_List.this);
        queue.add(authorityRequest);
    }

    View.OnClickListener authorityWriteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Activity_Authority_Search.class);
            intent.putExtra("User", user);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener authorityList_listview_ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if(authorityArrayList.get(position).isCheck()){
                Intent intent = new Intent(getApplicationContext(), Activity_Hospital.class);
                user.setHospitalAccess(authorityArrayList.get(position).getH_id(), authorityArrayList.get(position).getDepartment(), authorityArrayList.get(position).getPosition(), authorityArrayList.get(position).isAdmin());
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "병원에서 권한을 심사중입니다...", Toast.LENGTH_SHORT).show();
            }

        }
    };

    AdapterView.OnItemLongClickListener authorityList_listview_ItemLongClickListener = new AdapterView.OnItemLongClickListener() {
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
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            getAuthorityArrayList();
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
                    }).show();
            return true;
        }
    };
}