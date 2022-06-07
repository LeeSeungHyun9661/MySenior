package com.example.mysenior;

import android.app.Application;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;

public class Global{
    private static Global instance = null;
    private User user;
    private Hospital hospital;

    public static synchronized Global getInstance(){
        if(null == instance){
            instance = new Global();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
    public User getUser() {
        return user;
    }
    public Hospital getHospital() {
        return hospital;
    }
}
