package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.auth.api.Auth;
import com.suke.widget.SwitchButton;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.MainActivity;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainSettings extends AppCompatActivity {
    private ImageButton close;
    private RelativeLayout update_email,complete,deactivate;
    private SwitchButton enable_facebook,enable_notification,enable_show,enable_lock,enable_chaperone;
    private SessionManager sessionManager;
    private String id;
    private String fb_status,notification_status,show_me,lock_status,chaperone_status;
    private RelativeLayout logout;
    private TextView version_text,my_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        checkNetwork();
        close = findViewById(R.id.close);
        enable_facebook = findViewById(R.id.enable_facebook);
        enable_notification = findViewById(R.id.enable_notification);
        enable_show = findViewById(R.id.enable_show);
        enable_lock = findViewById(R.id.enable_lock);
        enable_chaperone = findViewById(R.id.enable_chaperone);
        version_text = findViewById(R.id.version_text);
        deactivate = findViewById(R.id.deactivate);
        my_email = findViewById(R.id.my_email);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        logout = findViewById(R.id.logout);
        getMySettingStatus(id);
        String versionName = BuildConfig.VERSION_NAME;
        version_text.setText(versionName);
        getData(id);
        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivateAccount(id);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });
        enable_notification.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    notification_status = "Yes";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }else{
                    notification_status = "No";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }
            }
        });
        enable_facebook.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    fb_status = "Yes";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }else{
                    fb_status = "No";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }
            }
        });
        enable_show.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    show_me = "Yes";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }else{
                    show_me = "No";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }
            }
        });
        enable_lock.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    lock_status = "Yes";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }else{
                    lock_status = "No";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }
            }
        });
        enable_chaperone.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    chaperone_status = "Yes";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }else{
                    chaperone_status = "No";
                    updateSetting(fb_status,notification_status,show_me,lock_status,chaperone_status);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update_email = findViewById(R.id.update_email);
        complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateProfile.class);
                startActivity(i);
            }
        });
        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateEmail.class);
                startActivity(i);
            }
        });
    }
    private void getMySettingStatus(final String id){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    fb_status = object.getString("fb_status");
                    notification_status = object.getString("notification_status");
                    show_me = object.getString("show_me");
                    lock_status = object.getString("lock_status");
                    chaperone_status = object.getString("chaperone_status");
                    if(fb_status.equals("Yes")){
                        enable_facebook.setChecked(true);
                    }else{
                        enable_facebook.setChecked(false);
                    }
                    if(notification_status.equals("Yes")){
                        enable_notification.setChecked(true);
                    }else{
                        enable_notification.setChecked(false);
                    }
                    if(show_me.equals("Yes")){
                        enable_show.setChecked(true);
                    }else{
                        enable_show.setChecked(false);
                    }
                    if(chaperone_status.equals("Yes")){
                        enable_chaperone.setChecked(true);
                    }else{
                        enable_chaperone.setChecked(false);
                    }
                    if(lock_status.equals("Yes")){
                        enable_lock.setChecked(true);
                    }else{
                        enable_lock.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","getMySettings");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void updateSetting(final String fb, final String noti, final String show, final String lock, final String chaperon){
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
                data.put("action","updateMySettings");
                data.put("fb",fb);
                data.put("noti",noti);
                data.put("show",show);
                data.put("lock",lock);
                data.put("chaperon",chaperon);
                data.put("id",id);
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
    private void deactivateAccount(final String id){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                        sessionManager.clearData();
                        sessionManager.logoutUser();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","deactivateAccount");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getData(final String id){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                        String email = object.getString("email");
                        my_email.setText(email);

                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                data.put("action","getSettingEmail");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(id);
    }
}
