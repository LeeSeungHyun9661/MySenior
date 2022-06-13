package com.example.mysenior.DTO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
데이터 테이블 오브젝트 클래스

이름 : User
역할 : 사용자와 직원 정보를 위한 데이터베이스
 */
public class User implements Serializable {

    String a_id;
    String u_id; //사용자ID
    String u_name; //사용자 이름
    String position; //사용자 직책
    String department; //사용자 소속
    String u_image; //사용자 소속
    String r_type;
    int isAdmin; //사용자가 관리자인지 아닌지

    public User(){
    }

    public User(String a_id, String u_id, String u_name, String h_id, String position, String department, String u_image, int isAdmin) {
        this.a_id = a_id;
        this.u_id = u_id;
        this.u_name = u_name;
        this.position = position;
        this.department = department;
        this.u_image = u_image;
        this.isAdmin = isAdmin;
    }

    //기본적인 사용자 클래스 생성자
    public User(String u_id, String u_name, String u_image) {
        this.u_id = u_id;
        this.u_name = u_name;
        this.u_image = u_image;
    }

    //병원 아이디로 접근하고 나서 직책과 소속, 병원 ID, 관리자인지 설정할때 쓸 함수
    public void setHospitalAccess( String position, String department, int isAdmin) {
        this.department = department;
        this.position = position;
        this.isAdmin = isAdmin;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }


    public boolean getIsAdmin() {
        if (isAdmin == 1)  return true;
        else return false;
    }

    public String getU_image() {
        return u_image;
    }

    public Bitmap getU_imageBitmap() {
        try {
            byte[] encodeByte = Base64.decode(u_image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public void setRoster(String r_type){
        this.r_type = r_type;
    }

    public String getRoster(){
        return r_type;
    }
}