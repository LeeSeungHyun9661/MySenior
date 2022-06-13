package com.example.mysenior.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.BitmapController;
import com.example.mysenior.CODES;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientDeleteRequest;
import com.example.mysenior.Request.PatientImageRequest;
import com.example.mysenior.Request.WorkerDeleteRequest;
import com.example.mysenior.Request.WorkerImageRequest;

import org.json.JSONException;
import org.json.JSONObject;
/*
MySenior
작성일자 : 2022-06-13
작성자 : 이승현(팀원)
작성목적 : 2022년 종합설계 팀프로젝트 - 요양원 관리 애플리케이션 'MySenior'
_________
액티비티 클래스

이름 : Activity_Worker_Detail
역할 : 병원 직원에 대한 상세한 정보를 확인할 수 있는 액티비티
기능 :
    1) 병원 직원의 직책과 소속, 권한을 불러와 표시가능
    2) 병원 
    3) 길게 눌러 권한을 거부할 수 있음
특이사항 :
    - 이미 권한을 거부한 사람에 대한 목록을 통해 권한 거부를 상세하게 처리할 예정
    - 병원 권한이 다양하게 지정될 경우 권한 등급을 선택해줄 수 있도록 조치할 예정
    - 권한 삭제에 따른 게시물 처리 등 병원 데이터 처리 정책을 지정할 수 있도록 변경 필요
 */
public class Activity_Worker_Detail  extends Activity {
    private TextView textview_name, textview_position, textview_department;
    private ImageView imageview_image, imageview_category, imageview_imageChange;
    private Button button_remove;
    private User worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_worker_detail);
        
        setUI();

        //직원 정보를 받아옴
        Intent intent = getIntent();
        worker = (User)intent.getSerializableExtra("Worker");

        //직원 이미지를 설정하고 없을 경우 기본 이미지를 적용
        if (worker.getU_image().equals("")) imageview_image.setImageResource(R.drawable.default_worker_128);
        else imageview_image.setImageBitmap(worker.getU_imageBitmap());
        
        //직원이 본인과 동일할 경우 이미지 변경 권한 부여
        if(worker.getU_id().equals(Global.getInstance().getUser().getU_id())){
            imageview_imageChange.setVisibility(View.VISIBLE);
            imageview_imageChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, CODES.REQUEST_CODE);
                }
            });
            //아닐 경우에는 숨김
        }else{
            imageview_imageChange.setVisibility(View.INVISIBLE);
        }



        
        //직원이 관리자인지 일반 직원인지를 분리해서 표시
        if(worker.getIsAdmin()) imageview_category.setImageResource(R.drawable.icon_admin);
        else imageview_category.setImageResource(R.drawable.icon_worker);

        textview_name.setText(worker.getU_name());
        textview_position.setText(worker.getPosition());
        textview_department.setText(worker.getDepartment());

        //직원 삭제를 통해 해당 직원을 병원에서 배제
        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.getInstance().getUser().getIsAdmin()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(Activity_Worker_Detail.this);
                    dlg.setTitle("삭제")
                            .setMessage("직원 정보를 삭제하시겠습니까?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String u_id = worker.getU_id();
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    WorkerDeleteRequest workerDeleteRequest = new WorkerDeleteRequest(u_id,Global.getInstance().getHospital().getH_id(), responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Detail.this);
                                    queue.add(workerDeleteRequest);
                                }
                            }).show();
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setUI() {
        imageview_image = (ImageView) findViewById(R.id.worker_detail_image);
        imageview_imageChange = (ImageView) findViewById(R.id.worker_detail_image_change);
        imageview_category = (ImageView) findViewById(R.id.worker_detail_category);
        textview_position = (TextView) findViewById(R.id.worker_detail_position);
        textview_name = (TextView) findViewById(R.id.worker_detail_name);
        textview_department = (TextView) findViewById(R.id.worker_detail_department);
        button_remove = (Button) findViewById(R.id.worker_detail_remove);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODES.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapController bitmapController = new BitmapController(getContentResolver().openInputStream(data.getData()),getResources().getConfiguration());
                    imageview_image.setImageBitmap(bitmapController.resize());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.w("RESPONSE", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                                    imageview_image.setImageBitmap(bitmapController.resize());
                                } else {
                                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    WorkerImageRequest workerImageRequest = new WorkerImageRequest(worker.getU_id(), bitmapController.toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Activity_Worker_Detail.this);
                    queue.add(workerImageRequest);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


}