package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.Authority;
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Fragment.Fragment_patient;
import com.example.mysenior.R;

import java.util.ArrayList;

public class Adapter_patient_gridview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Patient> patientArrayList;

    public Adapter_patient_gridview(Context context, ArrayList<Patient> patientArrayList) {
        this.patientArrayList = patientArrayList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return patientArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return patientArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.item_patient_list, null);

        ImageView item_patient_image = (ImageView)view.findViewById(R.id.item_patient_image);
        TextView item_patient_name = (TextView)view.findViewById(R.id.item_patient_name);
        TextView item_patient_gender = (TextView)view.findViewById(R.id.item_patient_gender);
        TextView item_patient_age = (TextView)view.findViewById(R.id.item_patient_age);
        TextView item_patient_ward = (TextView)view.findViewById(R.id.item_patient_ward);
        TextView item_patient_admin = (TextView)view.findViewById(R.id.item_patient_admin);

        item_patient_image.setImageResource(getImageResorce(patientArrayList.get(position).getP_image()));
        item_patient_name.setText(patientArrayList.get(position).getP_name());
        item_patient_gender.setText(patientArrayList.get(position).getP_gender());
        item_patient_age.setText(Integer.toString(patientArrayList.get(position).getP_age()));
        item_patient_ward.setText(patientArrayList.get(position).getP_ward());
        item_patient_admin.setText(patientArrayList.get(position).getP_admin());

        return view;
    }
    public int getImageResorce(String url){
        return 0;
    }
}
