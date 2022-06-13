package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_notification_listview;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Notification_List
역할 : 병원에 작성된 공지사항의 목록 확인
기능 :
    1) 병원에서 작성된 공지사항 목록 불러오기
특이사항 :
    - 작성자가 읽었는지 여부를 추가로 표시할 수 있도록 데이터베이스에 읽음을 추가할 예정임
    - 병원 정책에 차별을 둬 작성자 등급이 다양하질 경우 작성에 권한을 상세하게 부여할 예정
 */
public class Activity_Notification_List extends AppCompatActivity {
    private ArrayList<Notification> notifications = new ArrayList<>();
    private ListView listview_Notification;
    private Adapter_notification_listview adapter_Notification;
    private Button button_writeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        
        setUI();
        
        //사용자가 관리자일 경우에만 작성 가능 - 공지 작성 버튼 선택시 새로 작성하기 위한 페이지로 이동함
        if(Global.getInstance().getUser().getIsAdmin()){
            button_writeNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activity_Notification_List.this, Activity_Notification_Write.class);
                    startActivity(intent);
                }
            });
        }


        //공지 사항 목록을 불러와 리스트로 적용
        adapter_Notification = new Adapter_notification_listview(Activity_Notification_List.this,notifications);
        listview_Notification.setAdapter(adapter_Notification);

        listview_Notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //공지 사항을 자세히 보기위한 페이지로 이동
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activity_Notification_List.this, Activity_Notification_Detail.class);
                intent.putExtra("notification",notifications.get(i));
                startActivity(intent);
            }
        });

        //공지사항이 빈 경우 이미지를 적용
        listview_Notification.setEmptyView((TextView)findViewById(R.id.fragment_home_notification_noitem));
    }

    private void setUI() {
        listview_Notification = (ListView) findViewById(R.id.Notification_listview);
        button_writeNotification = (Button) findViewById(R.id.Notification_write_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
    }

    //공지사항 목록을 불러와 리스트로 표시합니다.
    private void getNotifications() {
        notifications = new ArrayList<>();
        adapter_Notification = new Adapter_notification_listview(this, notifications);
        listview_Notification.setAdapter(adapter_Notification);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String seq = item.getString("seq");
                        String h_id = item.getString("h_id");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String title = item.getString("title");
                        String date = item.getString("date");
                        String contents = item.getString("contents");
                        ArrayList<String> images = new ArrayList();
                        images.add(item.getString("image1"));
                        images.add(item.getString("image2"));
                        images.add(item.getString("image3"));
                        notifications.add(new Notification(seq,h_id,u_id,u_name,title,date,contents,images));
                    }
                    adapter_Notification.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NotificationRequest notificationRequest = new NotificationRequest(Global.getInstance().getHospital().getH_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Notification_List.this);
        queue.add(notificationRequest);
    }
}