package com.example.mysenior.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Notification;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.R;

import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
어댑터 클래스

이름 : Adapter_notification_listview
역할 : 공지사항의 목록을 표현하기 위한 리스트뷰 애댑터
 */
public class Adapter_notification_listview extends BaseAdapter {
    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Notification> arrayList;

    public Adapter_notification_listview(Context context, ArrayList<Notification> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.item_notification_item, null);

        ImageView item_notification_image = (ImageView) view.findViewById(R.id.item_notification_image);
        TextView item_notification_title = (TextView) view.findViewById(R.id.item_notification_title);
        TextView item_notification_time = (TextView) view.findViewById(R.id.item_notification_time);
        TextView item_notification_contents = (TextView) view.findViewById(R.id.item_notification_contents);

        item_notification_image.setImageBitmap(arrayList.get(position).getImageBitmap(context).get(0));

        item_notification_title.setText(arrayList.get(position).getTitle());
        item_notification_time.setText(arrayList.get(position).getDate());
        item_notification_contents.setText(arrayList.get(position).getContents());
        return view;
    }
}
