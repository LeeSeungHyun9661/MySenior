package com.example.mysenior.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Authority_List;
import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityRemoveRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
