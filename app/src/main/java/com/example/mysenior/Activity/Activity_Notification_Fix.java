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
import com.example.mysenior.DTO.Patient;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.NotificationFixRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class Activity_Notification_Fix extends FragmentActivity {

    Notification notification;
    Button Notification_fix_add_image;
    TextView Notification_fix_done, Notification_fix_cancle, Notification_fix_writer;
    EditText Notification_fix_title, Notification_fix_contents;
    ViewPager2 Notification_fix_viewpager;
    Adapter_notification_image_viewpager adapter_notification_imageViewpager;
    private CircleIndicator3 Notification_fix_indicator;
    ArrayList<Bitmap> imageBitmaps;
    private static final int REQUEST_CODE = 99;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_fix);

        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");

        Notification_fix_done = (TextView) findViewById(R.id.Notification_fix_done);
        Notification_fix_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = Notification_fix_title.getText().toString();
                if (title.equals("")) title = "제목 없음";
                String contents = Notification_fix_contents.getText().toString();
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

                notification.setTitle(title);
                notification.setContents(contents);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "공지 수정 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("notification",notification);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "공지 수정 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                NotificationFixRequest notificationFixRequest = new NotificationFixRequest(notification.getSeq(),title,contents,image1,image2,image3,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Activity_Notification_Fix.this);
                queue.add(notificationFixRequest);
            }


        });
        Notification_fix_cancle = (TextView) findViewById(R.id.Notification_fix_cancle);
        Notification_fix_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Notification_fix_add_image = (Button) findViewById(R.id.Notification_fix_add_image);
        Notification_fix_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Notification_fix_writer = (TextView) findViewById(R.id.Notification_fix_writer);
        Notification_fix_writer.setText(Global.getInstance().getUser().getU_name());

        Notification_fix_title = (EditText) findViewById(R.id.Notification_fix_title);
        Notification_fix_title.setText(notification.getTitle());
        Notification_fix_contents = (EditText) findViewById(R.id.Notification_fix_contents);
        Notification_fix_contents.setText(notification.getContents());

        Notification_fix_viewpager = findViewById(R.id.Notification_fix_viewpager);
        imageBitmaps = notification.getImageBitmap(this);
        adapter_notification_imageViewpager = new Adapter_notification_image_viewpager(this, imageBitmaps);
        Notification_fix_viewpager.setAdapter(adapter_notification_imageViewpager);

        Notification_fix_indicator = findViewById(R.id.Notification_fix_indicator);
        Notification_fix_indicator.setViewPager(Notification_fix_viewpager);
        Notification_fix_indicator.createIndicators(notification.getImageMax(), 0);
        Notification_fix_viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        Notification_fix_viewpager.setCurrentItem(1000); //시작 지점
        Notification_fix_viewpager.setOffscreenPageLimit(notification.getImageMax()); //최대 이미지 수
        Notification_fix_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    Notification_fix_viewpager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Notification_fix_indicator.animatePageSelected(position % notification.getImageMax());
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
                    Notification_fix_viewpager.setAdapter(adapter_notification_imageViewpager);
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

}