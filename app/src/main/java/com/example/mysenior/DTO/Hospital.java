package com.example.mysenior.DTO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
import java.util.Date;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
데이터 테이블 오브젝트 클래스

이름 : Hospital
역할 : 병원 정보 데이터
 */
public class Hospital implements Serializable {
    
    String h_id; //병원 ID
    String h_name; //병원 이름
    String h_category; //병원 구분
    String h_location; //병원 위치
    String h_phone; //병원 전화번호
    String h_image; //병원 이미지
    Date h_date; //병원 개원일자

    //기본적인 생성자
    public Hospital(String h_id, String h_name, String h_category, String h_location, String h_phone, String h_image, Date h_date) {
        this.h_id = h_id;
        this.h_name = h_name;
        this.h_category = h_category;
        this.h_location = h_location;
        this.h_phone = h_phone;
        this.h_image = h_image;
        this.h_date = h_date;
    }

    public Hospital() {
    }

    //기본적인 게터 목록
    public String getH_id() {return h_id;}
    public String getH_name() {return h_name;}
    public String getH_category() {return h_category;}
    public String getH_location() {return h_location;}
    public String getH_phone() {return h_phone;}
    public String getH_image() { return h_image;}
    public void setH_image(String h_image) {
        this.h_image = h_image;
    }
    public Bitmap getH_imageBitmap() {
        try {
            byte[] encodeByte = Base64.decode(h_image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String getH_date() {return h_date.toString();}
}
