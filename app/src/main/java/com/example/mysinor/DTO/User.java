package com.example.mysinor.DTO;

public class User {

    String u_id; //사용자ID
    String u_name; //사용자 이름
    String h_id; //사용자 현재 접근중인 병원 ID
    String position; //사용자 직책
    String department; //사용자 소속
    boolean isAdmin; //사용자가 관리자인지 아닌지

    //기본적인 사용자 클래스 생성자
    public User(String u_id,String u_name) {
        this.u_id = u_id;
        this.u_name = u_name;
    }

    //병원 아이디로 접근하고 나서 직책과 소속, 병원 ID, 관리자인지 설정할때 쓸 함수
    public void setHospitalAccess(String h_id,String position, String department, boolean isAdmin){
        this.department = department;
        this.h_id = h_id;
        this.position = position;
        this.isAdmin = isAdmin;
    }
    
    //기본적인 게터세터 목록
    public String getH_id() {
        return h_id;
    }
    public void setH_id(String h_id) {this.h_id = h_id;}

    public String getPosition() {return position;}
    public void setPosition(String position) {this.position = position;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public boolean isAdmin() {return isAdmin;}
    public void setAdmin(boolean admin) {isAdmin = admin;}

    public String getU_id() {return u_id;}
    public void setU_id(String u_id) {this.u_id = u_id;}

    public String getU_name() {return u_name;}
    public void setU_name(String u_name) {this.u_name = u_name;}
}