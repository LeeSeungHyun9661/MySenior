package com.example.mysenior.DTO;

public class Authority {

    String a_id; //신청서 아이디
    String u_id; //사용자 아이디 - 신청자 아이디
    String h_id; //병원 아이디 - 신청서 대상 병원 아이디
    String h_name;
    String h_image;
    String position; // 직책
    String department; //소솓
    int isAdmin; //관리자로 신청했는지 여부
    int ischeck; //관리자로 신청했는지 여부

    //기본적인 병원 권한 신청서 생성자
    public Authority(String a_id, String u_id, String h_id,String h_name,String h_image, String position, String department, int ischeck, int isAdmin) {
        this.a_id = a_id;
        this.u_id = u_id;
        this.h_id = h_id;
        this.h_name = h_name;
        this.h_image = h_image;
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

    public String getH_name() {
        return h_name;
    }

    public String getH_image() { return h_image;}

    @Override
    public String toString() {
        return "Authority{" +
                "a_id='" + a_id + '\'' +
                ", u_id='" + u_id + '\'' +
                ", h_id='" + h_id + '\'' +
                ", h_name='" + h_name + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", isAdmin=" + isAdmin +
                ", ischeck=" + ischeck +
                '}';
    }

    public boolean isCheck() {
        return (ischeck != 0);
    }
}
