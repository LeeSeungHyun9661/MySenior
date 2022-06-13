package com.example.mysenior.DTO;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
데이터 테이블 오브젝트 클래스

이름 : Monitor
역할 : 병원이 가진 모니터 데이터
 */
public class Monitor {
    String m_id; //모니터 ID
    String h_id; //병원 ID
    String m_name; //설정된 모니터 이름
    String m_ip; //모니터 접근용 아이피

    //기본적인 모니터 생성자
    public Monitor(String m_id, String h_id, String m_name, String m_ip) {
        this.m_id = m_id;
        this.h_id = h_id;
        this.m_name = m_name;
        this.m_ip = m_ip;
    }

    //ip랑 비밀번호를 확인해서 모니터를 라이브로 반환하도록 해주는 함수
    public void getMonitorLive(String pw){
        if (checkMonitorPW(pw)) {//먼저 모니터의 pw를 DB에 있는 m_pw랑 비교
            // 일치할 경우
            accessMonitorLive(m_ip);
        }
        //암호가 틀린 경우 암호 틀린걸 반환
    }

    private void accessMonitorLive(String m_ip) { //m_ip를 기반으로 접근, 영상 정보를 반환
    }


    private boolean checkMonitorPW(String pw) { //DB접근, 암호 비교, 결과 반환 - 로그인 기능과 비슷하게 가면 됨
        if (true){
            return true;
        }
        return false;
    }

    public String getM_id() {
        return m_id;
    }

    public String getH_id() {
        return h_id;
    }

    public String getM_name() {
        return m_name;
    }

    public String getM_ip() {
        return m_ip;
    }
}
