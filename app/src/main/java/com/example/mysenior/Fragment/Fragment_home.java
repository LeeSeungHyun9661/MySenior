package com.example.mysenior.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mysenior.DTO.User;
import com.example.mysenior.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment_home extends Fragment {
    User user;
    TextView fragment_home_notification;
    Button fragment_home_notification_more;
    ListView fragment_home_detection_Listview, fragment_home_note_Listview;

    public Fragment_home(User user){
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fragment_home_notification = (TextView) view.findViewById(R.id.fragment_home_notification);
        fragment_home_notification_more = (Button) view.findViewById(R.id.fragment_home_notification_more);

        initWeeklyRoster(view);

        return view;
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
}
