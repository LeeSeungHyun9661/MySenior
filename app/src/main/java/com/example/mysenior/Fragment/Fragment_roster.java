package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.mysenior.Activity.Activity_Worker_Add;
import com.example.mysenior.Activity.Activity_Worker_Detail;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class Fragment_roster extends Fragment {
    User user;
    Hospital hospital;
    MaterialCalendarView roster_calendarview;

    public Fragment_roster(User user, Hospital hospital) {
        this.hospital = hospital;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roster, container, false);

//        roster_calendarview = (MaterialCalendarView)view.findViewById(R.id.roster_calendarview);

        return view;
    }
}
