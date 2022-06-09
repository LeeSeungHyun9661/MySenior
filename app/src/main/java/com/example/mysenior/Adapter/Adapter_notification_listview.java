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
