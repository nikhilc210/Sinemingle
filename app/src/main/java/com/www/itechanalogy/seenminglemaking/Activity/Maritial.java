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

public class Maritial extends AppCompatActivity {
    private TextView never_married,divorced,separated,annulled,widowed;
    private String id, status;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maritial);
        checkNetwork();
        never_married = findViewById(R.id.never_married);
        divorced = findViewById(R.id.divorced);
        separated = findViewById(R.id.separated);
        annulled = findViewById(R.id.annulled);
        widowed = findViewById(R.id.widowed);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(status.equals("Never Married")){
            never_married.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Divorced")){
            divorced.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Separated")){
            separated.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Annulled")){
            annulled.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Widow")){
            widowed.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        never_married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                never_married.setBackground(getResources().getDrawable(R.drawable.round_border));
                divorced.setBackground(null);
                annulled.setBackground(null);
                separated.setBackground(null);
                widowed.setBackground(null);
                updateMaritial(id,"Never Married");
            }
        });
        divorced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divorced.setBackground(getResources().getDrawable(R.drawable.round_border));
                separated.setBackground(null);
                annulled.setBackground(null);
                never_married.setBackground(null);
                widowed.setBackground(null);
                updateMaritial(id,"Divorced");

            }
        });
        separated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                separated.setBackground(getResources().getDrawable(R.drawable.round_border));
                divorced.setBackground(null);
                annulled.setBackground(null);
                never_married.setBackground(null);
                widowed.setBackground(null);
                updateMaritial(id,"Separated");
            }
        });
        annulled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                annulled.setBackground(getResources().getDrawable(R.drawable.round_border));
                separated.setBackground(null);
                divorced.setBackground(null);
                never_married.setBackground(null);
                widowed.setBackground(null);
                updateMaritial(id,"Annulled");
            }
        });
        widowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widowed.setBackground(getResources().getDrawable(R.drawable.round_border));
                annulled.setBackground(null);
                separated.setBackground(null);
                divorced.setBackground(null);
                never_married.setBackground(null);
                updateMaritial(id,"Widow");

            }
        });

    }
    private void updateMaritial(final String id, final String maritial){
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
                data.put("maritial",maritial);
                data.put("action","updateMaritial");
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