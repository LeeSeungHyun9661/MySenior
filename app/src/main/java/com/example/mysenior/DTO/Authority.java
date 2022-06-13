package com.example.mysenior.DTO;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
데이터 테이블 오브젝트 클래스

이름 : Authority
역할 : 사용자가 병원에 가진 권한을 의미하는 신청서 데이터
 */
public class Authority {

    String a_id; //신청서 아이디
    String u_id; //사용자 아이디 - 신청자 아이디
    String h_id; //병원 아이디 - 신청서 대상 병원 아이디
    String position; // 직책
    String department; //소속
    int isAdmin; //관리자로 신청했는지 여부
    int ischeck; //병원의 승인을 받았는지 여부

    //기본적인 병원 권한 신청서 생성자
    public Authority(String a_id, String u_id, String h_id, String position, String department, int ischeck, int isAdmin) {
        this.a_id = a_id;
        this.u_id = u_id;
        this.h_id = h_id;
        this.position = position;
        this.department = department;
        this.isAdmin = isAdmin;
        this.ischeck = ischeck;
    }

    public String getA_id() {
        return a_id;
    }

    public String getU_id() {
        return u_id;
    }

    public String getH_id() {
        return h_id;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public int isAdmin() {
        return isAdmin;
    }

    public boolean isCheck() {
        return (ischeck != 0);
    }
}
