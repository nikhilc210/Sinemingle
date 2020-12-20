
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

public class Marriage extends AppCompatActivity {
    private TextView as_soon_marrige,one_to_two_year,three_to_four_years,four_plus_years;
    private String id, status;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marriage);
        checkNetwork();
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        as_soon_marrige = findViewById(R.id.as_soon_marrige);
        one_to_two_year = findViewById(R.id.one_to_two_year);
        three_to_four_years = findViewById(R.id.three_to_four_years);
        four_plus_years = findViewById(R.id.four_plus_years);
        if(status.equals("As Soon As Possible")){
            as_soon_marrige.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("One to Two Year")){
            one_to_two_year.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Three to Four Year")){
            three_to_four_years.setBackground(getResources().getDrawable(R.drawable.round_border));
        }else if(status.equals("Four+ Year")){
            four_plus_years.setBackground(getResources().getDrawable(R.drawable.round_border));
        }
        as_soon_marrige.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_soon_marrige.setBackground(getResources().getDrawable(R.drawable.round_border));
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(null);
                updateMarriageTime(id,"As Soon As Possible");
            }
        });
        one_to_two_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(getResources().getDrawable(R.drawable.round_border));
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(null);
                updateMarriageTime(id,"One to Two Year");
            }
        });
        three_to_four_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(getResources().getDrawable(R.drawable.round_border));
                four_plus_years.setBackground(null);
                updateMarriageTime(id,"Three to Four Year");
            }
        });
        four_plus_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(getResources().getDrawable(R.drawable.round_border));
                updateMarriageTime(id,"Four+ Year");
            }
        });
    }
    private void updateMarriageTime(final String id, final String time){
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
                data.put("time",time);
                data.put("action","updateMarriageTime");
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