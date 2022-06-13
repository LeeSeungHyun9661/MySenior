package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.Hospital;
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

이름 : Adapter_HospitalSearchListview
역할 : 검색어에 따른 병원 정보를 불러오는 리스트뷰 어댑터
 */
public class Adapter_HospitalSearchListview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Hospital> hospitalArrayList;
    User user;

    public Adapter_HospitalSearchListview(Context context, ArrayList<Hospital> hospitalArrayList, User user) {
        this.hospitalArrayList = hospitalArrayList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
        this.user = user;
    }

    @Override
    public int getCount() {
        return hospitalArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return hospitalArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_authority_search, null);

        ImageView item_authority_search_h_image = (ImageView)view.findViewById(R.id.item_authority_search_h_image);
        TextView item_authority_search_h_name = (TextView)view.findViewById(R.id.item_authority_search_h_name);
        TextView item_authority_search_h_category = (TextView)view.findViewById(R.id.item_authority_search_h_category);
        TextView item_authority_search_location = (TextView)view.findViewById(R.id.item_authority_search_location);

        if (hospitalArrayList.get(position).getH_image().equals("")) item_authority_search_h_image.setImageResource(R.drawable.no_image);
        else item_authority_search_h_image.setImageBitmap(hospitalArrayList.get(position).getH_imageBitmap());

        item_authority_search_h_name.setText(hospitalArrayList.get(position).getH_name());
        item_authority_search_h_category.setText(hospitalArrayList.get(position).getH_category());
        item_authority_search_location.setText(hospitalArrayList.get(position).getH_location());
        return view;
    }
    public int getImageResorce(String url){
        return 0;
    }
}
