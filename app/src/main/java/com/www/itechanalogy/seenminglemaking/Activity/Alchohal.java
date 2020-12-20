package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class Alchohal extends AppCompatActivity {
    private TextView yes_alchol,no_alchol;
    private String id,status;
    private ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alchohal);
        close = findViewById(R.id.close);
        yes_alchol = findViewById(R.id.no_alchol);
        no_alchol = findViewById(R.id.no_alchol);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(status.equals("Yes")){
            yes_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("No")){
            no_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        yes_alchol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_alchol.setBackground(null);
                updateAlcholo(id,"Yes");
            }
        });
        no_alchol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
                yes_alchol.setBackground(null);
                updateAlcholo(id,"No");
            }
        });
    }
    private void updateAlcholo(final String id, final String alchoal){
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
                data.put("alchoal",alchoal);
                data.put("action","updateAlcholol");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}