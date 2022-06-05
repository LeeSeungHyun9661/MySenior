package com.example.mysenior.DTO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.example.mysenior.R;

import java.io.Serializable;
import java.util.Date;

public class Patient implements Serializable {
    String h_id; //소속 병원 ID
    String p_id; //환자 고유 ID
    String p_name; //환자 이름
    String p_gender; //환자 성별
    String p_ward; //환자 소속 병실
    String p_NOK; //환자 보호자
    String p_NOK_phone; //환자 보호자의 전화번호
    String p_admin; //환자 담당직원
    String p_addr; //환자 주소
    String p_qr; //환자 고유 QR코드
    int p_age; //환자 나이
    String p_image;
    String p_birth; //환자 생일

    //생성자
    public Patient(String h_id, String p_id, String p_name, String p_gender, String p_ward, String p_NOK, String p_NOK_phone, String p_admin, String p_addr,String p_image, String p_qr, int p_age, String p_birth) {
        this.h_id = h_id;
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_gender = p_gender;
        this.p_ward = p_ward;
        this.p_NOK = p_NOK;
        this.p_NOK_phone = p_NOK_phone;
        this.p_admin = p_admin;
        this.p_addr = p_addr;
        this.p_qr = p_qr;
        this.p_age = p_age;
        this.p_image = p_image;
        this.p_birth = p_birth;
    }

    //기본적인 게터 리스트
    public String getH_id() {
        return h_id;
    }

    public String getP_id() {
        return p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_gender() {
        return p_gender;
    }

    public String getP_ward() {
        return p_ward;
    }

    public String getP_NOK() {
        return p_NOK;
    }

    public String getP_NOK_phone() {
        return p_NOK_phone;
    }

    public String getP_admin() {
        return p_admin;
    }

    public String getP_addr() {
        return p_addr;
    }

    public String getP_qr() {
        return p_qr;
    }

    public int getP_age() {
        return p_age;
    }

    public String getP_birth() {
        return p_birth;
    }

    public String getP_image() {
        return p_image;
    }
    public Bitmap getP_imageBitmap() {
        try {
            byte[] encodeByte = Base64.decode(p_image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
