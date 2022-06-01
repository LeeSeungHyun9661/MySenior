package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.Patient_Log;
import com.example.mysenior.R;

import java.util.ArrayList;

public class Adapter_log_listview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Patient_Log> patient_logs;

    public Adapter_log_listview(Context context, ArrayList<Patient_Log> patient_logs) {
        this.patient_logs = patient_logs;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return patient_logs.size();
    }

    @Override
    public Object getItem(int i) {
        return patient_logs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.item_patient_log, null);
        Patient_Log log = patient_logs.get(position);

        TextView item_log_name = (TextView)view.findViewById(R.id.item_log_name);
        TextView item_log_date = (TextView)view.findViewById(R.id.item_log_date);
        TextView item_log_content = (TextView)view.findViewById(R.id.item_log_content);

        item_log_name.setText(log.getU_name());
        item_log_date.setText(log.getPl_time());
        item_log_content.setText(log.getPl_contents());

        return view;
    }
}
