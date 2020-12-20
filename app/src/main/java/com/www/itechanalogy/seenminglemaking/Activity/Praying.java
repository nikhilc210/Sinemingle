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

public class Praying extends AppCompatActivity {
    private TextView always_pray,usually_pray,sometimes_pray,never_pray;
    private String id,status;
    private ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praying);
        checkNetwork();
        close = findViewById(R.id.close);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        always_pray = findViewById(R.id.always_pray);
        usually_pray = findViewById(R.id.usually_pray);
        sometimes_pray = findViewById(R.id.sometimes_pray);
        never_pray = findViewById(R.id.never_pray);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(status.equals("Always Pray")){
            always_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Usually Pray")){
            usually_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Sometimes Pray")){
            sometimes_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Never Pray")){
            never_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        always_pray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                always_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(null);
                never_pray.setBackground(null);
                updatePray(id,"Always Pray");
            }
        });
        usually_pray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                always_pray.setBackground(null);
                usually_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                sometimes_pray.setBackground(null);
                never_pray.setBackground(null);
                updatePray(id,"Usually Pray");
            }
        });
        sometimes_pray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                always_pray.setBackground(null);
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                never_pray.setBackground(null);
                updatePray(id,"Sometimes Pray");
            }
        });
        never_pray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                always_pray.setBackground(null);
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(null);
                never_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                updatePray(id,"Never Pray");
            }
        });
    }
    private void updatePray(final String id, final String pray){
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
                data.put("pray",pray);
                data.put("action","updatePray");
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