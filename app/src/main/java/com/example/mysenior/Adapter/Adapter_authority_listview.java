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

public class Adapter_authority_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Authority> authorityArrayList;
    ArrayList<Hospital> hospitalArrayList;

    public Adapter_authority_listview(Context context, ArrayList<Authority> authorityArrayList,ArrayList<Hospital> hospitalArrayList) {
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
        if (hospitalArrayList.get(position).getH_image().equals("null")) item_authority_list_h_image.setImageResource(R.drawable.no_image);
        else item_authority_list_h_image.setImageBitmap(hospitalArrayList.get(position).getH_imageBitmap());

        item_authority_list_h_name.setText(hospitalArrayList.get(position).getH_name());
        item_authority_list_department.setText(authorityArrayList.get(position).getDepartment());
        item_authority_list_position.setText(authorityArrayList.get(position).getPosition());

        if(authorityArrayList.get(position).isCheck()){
        }else{
            item_authority_list_h_name.setBackgroundResource(R.drawable.border_disable);
            item_authority_list_department.setBackgroundResource(R.drawable.border_disable);
            item_authority_list_position.setBackgroundResource(R.drawable.border_disable);
            view.setBackgroundResource(R.drawable.roundborder_disable);
        }
        return view;
    }

    public int getImageResorce(String url){
        return 0;
    }
}
