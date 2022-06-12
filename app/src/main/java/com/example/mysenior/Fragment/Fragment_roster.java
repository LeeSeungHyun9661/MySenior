package com.example.mysenior.Fragment;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.YEAR;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.RosterMonthlyRequest;

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
    List<EventDay> events;
    Calendar calendar;
    int move;

    public Fragment_roster(User user, Hospital hospital) {
        this.hospital = hospital;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roster, container, false);
        move = 0;

        roster_calendarview = (CalendarView) view.findViewById(R.id.roster_calendarview);
        calendar = Calendar.getInstance();
//        getMonthlyRoster();
        roster_calendarview.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
                getMonthlyRoster();
            }
        });
        roster_calendarview.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
                getMonthlyRoster();
            }
        });
        return view;
    }

    private void getMonthlyRoster() {
        //이벤트 리스트 초기화
        events = new ArrayList<>();
        //지정한 년월에 대해 첫째 날로 초기화
        Log.w("ADMINISTRATOR","일자를 첫 일자로 변경");
        calendar.set(Calendar.DATE,calendar.getMinimum(DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        Log.w("ADMINISTRATOR : 변경 년",Integer.toString(calendar.get(Calendar.YEAR)));
        Log.w("ADMINISTRATOR : 변경 월",Integer.toString(calendar.get(Calendar.MONTH)));
        Log.w("ADMINISTRATOR : 변경 일",Integer.toString(calendar.get(Calendar.DATE)));

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("roster");
                    Log.w("ADMINISTRATOR : ","서버에서 받아온 데이터 배열");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Calendar res_cal = Calendar.getInstance();
                        res_cal.set(year,month-1,day);

                        res_cal.add(Calendar.DAY_OF_MONTH, i);
                        String res_year = Integer.toString(res_cal.get(Calendar.YEAR));
                        String res_month = Integer.toString(res_cal.get(Calendar.MONTH) + 1);
                        String res_day = Integer.toString(res_cal.get(Calendar.DATE));

                        JSONObject item = jsonArray.getJSONObject(i);
                        String r_type = item.getString("r_type");
                        Log.w("ADMINISTRATOR : 데이터 값 ", res_year+ "-" + res_month + "-" + res_day + ": " + r_type);
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

    @Override
    public void onResume() {
        super.onResume();
        getMonthlyRoster();
    }
}
