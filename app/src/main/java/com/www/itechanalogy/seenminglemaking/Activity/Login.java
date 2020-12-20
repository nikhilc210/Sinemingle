package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.merkmod.achievementtoastlibrary.AchievementToast;
import com.universalvideoview.UniversalVideoView;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private UniversalVideoView video_view;
    private TextView signup;
    private EditText email,pass;
    private RelativeLayout continue_email;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkNetwork();
        video_view = findViewById(R.id.video_view);
        signup = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        continue_email = findViewById(R.id.continue_email);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String path = "android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.one;
        video_view.setVideoURI(Uri.parse(path));
        video_view.setOnPreparedListener(new
                                                 MediaPlayer.OnPreparedListener()  {
                                                     @Override
                                                     public void onPrepared(MediaPlayer mp) {
                                                         mp.setLooping(true);
                                                         Log.i("TAG", "Duration = " +
                                                                 video_view.getDuration());
                                                     }
                                                 });
        video_view.start();
        continue_email = findViewById(R.id.continue_email);
        continue_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()>0){
                    if(pass.getText().toString().length()>0){
                        doLogin(email.getText().toString(),pass.getText().toString());
                    }else{
                        AchievementToast.makeAchievement(Login.this, "Please enter password", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

                    }
                }else{
                    AchievementToast.makeAchievement(Login.this, "Please enter email address", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

                }
            }
        });
    }
    private void doLogin(final String email, final String pass){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String mobile = obj.getString("mobile");
                        String gender = obj.getString("gender");
                        String dob = obj.getString("dob");
                        String image = obj.getString("image");
                        sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createUserSession(id,name,email,mobile,gender,dob,"",image);
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), CompleteDashboard.class);
                        startActivity(i);
                        finish();
                    }else{
                        dialog.dismiss();
                        AchievementToast.makeAchievement(Login.this, "Invalid email or password", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("email",email);
                data.put("password",pass);
                data.put("action","doLogin");
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
