package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Authority_List;
import com.example.mysenior.Activity.Activity_Notification_List;
import com.example.mysenior.Activity.Activity_Notification_Detail;
import com.example.mysenior.Activity.Activity_Patient_Detail;
import com.example.mysenior.Activity.Activity_Patient_Log;
import com.example.mysenior.Adapter.Adapter_log_listview;
import com.example.mysenior.Adapter.Adapter_notification_listview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.Monitor_Detaction;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationRequest;
import com.example.mysenior.Request.PatientLogRequest;
import com.example.mysenior.Request.PatientLogRequest_hid;
import com.example.mysenior.Request.PatientRequest;
import com.example.mysenior.Request.PatientRequest_p_id;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_home extends Fragment {
    Hospital hospital;
    User user;
    Button fragment_home_notification_more;
    ListView fragment_home_notification_Listview, fragment_home_detection_Listview, fragment_home_log_Listview;
    Adapter_notification_listview notification_adapter;
    Adapter_log_listview patientlogadapter;

    ArrayList<Notification> notifications;
    ArrayList<Patient_Log> patient_logs;
    ArrayList<Monitor_Detaction> detactionArrayList;

    public Fragment_home(User user, Hospital hospital){
        this.user = user;
        this.hospital = hospital;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //공지사항 리스트뷰
        fragment_home_notification_Listview = (ListView) view.findViewById(R.id.fragment_home_notification_Listview);
        fragment_home_notification_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Notification_Detail.class);
                intent.putExtra("notification", notifications.get(i));
                startActivity(intent);
            }
        });
        fragment_home_notification_Listview.setEmptyView((TextView)view.findViewById(R.id.fragment_home_notification_noitem));
        
        //공지사항 리스트 아이템 클릭 리스너
        fragment_home_notification_more = (Button) view.findViewById(R.id.fragment_home_notification_more);
        fragment_home_notification_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Notification_List.class);
                startActivity(intent);
            }
        });

        fragment_home_detection_Listview = (ListView) view.findViewById(R.id.fragment_home_detection_Listview);

        fragment_home_log_Listview = (ListView) view.findViewById(R.id.fragment_home_log_Listview);
        fragment_home_log_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p_id  = patient_logs.get(position).getP_id();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("RES",response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                String h_id = jsonResponse.getString("h_id");
                                String p_id = jsonResponse.getString("p_id");
                                String p_name = jsonResponse.getString("p_name");
                                String p_gender = jsonResponse.getString("p_gender");
                                String p_ward = jsonResponse.getString("p_ward");
                                String p_NOK = jsonResponse.getString("p_NOK");
                                String p_NOK_phone = jsonResponse.getString("p_NOK_phone");
                                String p_admin = jsonResponse.getString("p_admin");
                                String p_addr = jsonResponse.getString("p_addr");
                                String p_qr = jsonResponse.getString("p_qr");
                                String p_image = jsonResponse.getString("p_image");
                                int p_age = Integer.parseInt(jsonResponse.getString("p_age"));
                                String p_birth =  jsonResponse.getString("p_birth");
                                Patient patient = new Patient(h_id, p_id, p_name, p_gender, p_ward, p_NOK, p_NOK_phone, p_admin, p_addr,p_image, p_qr, p_age, p_birth);

                                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Patient_Detail.class);
                                intent.putExtra("Patient",  patient);
                                startActivity(intent);

                            }else{
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                PatientRequest_p_id patientRequest = new PatientRequest_p_id(p_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(patientRequest);
            }
        });

        initWeeklyRoster(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifications();
        getRoster();
        getDetactions();
        getLogs();
    }

    private void getNotifications() {
        notifications = new ArrayList<>();
        notification_adapter = new Adapter_notification_listview(getActivity(), notifications);
        fragment_home_notification_Listview.setAdapter(notification_adapter);
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
                        if (notifications.size() < 2) {
                            notification_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NotificationRequest notificationRequest = new NotificationRequest(hospital.getH_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(notificationRequest);
    }

    private void getRoster() {

    }

    private void getDetactions() {

    }

    private void getLogs(){
        String h_id = hospital.getH_id();
        patient_logs = new ArrayList<>();
        patientlogadapter = new Adapter_log_listview(getActivity().getApplicationContext(),patient_logs);
        fragment_home_log_Listview.setAdapter(patientlogadapter);
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("Res",response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("log");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String seq = item.getString("seq");
                        String u_id = item.getString("u_id");
                        String p_id = item.getString("p_id");
                        String p_name = item.getString("p_name");
                        String u_name = item.getString("u_name");
                        String pl_contents = item.getString("pl_contents");
                        String pl_time = item.getString("pl_time");
                        patient_logs.add(new Patient_Log(seq, p_id,p_name, u_id,u_name, pl_contents, pl_time));
                    }
                    patientlogadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientLogRequest_hid patientLogRequest_hid = new PatientLogRequest_hid(h_id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(patientLogRequest_hid);
    }


    private void initWeeklyRoster(View view) {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int startDay = getStartdayofWeek();

        TextView fragment_home_year_month = (TextView) view.findViewById(R.id.fragment_home_year_month);
        fragment_home_year_month.setText(Integer.toString(year) + "-" + Integer.toString(month));

        TextView fragment_home_date_0 = (TextView) view.findViewById(R.id.fragment_home_date_0);
        fragment_home_date_0.setText(Integer.toString(startDay));
        TextView fragment_home_date_1 = (TextView) view.findViewById(R.id.fragment_home_date_1);
        fragment_home_date_1.setText(Integer.toString(startDay+1));
        TextView fragment_home_date_2 = (TextView) view.findViewById(R.id.fragment_home_date_2);
        fragment_home_date_2.setText(Integer.toString(startDay+2));
        TextView fragment_home_date_3 = (TextView) view.findViewById(R.id.fragment_home_date_3);
        fragment_home_date_3.setText(Integer.toString(startDay+3));
        TextView fragment_home_date_4 = (TextView) view.findViewById(R.id.fragment_home_date_4);
        fragment_home_date_4.setText(Integer.toString(startDay+4));
        TextView fragment_home_date_5 = (TextView) view.findViewById(R.id.fragment_home_date_5);
        fragment_home_date_5.setText(Integer.toString(startDay+5));
        TextView fragment_home_date_6 = (TextView) view.findViewById(R.id.fragment_home_date_6);
        fragment_home_date_6.setText(Integer.toString(startDay+6));
    }
    private int getStartdayofWeek(){
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int weekofyear = currentDate.get(Calendar.WEEK_OF_YEAR);
        currentDate.clear();
        currentDate.set(Calendar.WEEK_OF_YEAR, weekofyear);
        currentDate.set(Calendar.YEAR, year);
        Date startDate = currentDate.getTime();
        return Integer.parseInt(new SimpleDateFormat("dd").format(startDate));
    }
}
