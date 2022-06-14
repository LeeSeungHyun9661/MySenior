package com.example.mysenior.Fragment;

import androidx.fragment.app.Fragment;

import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
/*
MySenior
작성일자 : 2022-06-14
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
프래그먼트 클래스

이름 :  Fragment_monitor (미구현)
역할 : 병원에 연결된 카메라에 접근해 새롭게 연결하고, 위험 감지를 확인할 수 있도록 하는 프래그먼트
기능 :
    1) 새로 카메라에 연결하도록 접근을 신청하고 수락하는 기능
    2) 카메라 선택을 통해 해당 화면을 볼 수 있는 기능
    3) 카메라에 대한 권한을 제한하도록 지정
    4) 카메라에 따라 다른 감지 목록을 확인할 수 있도록 기능을 지정
특이사항 :
    - 현재 모니터를 통한 환자 객체 인식이 가능하도록 기능 구현중
 */
public class Fragment_monitor extends Fragment {
    private User user;
    private Hospital hospital;
    public Fragment_monitor(User user, Hospital hospital){
        this.hospital = hospital;
        this.user = user;
    }
}
