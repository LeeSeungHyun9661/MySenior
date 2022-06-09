package com.example.mysenior.DTO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.mysenior.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Notification implements Serializable {
    String seq, h_id, u_id,u_name, title, date, contents;
    ArrayList<String> images;

    public Notification(String seq, String h_id, String u_id, String u_name,String title, String date, String contents, ArrayList<String> images) {
        this.seq = seq;
        this.h_id = h_id;
        this.u_id = u_id;
        this.date = date;
        this.contents = contents;
        this.images = images;
        this.u_name = u_name;
        this.title = title;
    }

    public Notification() {
        images = new ArrayList<>();
        images.add("null");
        images.add("null");
        images.add("null");
    }

    public String getU_name() {
        return u_name;
    }

    public String getTitle() {
        return title;
    }

    public String getSeq() {
        return seq;
    }

    public String getH_id() {
        return h_id;
    }

    public String getU_id() {
        return u_id;
    }

    public String getDate() {
        return date;
    }

    public String getContents() {
        return contents;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<Bitmap> getImageBitmap(Context current) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < images.size(); i++){
            try {
                if(!images.get(i).equals("null")){
                    byte[] encodeByte = Base64.decode(images.get(i), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    bitmaps.add(bitmap);
                }else{
                    Bitmap bitmap = BitmapFactory.decodeResource(current.getResources(), R.drawable.no_image);
                    bitmaps.add(bitmap);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmaps;
    }

    public int getImageMax() {
        return images.size();
    }
    public int getImageCount() {
        int cnt = 0;
        for (int i = 0; i<images.size(); i++){
            if(!images.equals("null")){
                cnt+= 1;
            }
        }
        return cnt;
    }

    public void addImage(String image){
        images.remove(images.size()-1);
        images.add(0,image);
    }
}
