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

public class Children extends AppCompatActivity {
    private TextView yes_child,no_child;
    private String id, status;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        yes_child = findViewById(R.id.yes_child);
        no_child = findViewById(R.id.no_child);
        checkNetwork();
        if(status.equals("Yes")){
            yes_child.setBackground(getResources().getDrawable(R.drawable.round_border));

        }else if(status.equals("Yes")){
            no_child.setBackground(getResources().getDrawable(R.drawable.round_border));
            //yes_child.setBackground(null);
        }
        yes_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes_child.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_child.setBackground(null);
                updateChild(id,"Yes");
            }
        });
        no_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_child.setBackground(getResources().getDrawable(R.drawable.round_border));
                yes_child.setBackground(null);
                updateChild(id,"No");
            }
        });
    }
    private void updateChild(final String id, final String child){
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
                data.put("child",child);
                data.put("action","updateChild");
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