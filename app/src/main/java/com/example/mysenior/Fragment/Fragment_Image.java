package com.example.mysenior.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mysenior.R;
/*
MySenior
작성일자 : 2022-06-14
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
프래그먼트 클래스

이름 :  Fragment_Image
역할 : 이미지 뷰페이저의 각 이미지를 나타내기 위한 프래그먼트
기능 : 이미지값을 비트맵으로 받아 해당 이미지를 표시
특이사항 :
    - 이미지 클릭시 해당 프레그먼트를 비활성화 하기 위한 기능이 추가되어야함
 */
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