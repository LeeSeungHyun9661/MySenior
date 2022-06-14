package com.example.mysenior.Fragment;

import static java.util.Calendar.DAY_OF_MONTH;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.mysenior.Adapter.Adapter_roster_worker_listview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.RosterMonthlyRequest;
import com.example.mysenior.Request.RosterWorkerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
MySenior
작성일자 : 2022-06-14
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
프래그먼트 클래스

이름 :  Fragment_roster
역할 : 월간 사용자의 근무표를 확인하는 기능
기능 :
    1) 근무표 새롭게 업로드(미구현)
    2) 사용자의 월간 근무표를 불러와 달력에 추가
    3) 날짜 선택으로 해당 날짜의 근무자들을 보여주는 기능
특이사항 :
    - 병원 근무자에 대한 근무표를 간편하게 올릴 수 있도록 PC 프로그램을 제작해 앱과 연동할 예정
    - 일간 근무자의 목록을 근무 시간별로 드랍다운을 통해 볼 수 있도록 기능을 추가할 예정
 */
public class Fragment_roster extends Fragment {
    private User user;
    private Hospital hospital;
    private CalendarView calendarView;
    private TextView textview_date;
    private List<EventDay> events;
    private Calendar calendar;
    private ArrayList<User> users;
    private ListView listview_roster;
    private Adapter_roster_worker_listview adapter_worker_listview;
    private ScrollView roster_scrollview;

    public Fragment_roster(User user, Hospital hospital) {
        this.hospital = hospital;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roster, container, false);
        
        setUI(view);

        listview_roster.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                roster_scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        
        //현재 날짜를 받아와 캘린더를 초기화함
        calendar = Calendar.getInstance();
        
        //캘린더의 각 날짜 선택시 해당 날짜의 근무자 리스트뷰를 보여주는 기능
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedCalendar = eventDay.getCalendar();
                int year = clickedCalendar.get(Calendar.YEAR);
                int month = clickedCalendar.get(Calendar.MONTH)+1;
                int day = clickedCalendar.get(Calendar.DATE);
                String ClickedDate = Integer.toString(year) + "년 " + Integer.toString(month) + "월 " + Integer.toString(day) + "일";
                textview_date.setText(ClickedDate);
                getWorkerRoster(year, month, day);
            }
        });

        //캘린더 뷰를 앞, 뒤로 이동할 때 이전 월, 이후 월에 대해 데이터를 불러와 표시하는 기능
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                getMonthlyRoster();
            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                getMonthlyRoster();
            }
        });
        return view;
    }

    private void setUI(View view) {
        roster_scrollview = (ScrollView) view.findViewById(R.id.roster_scrollview);
        textview_date = (TextView) view.findViewById(R.id.roster_date);
        calendarView = (CalendarView) view.findViewById(R.id.roster_calendarview);
        listview_roster = (ListView) view.findViewById(R.id.fragment_roster_listview);
    }

    //월간 근무표를 불러와 달력에 표시하는 기능
    private void getMonthlyRoster() {
        //이벤트 리스트 초기화
        events = new ArrayList<>();
        
        //지정한 년월에 대해 첫째 날로 달력을 초기화
        calendar.set(Calendar.DATE, calendar.getMinimum(DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        //서버에 월간 근무표를 요청
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("roster");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        //새로운 달력을 불러와 현재 날짜로 맞춤
                        Calendar res_cal = Calendar.getInstance();
                        res_cal.set(year, month - 1, day);

                        //달력을 첫 일자부터 날짜를 이동시킴 -> 1일 단위로 이동
                        res_cal.add(Calendar.DAY_OF_MONTH, i);
                        
                        //서버에서 받아온 근무 타입을 불러와 유형에 따라 새로운 이벤트 클래스에 추가함
                        JSONObject item = jsonArray.getJSONObject(i);
                        String r_type = item.getString("r_type");
                        switch (r_type) {
                            case "D":
                                events.add(new EventDay(res_cal, R.drawable.roster_d));
                                break;
                            case "O":
                                events.add(new EventDay(res_cal, R.drawable.roster_o));
                                break;
                            case "E":
                                events.add(new EventDay(res_cal, R.drawable.roster_e));
                                break;
                            case "N":
                                events.add(new EventDay(res_cal, R.drawable.roster_n));
                                break;
                            default:
                                events.add(new EventDay(res_cal, R.drawable.roster_x));
                                break;
                        }
                    }
                    //완성된 이벤트 목록을 캘린더에 적용
                    calendarView.setEvents(events);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //월간 데이터를 받기위해 검색 시작과 끝 날짜를 문자열로 만들고 서버에 요청함
        String startday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(calendar.getMinimum(DAY_OF_MONTH));
        String endday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(calendar.getMaximum(DAY_OF_MONTH));
        RosterMonthlyRequest monthlyRosterRequest = new RosterMonthlyRequest(Global.getInstance().getHospital().getH_id(), Global.getInstance().getUser().getU_id(), startday, endday, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(monthlyRosterRequest);
    }

    //선택 근무일자에 근무표를 보유한 직원과 근무 유형을 불러와 배치함
    private void getWorkerRoster(int year, int month, int day) {
        users = new ArrayList<>();
        adapter_worker_listview = new Adapter_roster_worker_listview(getActivity(), users);
        listview_roster.setAdapter(adapter_worker_listview);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.w("RESPONDE",response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("worker");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String u_image = item.getString("u_image");
                        String h_id = item.getString("h_id");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int isadmin = item.getInt("isadmin");
                        String r_type = item.getString("r_type");

                        User worker = new User(a_id, u_id, u_name, h_id, position, department, u_image, isadmin);
                        worker.setRoster(r_type);
                        Log.w("NAME",worker.getU_name());
                        Log.w("ROSTER",worker.getRoster());

                        if(!(worker.getRoster().equals("") | worker.getRoster().equals("O"))){
                            Log.w("RESPONSE","Worker added");
                            users.add(worker);
                        }
                    }
                    adapter_worker_listview.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //검색 일자를 문자열로 지정
        String searchday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        RosterWorkerRequest rosterWorkerRequest = new RosterWorkerRequest(Global.getInstance().getHospital().getH_id(), searchday, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(rosterWorkerRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMonthlyRoster();
    }
}
