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
import com.google.android.gms.auth.api.Auth;
import com.merkmod.achievementtoastlibrary.AchievementToast;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateEmail extends AppCompatActivity {
    private ImageButton back;
    private EditText email;
    private Button update;
    private SessionManager sessionManager;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        checkNetwork();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        update = findViewById(R.id.update);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length()>0){
                    updateEmail(email.getText().toString(),id);
                }else{
                    AchievementToast.makeAchievement(UpdateEmail.this, "Please enter email address", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

                }
            }
        });
    }
    private void updateEmail(final String email, final String id){
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
                        AchievementToast.makeAchievement(UpdateEmail.this, "Your email updated", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.com_facebook_button_like_icon_selected)).show();

                    }else{
                        AchievementToast.makeAchievement(UpdateEmail.this, object.getString("error"), AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

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
                data.put("email",email);
                data.put("action","updateEmail");
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
