package com.example.mysenior.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mysenior.Activity.Activity_Worker_Add;
import com.example.mysenior.Adapter.Adapter_worker_gridview;
import com.example.mysenior.DTO.Hospital;
import com.example.mysenior.DTO.User;
import com.example.mysenior.R;
import com.example.mysenior.Request.WorkerRequest;
import com.example.mysenior.Request.WorkerSearchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_worker extends Fragment {
    User user;
    Hospital hospital;
    EditText fragment_worker_search;
    ArrayList<User> userArrayList;
    GridView fragment_worker_gridview;
    Adapter_worker_gridview worker_gridview_adapter;
    Button fragment_worker_add;

    public Fragment_worker(User user, Hospital hospital) {
        this.hospital = hospital;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        fragment_worker_search = (EditText) view.findViewById(R.id.fragment_worker_search);
        fragment_worker_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = fragment_worker_search.getText().toString();
                Log.w("Search : ", search);
                if (search.length() >= 2) {
                    searchWorker(search);
                } else {
                    getWorker();
                }
            }
        });

        fragment_worker_gridview = (GridView) view.findViewById(R.id.fragment_worker_gridview);

        fragment_worker_add = (Button) view.findViewById(R.id.fragment_worker_add);
        fragment_worker_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_Worker_Add.class);
                startActivity(intent);

            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        getWorker();
    }

    private void getWorker() {
        String h_id = hospital.getH_id();
        userArrayList = new ArrayList<>();
        worker_gridview_adapter = new Adapter_worker_gridview(getActivity(), userArrayList);
        fragment_worker_gridview.setAdapter(worker_gridview_adapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("worker");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String u_image = item.getString("u_image");
                        String h_id = item.getString("h_id");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int isadmin = item.getInt("isadmin");
                        userArrayList.add(new User(a_id, u_id, u_name, h_id, position, department, u_image, isadmin));
                        worker_gridview_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        WorkerRequest workerRequest = new WorkerRequest(h_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(workerRequest);
    }

    private void searchWorker(String search) {
        String h_id = hospital.getH_id();
        userArrayList = new ArrayList<>();
        worker_gridview_adapter = new Adapter_worker_gridview(getActivity(), userArrayList);
        fragment_worker_gridview.setAdapter(worker_gridview_adapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("worker");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String a_id = item.getString("a_id");
                        String u_id = item.getString("u_id");
                        String u_name = item.getString("u_name");
                        String u_image = item.getString("u_image");
                        String h_id = item.getString("h_id");
                        String position = item.getString("position");
                        String department = item.getString("department");
                        int isadmin = item.getInt("isadmin");
                        userArrayList.add(new User(a_id, u_id, u_name, h_id, position, department, u_image, isadmin));
                        worker_gridview_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        WorkerSearchRequest workerSearchRequest = new WorkerSearchRequest(h_id, search, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(workerSearchRequest);
    }

}
