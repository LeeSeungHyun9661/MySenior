package com.example.mysenior.Fragment;

import androidx.fragment.app.Fragment;

import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;

public class Fragment_monitor extends Fragment {
    User user;
    Hospital hospital;
    public Fragment_monitor(User user, Hospital hospital){
        this.hospital = hospital;
        this.user = user;
    }
}
