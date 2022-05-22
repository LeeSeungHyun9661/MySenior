package com.example.mysenior.DTO;

import java.util.Date;

public class Monitor_Detaction {
    String m_id; //모니터 ID
    String d_contents; //감지 기록
    String d_image; //감지된 이미지의 저장된 경로
    Date d_date; //감지시간

    public Monitor_Detaction(String m_id, String d_contents, String d_image, Date d_date) {
        this.m_id = m_id;
        this.d_contents = d_contents;
        this.d_image = d_image;
        this.d_date = d_date;
    }

    public String getM_id() {
        return m_id;
    }

    public String getD_contents() {
        return d_contents;
    }

    public String getD_image() {
        return d_image;
    }

    public Date getD_date() {
        return d_date;
    }
}
