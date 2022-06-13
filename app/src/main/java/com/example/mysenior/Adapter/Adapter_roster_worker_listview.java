package com.example.mysenior.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;

import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
어댑터 클래스

이름 : Adapter_roster_worker_listview
역할 : 달력 선택에 따라 해당 날짜에 근무표가 설정된 직원의 목록을 표시하기 위한 리스트뷰 어댑터
 */
public class Adapter_roster_worker_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<User> workerArrayList;

    public Adapter_roster_worker_listview(Context context, ArrayList<User> workerArrayList) {
        this.workerArrayList = workerArrayList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return workerArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return workerArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.item_roster_worker_list, null);

        ImageView item_roster_worker_image = (ImageView)view.findViewById(R.id.item_roster_worker_image);
        TextView item_roster_worker_name = (TextView)view.findViewById(R.id.item_roster_worker_name);
        TextView item_roster_worker_department = (TextView)view.findViewById(R.id.item_roster_worker_department);
        TextView item_roster_worker_position = (TextView)view.findViewById(R.id.item_roster_worker_position);
        ImageView item_roster_worker_roster = (ImageView)view.findViewById(R.id.item_roster_worker_roster);

        if (workerArrayList.get(position).getU_image().equals("")) item_roster_worker_image.setImageResource(R.drawable.default_worker_64);
        else item_roster_worker_image.setImageBitmap(workerArrayList.get(position).getU_imageBitmap());

        item_roster_worker_name.setText(workerArrayList.get(position).getU_name());
        item_roster_worker_position.setText(workerArrayList.get(position).getPosition());
        item_roster_worker_department.setText(workerArrayList.get(position).getDepartment());

        switch (workerArrayList.get(position).getRoster()) {
            case "D":
                item_roster_worker_roster.setImageResource(R.drawable.roster_d);
                break;
            case "O":
                item_roster_worker_roster.setImageResource(R.drawable.roster_o);
                break;
            case "E":
                item_roster_worker_roster.setImageResource(R.drawable.roster_e);
                break;
            case "N":
                item_roster_worker_roster.setImageResource(R.drawable.roster_n);
                break;
            default:
                item_roster_worker_roster.setImageResource(R.drawable.roster_x);
                break;
        }
        return view;
    }
}
