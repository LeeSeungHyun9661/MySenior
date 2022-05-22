package com.example.mysenior.DTO;

public class Application {

    String a_id; //신청서 아이디
    String u_id; //사용자 아이디 - 신청자 아이디
    String h_id; //병원 아이디 - 신청서 대상 병원 아이디
    String u_name; //신청자 이름
    String position; // 직책
    String department; //소솓
    String phone; // 전화번호
    boolean isAdmin; //관리자로 신청했는지 여부
    
    //기본적인 병원 권한 신청서 생성자
    public Application(String a_id, String u_id, String h_id, String u_name, String position, String department, String phone, boolean isAdmin) {
        this.a_id = a_id;
        this.u_id = u_id;
        this.h_id = h_id;
        this.u_name = u_name;
        this.position = position;
        this.department = department;
        this.phone = phone;
        this.isAdmin = isAdmin;
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

    public String getU_name() {
        return u_name;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
