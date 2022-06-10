package com.example.mysenior.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.DTO.User;
import com.example.mysenior.Global;
import com.example.mysenior.R;
import com.example.mysenior.Request.PatientAddRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Activity_Worker_Detail  extends Activity {
    TextView worker_detail_category,worker_detail_name,worker_detail_position,worker_detail_department;
    ImageView worker_detail_image;
    Button worker_detail_remove;
    User worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_worker_detail);

        Intent intent = getIntent();
        worker = (User)intent.getSerializableExtra("Worker");

        worker_detail_image = (ImageView) findViewById(R.id.worker_detail_image);
        if (worker.getU_image().equals("")) worker_detail_image.setImageResource(R.drawable.default_user);
        else worker_detail_image.setImageBitmap(worker.getU_imageBitmap());

        worker_detail_category = (TextView) findViewById(R.id.worker_detail_category);
        if(worker.getIsAdmin() == 1){
//            worker_detail_category.setText();
        }else{
//            worker_detail_category.setText();
        }

        worker_detail_name = (TextView) findViewById(R.id.worker_detail_name);
        worker_detail_name.setText(worker.getU_name());

        worker_detail_position = (TextView) findViewById(R.id.worker_detail_position);
        worker_detail_position.setText(worker.getPosition());

        worker_detail_department = (TextView) findViewById(R.id.worker_detail_department);
        worker_detail_department.setText(worker.getDepartment());

        worker_detail_remove = (Button) findViewById(R.id.worker_detail_remove);
        worker_detail_remove.setOnClickListener(removeOnClickListener);
    }

    View.OnClickListener removeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            }
    };
}