package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mysenior.DTO.Authority;
import com.example.mysenior.R;
import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
어댑터 클래스

이름 : Adapter_worker_authority_listview
역할 : 직원의 권한 신청을 확인하기 위한 신청서 목록의 리스트뷰 어댑터
 */
public class Adapter_worker_authority_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Authority> authorityArrayList;
    ArrayList<String> namelist;

    public Adapter_worker_authority_listview(Context context, ArrayList<Authority> authorityArrayList, ArrayList<String> namelist) {
        this.authorityArrayList = authorityArrayList;
        this.namelist = namelist;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return authorityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return authorityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_worker_authority, null);

        TextView item_worker_authority_name = (TextView) view.findViewById(R.id.item_worker_authority_name);
        TextView item_worker_authority_position = (TextView) view.findViewById(R.id.item_worker_authority_position);
        TextView item_worker_authority_department = (TextView) view.findViewById(R.id.item_worker_authority_department);
        TextView item_worker_authority_isAdmin = (TextView) view.findViewById(R.id.item_worker_authority_isAdmin);

        item_worker_authority_name.setText(namelist.get(position));
        item_worker_authority_position.setText(authorityArrayList.get(position).getDepartment());
        item_worker_authority_department.setText(authorityArrayList.get(position).getPosition());
        if(authorityArrayList.get(position).isAdmin() == 1){
            item_worker_authority_isAdmin.setText("관리자");
        }else{
            item_worker_authority_isAdmin.setText("일반 직원");
        }
        return view;
    }
}
