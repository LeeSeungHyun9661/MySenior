package com.example.mysenior;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
기능 지원 클래스

이름 : BitmapController
역할 : 이미지에 대해 비트맵 변환, base64 스트링 변환을 도와주는 클래스
 */
public class BitmapController {
    Bitmap btm;
    Configuration config;

    public BitmapController(InputStream in, Configuration config) {
        this.btm = BitmapFactory.decodeStream(in);
        this.config = config;
    }

    public Bitmap resize() {
        if (config.smallestScreenWidthDp >= 800)
            btm = Bitmap.createScaledBitmap(btm, 400, 240, true);
        else if (config.smallestScreenWidthDp >= 600)
            btm = Bitmap.createScaledBitmap(btm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            btm = Bitmap.createScaledBitmap(btm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            btm = Bitmap.createScaledBitmap(btm, 180, 108, true);
        else btm = Bitmap.createScaledBitmap(btm, 160, 96, true);
        return btm;
    }

    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        return image;
    }
}
