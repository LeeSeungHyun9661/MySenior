package com.example.mysenior.DTO;

import java.util.Date;

public class Patient_Log {
    String seq;
    String p_id; //해당 로그 환자 ID
    String p_name; //해당 로그 환자 ID
    String u_id; //작성자의 ID
    String pl_contents; //로그 내용
    String pl_time; //로그 작성된 시간
    String u_name;

    //생성자
    public Patient_Log(String seq, String p_id, String p_name, String u_id, String u_name, String pl_contents, String pl_time) {
        this.seq = seq;
        this.p_id = p_id;
        this.p_name = p_name;
        this.u_id = u_id;
        this.u_name = u_name;
        this.pl_contents = pl_contents;
        this.pl_time = pl_time;
    }

    //게터 목록
    public String getP_id() {
        return p_id;
    }

    public String getU_id() {
        return u_id;
    }

    public String getPl_contents() {
        return pl_contents;
    }

    public String getPl_time() {
        return pl_time;
    }

    public String getU_name() {
        return u_name;
    }

    public String getSeq() {
        return seq;
    }

    public String getP_name() {
        return p_name;
    }
}
