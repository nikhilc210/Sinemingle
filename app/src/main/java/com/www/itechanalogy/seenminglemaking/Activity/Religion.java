package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Religion extends AppCompatActivity {
    private String id,status;
    private ImageButton close;
    private TextView very_practising,practising,moderately_practising,not_practising;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religion);
        checkNetwork();
        close = findViewById(R.id.close);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        very_practising = findViewById(R.id.very_practising);
        practising = findViewById(R.id.practising);
        moderately_practising = findViewById(R.id.moderately_practising);
        not_practising = findViewById(R.id.not_practising);
        if(status.equals("Very Practising")){
            very_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Practising")){
            practising.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Moderately Practising")){
            moderately_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Not Practising")){
            not_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        very_practising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                very_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                practising.setBackground(null);
                moderately_practising.setBackground(null);
                not_practising.setBackground(null);
                updateReligion(id,"Very Practising");
            }
        });
        practising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                very_practising.setBackground(null);
                practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                moderately_practising.setBackground(null);
                not_practising.setBackground(null);
                updateReligion(id,"Practising");
            }
        });
        moderately_practising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                very_practising.setBackground(null);
                practising.setBackground(null);
                moderately_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                not_practising.setBackground(null);
                updateReligion(id,"Moderately Practising");
            }
        });
        not_practising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                very_practising.setBackground(null);
                practising.setBackground(null);
                moderately_practising.setBackground(null);
                not_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                updateReligion(id,"Not Practising");
            }
        });

    }
    private void updateReligion(final String id, final String religion){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",id);
                data.put("religion",religion);
                data.put("action","updateReligion");
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