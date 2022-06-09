package com.example.mysenior.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mysenior.R;

public class Fragment_Frame extends Fragment {
    Bitmap image;
    ImageView item_notification_viewpater_image;

    public Fragment_Frame(Bitmap image) {
        this.image = image;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_viewpager_frame, container, false);
        item_notification_viewpater_image = (ImageView) rootView.findViewById(R.id.item_notification_viewpater_image);
        item_notification_viewpater_image.setImageBitmap(image);
        return rootView;
    }
}