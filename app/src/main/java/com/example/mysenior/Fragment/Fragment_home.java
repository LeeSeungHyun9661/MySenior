package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Notification_List;
import com.example.mysenior.Activity.Activity_Notification_Detail;
import com.example.mysenior.Activity.Activity_Patient_Detail;
import com.example.mysenior.Adapter.Adapter_LogListview;
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
import com.example.mysenior.Request.PatientLogRequest_hid;
import com.example.mysenior.Request.PatientRequest_p_id;
import com.example.mysenior.Request.RosterWeeklyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
/*
MySenior
작성일자 : 2022-06-14
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
프래그먼트 클래스

이름 :  Fragment_home
역할 : 병원 전반의 공지, 주간일정, 최근감지, 최근 환자 메모등을 볼수 있는 프래그먼트
기능 :
    1) 병원 공지사항 중 최근 공지 사항을 표시하고, 공지사항에 대한 목록으로 안내
    2) 사용자의 주간 일정을 확인하고 일주일 근무표를 표시
    3) 최근 위험 감지 목록을 확인
    4) 최근 새롭게 추가된 환자 기록을 보여주고 환자 액티비티로 안내
특이사항 :
    - 병원 공지사항의 경우 읽음 여부에 따라 계속 보여질건지를 확인하고 반영할 예정
    - 병원 모니터와 이에 따른 감지 기록은 미구현(이후 추가 구현할 예정임)
 */
public class Fragment_home extends Fragment {
    private Hospital hospital;
    private User user;
    private Button button_notificationMore;
    private ListView listview_notification, listview_detection, listview_log;
    private Adapter_notification_listview adapter_notificationListview;
    private Adapter_LogListview adapter_logListview;
    private ArrayList<Notification> notifications;
    private ArrayList<Patient_Log> logs;
    private ArrayList<String> weeklyRoster;
    private ArrayList<Monitor_Detaction> detactionArrayList; //추가 구현 예정임
    private ArrayList<TextView> dates;
    private ArrayList<ImageView> rosters;
    private TextView textview_yearmonth;

    //프래그먼트 기본 생성시 병원과 사용자 데이터를 받아옴
    public Fragment_home(User user, Hospital hospital){
        this.user = user;
        this.hospital = hospital;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setUI(view);

        //공지사항 클릭시 해당 내용을 볼 수 있는 페이지로 이동
        listview_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Notification_Detail.class);
                intent.putExtra("notification", notifications.get(i));
                startActivity(intent);
            }
        });

        //공지사항이 비어있는 경우
        listview_notification.setEmptyView((TextView)view.findViewById(R.id.fragment_home_notification_noitem));
        
        //병원의 모든 공지사항을 볼 수 있도록 이동하는 버튼 기능
        button_notificationMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Notification_List.class);
                startActivity(intent);
            }
        });

        //환자 기록 목록을 통해 환자 데이터베이스에 접근할 수 있는 기능
        listview_log.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p_id  = logs.get(position).getP_id();
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

        //사용자의 주간 근무표를 불러오는 기능
        initWeeklyRoster(view);
        return view;
    }

    private void setUI(View view) {
        textview_yearmonth = (TextView) view.findViewById(R.id.fragment_home_year_month);
        listview_notification = (ListView) view.findViewById(R.id.fragment_home_notification_Listview);
        button_notificationMore = (Button) view.findViewById(R.id.fragment_home_notification_more);
        listview_detection = (ListView) view.findViewById(R.id.fragment_home_detection_Listview);
        listview_log = (ListView) view.findViewById(R.id.fragment_home_log_Listview);
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifications();
        getDetactions();
        getLogs();
    }

    //병원의 공지사항 리스트를 서버에 요청
    private void getNotifications() {
        notifications = new ArrayList<>();
        adapter_notificationListview = new Adapter_notification_listview(getActivity(), notifications);
        listview_notification.setAdapter(adapter_notificationListview);
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
                            adapter_notificationListview.notifyDataSetChanged();
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

    //년월일에 따른 주간 근무표 내용을 서버에 요청
    private void getWeeklyRoster(int year, int month, int startDay) {
        weeklyRoster = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("roster");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String r_type = item.getString("r_type");
                        weeklyRoster.add(r_type);
                    }
                    for (int i=0; i<7; i++){
                        dates.get(i).setText(Integer.toString(startDay+i));
                        switch (weeklyRoster.get(i)){
                            case "D":
                                rosters.get(i).setImageResource(R.drawable.roster_d);
                                break;
                            case "O":
                                rosters.get(i).setImageResource(R.drawable.roster_o);
                                break;
                            case "E":
                                rosters.get(i).setImageResource(R.drawable.roster_e);
                                break;
                            case "N":
                                rosters.get(i).setImageResource(R.drawable.roster_n);
                                break;
                            default:
                                rosters.get(i).setImageResource(R.drawable.roster_x);
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String startdayString = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(startDay);
        RosterWeeklyRequest rosterWeeklyRequest = new RosterWeeklyRequest(Global.getInstance().getHospital().getH_id(),Global.getInstance().getUser().getU_id(),startdayString, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(rosterWeeklyRequest);
    }

    //(구현예정) 최근 위험 감지 내용 호출
    private void getDetactions() {

    }

    //병원의 환자 기록을 불러오는 기능
    private void getLogs(){
        String h_id = hospital.getH_id();
        logs = new ArrayList<>();
        adapter_logListview = new Adapter_LogListview(getActivity().getApplicationContext(), logs);
        listview_log.setAdapter(adapter_logListview);
        Response.Listener<String> responseListener  = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                        logs.add(new Patient_Log(seq, p_id,p_name, u_id,u_name, pl_contents, pl_time));
                    }
                    adapter_logListview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PatientLogRequest_hid patientLogRequest_hid = new PatientLogRequest_hid(h_id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(patientLogRequest_hid);
    }

    //최근 일주일의 주간 근무표를 생성하기 위한 기능
    private void initWeeklyRoster(View view) {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);

        //현재 주차의 첫 번째 일자
        int startDay = startofWeek();

        //현재 년도와 월을 추가함
        textview_yearmonth.setText(Integer.toString(year) + "년 " + Integer.toString(month+1) + "월");

        //일자와 근무표를 레이아웃에서 찾아 배열로 지정
        dates = new ArrayList<>(Arrays.asList(
                (TextView) view.findViewById(R.id.fragment_home_date_0),
                (TextView) view.findViewById(R.id.fragment_home_date_1),
                (TextView) view.findViewById(R.id.fragment_home_date_2),
                (TextView) view.findViewById(R.id.fragment_home_date_3),
                (TextView) view.findViewById(R.id.fragment_home_date_4),
                (TextView) view.findViewById(R.id.fragment_home_date_5),
                (TextView) view.findViewById(R.id.fragment_home_date_6)
        ));
        rosters = new ArrayList<>(Arrays.asList(
                (ImageView) view.findViewById(R.id.fragment_home_roster_0),
                (ImageView) view.findViewById(R.id.fragment_home_roster_1),
                (ImageView) view.findViewById(R.id.fragment_home_roster_2),
                (ImageView) view.findViewById(R.id.fragment_home_roster_3),
                (ImageView) view.findViewById(R.id.fragment_home_roster_4),
                (ImageView) view.findViewById(R.id.fragment_home_roster_5),
                (ImageView) view.findViewById(R.id.fragment_home_roster_6)
        ));
        
        //주간 일정을 서버에서 불러와 배열에 추가하고, 레이아웃을 다시 표현함
        getWeeklyRoster(year,month,startDay);
    }

    //현재 주차를 찾아서 주차의 시작 일자 값을 구하기 위한 기능
    private int startofWeek(){
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
