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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Halal extends AppCompatActivity {
    private TextView yes_halal,no_halal;
    private String id,status;
    private ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal);
        checkNetwork();
        close = findViewById(R.id.close);
        yes_halal = findViewById(R.id.yes_halal);
        no_halal = findViewById(R.id.no_halal);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(status.equals("Yes")){
            yes_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("No")){
            no_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        yes_halal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_halal.setBackground(null);
                updateHalal(id,"Yes");
            }
        });
        no_halal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
                yes_halal.setBackground(null);
                updateHalal(id,"No");
            }
        });
    }
    private void updateHalal(final String id, final String halal){
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
                data.put("halal",halal);
                data.put("action","updateHalal");
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