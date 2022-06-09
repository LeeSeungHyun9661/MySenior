package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

public class Activity_Notification extends AppCompatActivity {
    ArrayList<Notification> notifications = new ArrayList<>();
    ListView Notification_listview;
    Adapter_notification_listview notification_adapter;
    Button Notification_write_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();

        Notification_write_button= (Button) findViewById(R.id.Notification_write_button);
        Notification_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Notification.this, Activity_Notification_Write.class);
                startActivity(intent);
            }
        });

        Notification_listview = (ListView) findViewById(R.id.Notification_listview);
        notification_adapter = new Adapter_notification_listview(Activity_Notification.this,notifications);
        Notification_listview.setAdapter(notification_adapter);
        Notification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activity_Notification.this, Activity_Notification_Detail.class);
                intent.putExtra("notification",notifications.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
    }

    private void getNotifications() {
        notifications = new ArrayList<>();
        notification_adapter = new Adapter_notification_listview(this, notifications);
        Notification_listview.setAdapter(notification_adapter);
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
                        if (notifications.size() < 1) {
                            notification_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NotificationRequest notificationRequest = new NotificationRequest(Global.getInstance().getHospital().getH_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Activity_Notification.this);
        queue.add(notificationRequest);
    }
}