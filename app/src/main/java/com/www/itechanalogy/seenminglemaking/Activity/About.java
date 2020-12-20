package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class About extends AppCompatActivity {
    private String id,status;
    private ImageButton close;
    EditText my_about;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close = findViewById(R.id.close);
        my_about = findViewById(R.id.my_about);
        update = findViewById(R.id.update);
        my_about.setText(status);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my_about.getText().toString().length()>0){
                    updateAbout(id,my_about.getText().toString());
                }else {
                    Toast.makeText(getApplicationContext(),"Please write your about",Toast.LENGTH_LONG).show();
                }
            }
        });
        checkNetwork();
    }
    private void updateAbout(final String id, final String about){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        Toast.makeText(getApplicationContext(),"your About updated",Toast.LENGTH_LONG).show();
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

            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("id",id);
                data.put("about",about);
                data.put("action","updateAbout");
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