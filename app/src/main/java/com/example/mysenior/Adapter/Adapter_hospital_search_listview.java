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

public class Adapter_hospital_search_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Hospital> hospitalArrayList;
    User user;

    public Adapter_hospital_search_listview(Context context, ArrayList<Hospital> hospitalArrayList, User user) {
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

        item_authority_search_h_image.setImageResource(hospitalArrayList.get(position).getH_image());
        item_authority_search_h_name.setText(hospitalArrayList.get(position).getH_name());
        item_authority_search_h_category.setText(hospitalArrayList.get(position).getH_category());
        item_authority_search_location.setText(hospitalArrayList.get(position).getH_location());
        return view;
    }
    public int getImageResorce(String url){
        return 0;
    }
}