package com.example.mysenior.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Adapter.Adapter_notification_image_viewpager;
import com.example.mysenior.DTO.Notification;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.AuthorityAddRequest;
import com.example.mysenior.Request.NotificationAddRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class Activity_Notification_Write extends FragmentActivity {
    Notification notification;
    Button Notification_write_add_image;
    TextView Notification_write_done, Notification_write_cancle, Notification_write_writer;
    EditText Notification_write_title, Notification_write_contents;
    ViewPager2 Notification_write_viewpager;
    Adapter_notification_image_viewpager adapter_notification_imageViewpager;
    private CircleIndicator3 Notification_write_indicator;
    ArrayList<Bitmap> imageBitmaps;
    private static final int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_write);
        notification = new Notification();

        Notification_write_done = (TextView) findViewById(R.id.Notification_write_done);
        Notification_write_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_id = Global.getInstance().getUser().getU_id();
                String h_id = Global.getInstance().getHospital().getH_id();
                String title = Notification_write_title.getText().toString();
                if (title.equals("")) title = "제목 없음";
                String contents = Notification_write_contents.getText().toString();
                if (contents.equals("")) contents = "내용 없음";
                String image1 = "";
                String image2 = "";
                String image3 = "";
                if (!notification.getImages().get(0).equals(""))
                    image1 = notification.getImages().get(0);
                if (!notification.getImages().get(1).equals(""))
                    image2 = notification.getImages().get(1);
                if (!notification.getImages().get(2).equals(""))
                    image3 = notification.getImages().get(2);
                
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.w("RESPONSE", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { 
                                Toast.makeText(getApplicationContext(), "공지 작성 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "공지 작성 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                NotificationAddRequest notificationAddRequest = new NotificationAddRequest(h_id,u_id,title,contents,image1,image2,image3,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Notification_Write.this);
                queue.add(notificationAddRequest);
            }


        });
        Notification_write_cancle = (TextView) findViewById(R.id.Notification_write_cancle);
        Notification_write_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Notification_write_add_image = (Button) findViewById(R.id.Notification_write_add_image);
        Notification_write_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Notification_write_writer = (TextView) findViewById(R.id.Notification_write_writer);
        Notification_write_writer.setText(Global.getInstance().getUser().getU_name());

        Notification_write_title = (EditText) findViewById(R.id.Notification_write_title);
        Notification_write_contents = (EditText) findViewById(R.id.Notification_write_contents);

        Notification_write_viewpager = findViewById(R.id.Notification_write_viewpager);
        imageBitmaps = notification.getImageBitmap(this);
        adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(this, imageBitmaps);
        Notification_write_viewpager.setAdapter(adapter_notification_imageViewpager);

        Notification_write_indicator = findViewById(R.id.Notification_write_indicator);
        Notification_write_indicator.setViewPager(Notification_write_viewpager);
        Notification_write_indicator.createIndicators(notification.getImageMax(), 0);
        Notification_write_viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        Notification_write_viewpager.setCurrentItem(1000); //시작 지점
        Notification_write_viewpager.setOffscreenPageLimit(notification.getImageMax()); //최대 이미지 수
        Notification_write_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    Notification_write_viewpager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Notification_write_indicator.animatePageSelected(position % notification.getImageMax());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = resize(BitmapFactory.decodeStream(in));
                    notification.addImage(BitMapToString(img));
                    imageBitmaps = notification.getImageBitmap(this);
                    adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(this, imageBitmaps);
                    Notification_write_viewpager.setAdapter(adapter_notification_imageViewpager);
                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        return image;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}