package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class Gender extends AppCompatActivity {
    private String id,status;
    private ImageButton close;
    private TextView male,female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        checkNetwork();
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close = findViewById(R.id.close);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(status.equals("Male")){
            male.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Female")){
            female.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackground(getResources().getDrawable(R.drawable.round_border));
                female.setBackground(null);
                updategender(id,"Male");
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackground(getResources().getDrawable(R.drawable.round_border));
                male.setBackground(null);
                updategender(id,"Female");
            }
        });
    }
    private void updategender(final String id, final String gender){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                data.put("gender",gender);
                data.put("action","updateGender");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
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
