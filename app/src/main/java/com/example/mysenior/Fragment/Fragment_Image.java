package com.example.mysenior.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mysenior.R;

public class Fragment_Image extends Fragment {
    private Bitmap image;
    private ImageView imageview;

    //이미지 프레임에 적용할 이미지를 불러옴
    public Fragment_Image(Bitmap image) {
        this.image = image;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_viewpager_frame, container, false);
        imageview = (ImageView) rootView.findViewById(R.id.item_notification_viewpater_image);
        imageview.setImageBitmap(image);
        return rootView;
    }
}