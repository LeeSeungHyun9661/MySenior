package com.example.mysenior.DTO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.mysenior.R;

import java.io.Serializable;
import java.util.ArrayList;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
데이터 테이블 오브젝트 클래스

이름 : Notification
역할 : 병원의 공지사항 데이터
 */
public class Notification implements Serializable {
    String seq, h_id, u_id,u_name, title, date, contents;
    ArrayList<String> images;

    public Notification(String seq, String h_id, String u_id, String u_name,String title, String date, String contents, ArrayList<String> images) {
        this.seq = seq;
        this.h_id = h_id;
        this.u_id = u_id;
        this.date = date;
        this.contents = contents;
        this.images = images;
        this.u_name = u_name;
        this.title = title;
    }

    public Notification() {
        images = new ArrayList<>();
        images.add("");
        images.add("");
        images.add("");
    }

    public String getU_name() {
        return u_name;
    }

    public String getTitle() {
        return title;
    }

    public String getSeq() {
        return seq;
    }

    public String getH_id() {
        return h_id;
    }

    public String getU_id() {
        return u_id;
    }

    public String getDate() {
        return date;
    }

    public String getContents() {
        return contents;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<Bitmap> getImageBitmap(Context current) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < images.size(); i++){
            try {
                if(!images.get(i).equals("")){
                    byte[] encodeByte = Base64.decode(images.get(i), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    bitmaps.add(bitmap);
                }else{
                    Bitmap bitmap = BitmapFactory.decodeResource(current.getResources(), R.drawable.no_image);
                    bitmaps.add(bitmap);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmaps;
    }

    public int getImageMax() {
        return images.size();
    }

    public int getImageCount() {
        int cnt = 0;
        for (int i = 0; i<images.size(); i++){
            if(!images.equals("")){
                cnt+= 1;
            }
        }
        return cnt;
    }

    public void addImage(String image){
        images.remove(images.size()-1);
        images.add(0,image);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
