package com.example.mysenior.Adapter;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mysenior.Fragment.Fragment_Image;

import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
어댑터 클래스

이름 : Adapter_ImageViewpager
역할 : 공지 내용에 이미지를 표시하기 위한 뷰페이저 어댑터
 */
public class Adapter_ImageViewpager extends FragmentStateAdapter {

    public ArrayList<Bitmap> images;
    public int count;

    public Adapter_ImageViewpager(FragmentActivity fa, ArrayList<Bitmap> images) {
        super(fa);
        this.images = images;
        this.count = images.size();
    }
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        return new Fragment_Image(images.get(index));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public int getRealPosition(int position) { return position % count; }

    @Override
    public long getItemId(int position) {
        Log.w("Position",Integer.toString(position));
        return images.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return images.contains(itemId);
    }
}