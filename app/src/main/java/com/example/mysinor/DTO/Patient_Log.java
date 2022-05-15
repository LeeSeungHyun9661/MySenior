package com.example.mysinor.DTO;

import java.util.Date;

public class Patient_Log {
    String p_id; //해당 로그 환자 ID
    String u_id; //작성자의 ID
    String pl_contents; //로그 내용
    Date pl_time; //로그 작성된 시간

    //생성자
    public Patient_Log(String p_id, String u_id, String pl_contents, Date pl_time) {
        this.p_id = p_id;
        this.u_id = u_id;
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

    public Date getPl_time() {
        return pl_time;
    }
}
