package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Authority;
import com.example.mysenior.R;

import java.util.ArrayList;

public class Adapter_authority_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Authority> authorityArrayList;

    public Adapter_authority_listview(Context context, ArrayList<Authority> authorityArrayList) {
        this.authorityArrayList = authorityArrayList;
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
        Button item_authority_list_enter = (Button) view.findViewById(R.id.item_authority_list_enter);
        Button item_authority_list_delete = (Button) view.findViewById(R.id.item_authority_list_delete);
        CheckBox item_authority_list_auto_enter = (CheckBox) view.findViewById(R.id.item_authority_list_auto_enter);

        item_authority_list_h_image.setImageResource(authorityArrayList.get(position).getImageResorce());
        item_authority_list_h_name.setText(authorityArrayList.get(position).getH_name());
        item_authority_list_department.setText(authorityArrayList.get(position).getDepartment());
        item_authority_list_position.setText(authorityArrayList.get(position).getPosition());

        return view;
    }
}
