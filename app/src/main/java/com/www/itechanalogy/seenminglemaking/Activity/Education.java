package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import java.util.HashMap;
import java.util.Map;

public class Education extends AppCompatActivity {
    private String id,status;
    private ImageButton close;
    private EditText edu;
    private Button update;
    private TextView bachelors_degree,masters_degree,no_degree,collage,doctorate,education_other,secondry_school;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        checkNetwork();
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close = findViewById(R.id.close);
        edu = findViewById(R.id.edu);
        update = findViewById(R.id.update);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bachelors_degree = findViewById(R.id.bachelors_degree);
        masters_degree = findViewById(R.id.masters_degree);
        no_degree = findViewById(R.id.no_degree);
        collage = findViewById(R.id.collage);
        doctorate = findViewById(R.id.doctorate);
        secondry_school = findViewById(R.id.secondry_school);
        education_other = findViewById(R.id.education_other);
        edu.setText(status);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edu.getText().toString().length()>0){
                    updateEducation(id,edu.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Please write education",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(status.equals("Bachelors Degree")){
            bachelors_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Masters Degree")){
            masters_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("No Degree")){
            no_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Collage")){
            collage.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Doctorate")){
            doctorate.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Secondary School")){
            secondry_school.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else{
            education_other.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        bachelors_degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                updateEducation(id,"Bachelors Degree");
            }
        });
        masters_degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                updateEducation(id,"Masters Degree");
            }
        });
        no_degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                updateEducation(id,"No Degree");
            }
        });
        collage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(getResources().getDrawable(R.drawable.round_border));
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                updateEducation(id,"Collage");
            }
        });
        doctorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(getResources().getDrawable(R.drawable.round_border));
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                updateEducation(id,"Doctorate");
            }
        });
        secondry_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(getResources().getDrawable(R.drawable.round_border));
                education_other.setBackground(null);
                updateEducation(id,"Secondary School");
            }
        });
        education_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(getResources().getDrawable(R.drawable.round_border));
                updateEducation(id,"Other");
            }
        });
    }
    private void updateEducation(final String id, final String edication){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Education updated",Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("id",id);
                data.put("education",edication);
                data.put("action","updateEducation");
                return data;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private void checkNetwork(){
        if(isNetworkAvailable(getApplicationContext())){

        }else{
            Intent i = new Intent(getApplicationContext(), NoInternet.class);
            startActivity(i);
        }
    }
}