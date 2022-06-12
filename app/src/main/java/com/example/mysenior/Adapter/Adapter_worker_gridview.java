package com.example.mysenior.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysenior.DTO.User;
import com.example.mysenior.R;

import java.util.ArrayList;

public class Adapter_worker_gridview extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<User> workerArrayList;

    public Adapter_worker_gridview(Context context, ArrayList<User> workerArrayList) {
        this.workerArrayList = workerArrayList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return workerArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return workerArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.item_worker_list, null);

        ImageView item_worker_image = (ImageView)view.findViewById(R.id.item_worker_image);
        TextView item_worker_name = (TextView)view.findViewById(R.id.item_worker_name);
        TextView item_worker_position = (TextView)view.findViewById(R.id.item_worker_position);

        if (workerArrayList.get(position).getU_image().equals("")) item_worker_image.setImageResource(R.drawable.default_worker_64);
        else item_worker_image.setImageBitmap(workerArrayList.get(position).getU_imageBitmap());

        item_worker_name.setText(workerArrayList.get(position).getU_name());
        item_worker_position.setText(workerArrayList.get(position).getPosition());

        return view;
    }
}
