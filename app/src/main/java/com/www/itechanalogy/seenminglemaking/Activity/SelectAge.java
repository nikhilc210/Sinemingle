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
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectAge extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private SessionManager sessionManager;
    private String id;
    private RelativeLayout age_section;
    private Switch any_age;
    private ImageButton back;
    private SeekBar rangeSeekbar1;
    private TextView selected_age;
    private TextView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_age);
        checkNetwork();
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        age_section = findViewById(R.id.age_section);
        any_age = findViewById(R.id.any_age);
        selected_age = findViewById(R.id.selected_age);
        rangeSeekbar1 = findViewById(R.id.rangeSeekbar1);
        rangeSeekbar1.setOnSeekBarChangeListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
            }
        });
        getMyAgeFilter(id);
        any_age.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    age_section.setVisibility(View.VISIBLE);
                }else{
                    age_section.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getMyAgeFilter(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        String st = obj.getString("age");
                        String cou = obj.getString("age_count");
                        rangeSeekbar1.setProgress(Integer.parseInt(cou));
                        any_age.setChecked(true);
                        age_section.setVisibility(View.VISIBLE);
                        selected_age.setText(cou+" years");


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
                data.put("id",id);
                data.put("action","getAgeFilter");
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
    private void updateFilterAge(String id, String age){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object  = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                       // Toast.makeText(getApplicationContext(),"Age Filter Saved",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
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
                data.put("myid",id);
                data.put("age",age);
                data.put("action","saveAgeFilter");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        String my = String.valueOf(i);
        selected_age.setText(my+" years");
        updateFilterAge(id,my);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}