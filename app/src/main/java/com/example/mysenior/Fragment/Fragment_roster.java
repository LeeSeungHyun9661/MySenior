package com.example.mysenior.Fragment;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.YEAR;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
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
import com.example.mysenior.Adapter.Adapter_worker_gridview;
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

public class Fragment_roster extends Fragment {
    User user;
    Hospital hospital;
    CalendarView roster_calendarview;
    TextView roster_date;
    List<EventDay> events;
    Calendar calendar;
    private ArrayList<User> userArrayList;
    private ListView fragment_roster_listview;
    private Adapter_roster_worker_listview roster_worker_adapter;

    public Fragment_roster(User user, Hospital hospital) {
        this.hospital = hospital;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roster, container, false);

        roster_date = (TextView) view.findViewById(R.id.roster_date);
        roster_calendarview = (CalendarView) view.findViewById(R.id.roster_calendarview);
        fragment_roster_listview = (ListView) view.findViewById(R.id.fragment_roster_listview);

        calendar = Calendar.getInstance();
        roster_calendarview.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedCalendar = eventDay.getCalendar();
                int year = clickedCalendar.get(Calendar.YEAR);
                int month = clickedCalendar.get(Calendar.MONTH)+1;
                int day = clickedCalendar.get(Calendar.DATE);
                String ClickedDate = Integer.toString(year) + "년 " + Integer.toString(month) + "월 " + Integer.toString(day) + "일";
                roster_date.setText(ClickedDate);
                getWorkerRoster(year, month, day);
            }
        });

        roster_calendarview.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                getMonthlyRoster();
            }
        });
        roster_calendarview.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                getMonthlyRoster();
            }
        });
        return view;
    }

    private void getMonthlyRoster() {
        //이벤트 리스트 초기화
        events = new ArrayList<>();
        //지정한 년월에 대해 첫째 날로 초기화
        calendar.set(Calendar.DATE, calendar.getMinimum(DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("roster");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Calendar res_cal = Calendar.getInstance();
                        res_cal.set(year, month - 1, day);

                        res_cal.add(Calendar.DAY_OF_MONTH, i);
                        String res_year = Integer.toString(res_cal.get(Calendar.YEAR));
                        String res_month = Integer.toString(res_cal.get(Calendar.MONTH) + 1);
                        String res_day = Integer.toString(res_cal.get(Calendar.DATE));

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
                    roster_calendarview.setEvents(events);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String startday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(calendar.getMinimum(DAY_OF_MONTH));
        String endday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(calendar.getMaximum(DAY_OF_MONTH));
        RosterMonthlyRequest monthlyRosterRequest = new RosterMonthlyRequest(Global.getInstance().getHospital().getH_id(), Global.getInstance().getUser().getU_id(), startday, endday, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(monthlyRosterRequest);
    }

    private void getWorkerRoster(int year, int month, int day) {
        userArrayList = new ArrayList<>();
        roster_worker_adapter = new Adapter_roster_worker_listview(getActivity(), userArrayList);
        fragment_roster_listview.setAdapter(roster_worker_adapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
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
                        if(!r_type.equals("")){
                            User wokrer = new User(a_id, u_id, u_name, h_id, position, department, u_image, isadmin);
                            wokrer.setRoster(r_type);
                            userArrayList.add(wokrer);
                        }
                    }
                    roster_worker_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String searchday = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        Log.w("RESPONSE",searchday);
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
