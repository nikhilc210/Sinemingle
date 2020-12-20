package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.auth.api.Auth;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetLocation extends AppCompatActivity   implements SeekBar.OnSeekBarChangeListener{
    private SessionManager sessionManager;
    private String id;
    private Switch location_switch,country_limit;
    private RelativeLayout location_section;
    private ImageButton back;
    private SeekBar rangeSeekbar1;
    private TextView distance;
    private String IS_LIMIT_BY_DISTANCE = "NO";
    private String DISTANCE = "0";
    private TextView save;
    private RelativeLayout country_section;
    private TextView countryname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        checkNetwork();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        location_switch = findViewById(R.id.location_switch);
        country_limit = findViewById(R.id.country_limit);
        location_section = findViewById(R.id.location_section);
        rangeSeekbar1 = findViewById(R.id.rangeSeekbar1);

        back = findViewById(R.id.back);
        distance = findViewById(R.id.distance);
        save = findViewById(R.id.save);
        country_section = findViewById(R.id.country_section);
        countryname = findViewById(R.id.countryname);
        getMyLocationFilter(id);
        rangeSeekbar1.setOnSeekBarChangeListener(this);
//        rangeSeekbar1.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveDistanceFilter(IS_LIMIT_BY_DISTANCE,DISTANCE,id);
            }
        });

        country_limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    country_section.setVisibility(View.VISIBLE);
                }else{
                    country_section.setVisibility(View.GONE);
                    limitCountry("No",id);
                }
            }
        });
        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    location_section.setVisibility(View.VISIBLE);
                }else{
                    location_section.setVisibility(View.GONE);
                }
            }
        });

        country_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i   = new Intent(getApplicationContext(),SelectFilterCountry.class);
                startActivity(i);
            }
        });

    }
    private void limitCountry(String status, String id){
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
                data.put("action","updateLimitCountry");
                data.put("status",status);
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getMyLocationFilter(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){

                        String limit_by_distance = object.getString("limit_by_distance");
                        String distance = object.getString("distance");
                        String limit_by_country = object.getString("limit_by_country");
                        String country = object.getString("country");
                        String countryStatus = object.getString("countryStatus");
                        rangeSeekbar1.setProgress(Integer.parseInt(distance));
                        DISTANCE = distance;
                        if(countryStatus.equals("Yes")){
                            country_limit.setChecked(true);
                            countryname.setText(country);
                            country_section.setVisibility(View.VISIBLE);
                        }else{
                            country_limit.setChecked(false);
                        }
//                        rangeSeekbar1.setMinStartValue(0).setMaxValue(Float.parseFloat(distance)).apply();
//                        Toast.makeText(getApplicationContext(),distance,Toast.LENGTH_LONG).show();

                      //  rangeSeekbar1.setMinStartValue(Float.parseFloat(distance)).apply();
                        if(limit_by_distance.equals("Yes")){
                            location_switch.setChecked(true);
                            //rangeSeekbar1.setMinStartValue(Float.parseFloat(distance)).apply();
                        }else{
                            location_switch.setChecked(false);
                        }
                        if(limit_by_country.equals("Yes")){
                            country_limit.setChecked(true);
                        }else{
                            country_limit.setChecked(false);
                        }
                    }
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
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","getMyLocationFilter");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void saveDistanceFilter(final String isLimit, final String distance, final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","saveDistanceFilter");
                data.put("id",id);
                data.put("distance",distance);
                data.put("isLimit",isLimit);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyLocationFilter(id);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        DISTANCE = String.valueOf(i);
        IS_LIMIT_BY_DISTANCE = "Yes";
        saveDistanceFilter(IS_LIMIT_BY_DISTANCE,DISTANCE,id);
        distance.setText("Up to "+i+" miles away");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
