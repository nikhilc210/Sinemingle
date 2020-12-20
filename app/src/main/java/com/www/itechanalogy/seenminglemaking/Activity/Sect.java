package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

public class Sect extends AppCompatActivity {
    private String id,status;
    private TextView sunni,shia,all,other;
    private ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sect);
        checkNetwork();
        close = findViewById(R.id.close);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        sunni = findViewById(R.id.sunni);
        shia = findViewById(R.id.shia);
        all = findViewById(R.id.all);
        other = findViewById(R.id.other);
        if(status.equals("Other")){
            other.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Muslim")){
            sunni.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Christian")){
            shia.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("All")){
            all.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sunni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunni.setBackground(getResources().getDrawable(R.drawable.round_border));
                shia.setBackground(null);
                all.setBackground(null);
                other.setBackground(null);
                updateSect(id,"Muslim");
            }
        });
        shia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunni.setBackground(null);
                shia.setBackground(getResources().getDrawable(R.drawable.round_border));
                all.setBackground(null);
                other.setBackground(null);
                updateSect(id,"Christian");
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunni.setBackground(null);
                shia.setBackground(null);
                all.setBackground(getResources().getDrawable(R.drawable.round_border));
                other.setBackground(null);
                updateSect(id,"All");
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunni.setBackground(null);
                shia.setBackground(null);
                all.setBackground(null);
                other.setBackground(getResources().getDrawable(R.drawable.round_border));
                updateSect(id,"Other");
            }
        });
    }
    private void updateSect(final String id, final String sect){

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
                data.put("sect",sect);
                data.put("action","updateSect");
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