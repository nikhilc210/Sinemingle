package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.www.itechanalogy.seenminglemaking.Fragment.Cards;
import com.www.itechanalogy.seenminglemaking.Fragment.Chat;
import com.www.itechanalogy.seenminglemaking.Fragment.Explore;
import com.www.itechanalogy.seenminglemaking.Fragment.Setting;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;

import java.util.HashMap;
import java.util.Map;

public class CompleteDashboard extends AppCompatActivity {
    private BottomNavigationView bottom;
    private ImageButton user_info;
    private SessionManager sessionManager;
    private String myid, token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_dashboard);
        bottom = findViewById(R.id.bottom);
        user_info = findViewById(R.id.user_info);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String> data = sessionManager.getUserDetails();
        myid = data.get(SessionManager.KEY_ID);
        token = FirebaseInstanceId.getInstance().getToken();
        updateDeviceToken(myid,token);
        setDefault();
        checkNetwork();
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Filters.class);
                startActivity(i);
            }
        });
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.cards:
                        fragment = new Cards().newInstance();
                        // Dashboard.this.toolbar.setTitle((CharSequence) "Chat");
                        break;
                    case R.id.eye:
                        fragment = new Explore().newInstance();
                        break;
                    case R.id.filter:
                        fragment = new Filters().newInstance();
                        break;
                    case R.id.chat:
                        fragment = new Chat().newInstance();
                        break;
                    case R.id.settings:
                        fragment = new Setting().newInstance();
                        break;


                }
                FragmentTransaction fragmentTransaction = CompleteDashboard.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });
    }
    private void setDefault() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, Cards.newInstance());
        fragmentTransaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
    }
    @SuppressLint("MissingPermission")
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
    private void updateDeviceToken(final String myid, final String token){
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
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String>data = new HashMap<>();
                data.put("id",myid);
                data.put("token",token);
                data.put("action","refreshToken");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}