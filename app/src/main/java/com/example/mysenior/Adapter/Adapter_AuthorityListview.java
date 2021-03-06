package com.example.mysenior.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.Activity.Activity_Authority_List;
import com.example.mysenior.Activity.Activity_Hospital;
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

이름 : Adapter_AuthorityListview
역할 :병원에 대한 사용자의 권한 리스트뷰를 처리할 어댑터
 */
public class Adapter_AuthorityListview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Authority> authorityArrayList;
    ArrayList<Hospital> hospitalArrayList;

    public Adapter_AuthorityListview(Context context, ArrayList<Authority> authorityArrayList, ArrayList<Hospital> hospitalArrayList) {
        this.authorityArrayList = authorityArrayList;
        this.hospitalArrayList = hospitalArrayList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
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
        View view = layoutInflater.inflate(R.layout.item_authority_list, null);

        ImageView item_authority_list_h_image = (ImageView)view.findViewById(R.id.item_authority_list_h_image);
        TextView item_authority_list_h_name = (TextView)view.findViewById(R.id.item_authority_list_h_name);
        TextView item_authority_list_department = (TextView)view.findViewById(R.id.item_authority_list_department);
        TextView item_authority_list_position = (TextView)view.findViewById(R.id.item_authority_list_position);

        item_authority_list_h_image.setImageResource(getImageResorce(hospitalArrayList.get(position).getH_image()));
        if (hospitalArrayList.get(position).getH_image().equals("")) item_authority_list_h_image.setImageResource(R.drawable.no_image);
        else item_authority_list_h_image.setImageBitmap(hospitalArrayList.get(position).getH_imageBitmap());

        item_authority_list_h_name.setText(hospitalArrayList.get(position).getH_name());
        item_authority_list_department.setText(authorityArrayList.get(position).getDepartment());
        item_authority_list_position.setText(authorityArrayList.get(position).getPosition());

        //병원 권한에 따라 다르게 표현하는 부분
        if(authorityArrayList.get(position).isCheck()){
        }else{
            item_authority_list_h_name.setBackgroundResource(R.drawable.border_disable);
            item_authority_list_department.setBackgroundResource(R.drawable.border_disable);
            item_authority_list_position.setBackgroundResource(R.drawable.border_disable);
            view.setBackgroundResource(R.drawable.disable);
        }
        return view;
    }

    public int getImageResorce(String url){
        return 0;
    }
}
