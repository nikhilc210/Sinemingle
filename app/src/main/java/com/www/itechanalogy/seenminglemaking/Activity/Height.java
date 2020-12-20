package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Height extends AppCompatActivity {
    private LoopView loopView;
    ArrayList<String> list = new ArrayList<>();
    private String id;
    private ImageButton back;
    private String HEIGHT = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        checkNetwork();
        loopView = findViewById(R.id.loopView);
        id = getIntent().getExtras().getString("id");
        back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list.add("122cm");
        list.add("124cm");
        list.add("127cm");
        list.add("130cm");
        list.add("132cm");
        list.add("135cm");
        list.add("137cm");
        list.add("140cm");
        list.add("142cm");
        list.add("145cm");
        list.add("147cm");
        list.add("150cm");
        list.add("152cm");
        list.add("155cm");
        list.add("157cm");
        list.add("160cm");
        list.add("163cm");
        list.add("165cm");
        list.add("168cm");
        list.add("170cm");
        list.add("173cm");
        list.add("175cm");
        list.add("178cm");
        list.add("180cm");
        list.add("183cm");
        list.add("185cm");
        list.add("188cm");
        list.add("190cm");
        list.add("193cm");
        list.add("196cm");
        list.add("198cm");
        list.add("201cm");
        list.add("203cm");
        list.add("206cm");
        list.add("208cm");
        list.add("211cm");
        list.add("213cm");
        list.add("216cm");
        list.add("218cm");
        list.add("221cm");
        list.add("224cm");
        list.add("226cm");
        list.add("229cm");
        list.add("231cm");
        list.add("234cm");
        list.add("236cm");
        list.add("239cm");
        list.add("241cm");
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                //  HEIGHT = String.valueOf(index);
                HEIGHT =  list.get(index);
                //  Toast.makeText(getApplicationContext(),HEIGHT,Toast.LENGTH_LONG).show();
                Log.d("Height======>",HEIGHT);
                updateHeight(id,HEIGHT);

            }
        });
        loopView.setOnItemScrollListener(new OnItemScrollListener() {
            @Override

            public void onItemScrollStateChanged(LoopView loopView, int currentPassItem, int oldScrollState, int scrollState, int totalScrollY) {

                Log.i("gy",String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d",currentPassItem,oldScrollState,scrollState,totalScrollY));
            }

            @Override
            public void onItemScrolling(LoopView loopView, int currentPassItem, int scrollState, int totalScrollY) {
                Log.i("gy",String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d",currentPassItem,scrollState,totalScrollY));
            }
        });
        loopView.setItems(list);


    }
    private void updateHeight(final String id, String height){
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
                data.put("height",HEIGHT);
                data.put("action","updateHeight");
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