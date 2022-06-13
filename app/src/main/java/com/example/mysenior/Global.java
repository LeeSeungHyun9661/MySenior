package com.example.mysenior;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
기능 지원 클래스

이름 : Global
역할 : 액티비티 전체에 사용할 사용자와 병원의 데이터를 저장하기 위한 글로벌 클래스
 */
public class Global{
    private static Global instance = null;
    private User user;
    private Hospital hospital;

    public static synchronized Global getInstance(){
        if(null == instance){
            instance = new Global();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
    public User getUser() {
        return user;
    }
    public Hospital getHospital() {
        return hospital;
    }
}
