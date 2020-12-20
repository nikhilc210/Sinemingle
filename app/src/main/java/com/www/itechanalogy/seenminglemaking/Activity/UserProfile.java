package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    private String id,myid,type;
    private SessionManager sessionManager;
    private ImageButton close,more;
    private ImageView main_image,flag_first,second_image,flag_two,third_image,fourth_image,fifth_image;
    private TextView user_name,age,prof,origin,about,maritial,height,origin_two,prof_two,job_title,employer,education,sect,religious,prays,eating,alchohal,smoke,marriage,move_abroad;
    private ImageButton dislike,like;
    private RelativeLayout loading;
    private Button unblock,unfavorite,favorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        checkNetwork();
        id = getIntent().getExtras().getString("id");
        type = getIntent().getExtras().getString("type");
        unblock = findViewById(R.id.unblock);
        unfavorite = findViewById(R.id.unfavorite);
        favorite = findViewById(R.id.favorite);
        if(type.equals("Blocked")){
            unblock.setVisibility(View.VISIBLE);
        }else if(type.equals("Fav")){
            unfavorite.setVisibility(View.VISIBLE);
        }else{
            favorite.setVisibility(View.VISIBLE);
        }
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String> data = sessionManager.getUserDetails();
        myid = data.get(SessionManager.KEY_ID);
        close = findViewById(R.id.close);
        more = findViewById(R.id.more);
        main_image = findViewById(R.id.main_image);
        flag_first = findViewById(R.id.flag_first);
        second_image = findViewById(R.id.second_image);
        flag_two = findViewById(R.id.flag_two);
        third_image = findViewById(R.id.third_image);
        fourth_image = findViewById(R.id.fourth_image);
        fifth_image = findViewById(R.id.fifth_image);
        user_name = findViewById(R.id.user_name);
        age = findViewById(R.id.age);
        prof = findViewById(R.id.prof);
        origin = findViewById(R.id.origin);
        about = findViewById(R.id.about);
        maritial = findViewById(R.id.maritial);
        height = findViewById(R.id.height);
        origin_two = findViewById(R.id.origin_two);
        prof_two = findViewById(R.id.prof_two);
        job_title = findViewById(R.id.job_title);
        employer = findViewById(R.id.employer);
        education = findViewById(R.id.education);
        sect = findViewById(R.id.sect);
        religious = findViewById(R.id.religious);
        prays = findViewById(R.id.prays);
        eating = findViewById(R.id.eating);
        alchohal = findViewById(R.id.alchohal);
        smoke = findViewById(R.id.smoke);
        marriage = findViewById(R.id.marriage);
        move_abroad = findViewById(R.id.move_abroad);
        dislike = findViewById(R.id.dislike);
        like = findViewById(R.id.like);
        loading = findViewById(R.id.loading);
        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unBlockUser(myid,id);
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makefav(myid,id);
            }
        });
        unfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unfav(myid,id);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(UserProfile.this);
                dialog.setContentView(R.layout.user_profile_more);
                dialog.setCancelable(false);
                dialog.show();

            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeUser(myid,id);
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeUser(myid,id);
            }
        });
        getUserProfile(id);
        submitVisited(myid,id);

    }
    private void submitVisited(final String myid, final String uid){
        if(myid.equals(id)){

        }else{
            StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String>data = new HashMap<>();
                    data.put("id",uid);
                    data.put("myid",myid);
                    data.put("action","submitVisited");
                    return data;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }
    }
    private void getUserProfile(final String id){

        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        String main_image_one = obj.getString("main_image");
                        String flag_first_one = obj.getString("flag_first");
                        String second_image_one = obj.getString("second_image");
                        String flag_two_one = obj.getString("flag_two");
                        String third_image_one = obj.getString("third_image");
                        String fourth_image_one = obj.getString("fourth_image");
                        String fifth_image_one = obj.getString("fifth_image");
                        String user_name_one = obj.getString("user_name");
                        String age_one = obj.getString("age");
                        String prof_one = obj.getString("prof");
                        String origin_one = obj.getString("origin");
                        String about_one = obj.getString("about");
                        String maritial_one = obj.getString("maritial");
                        String height_one = obj.getString("height");
                        String origin_two_one = obj.getString("origin_two");
                        String prof_two_one = obj.getString("prof_two");
                        String job_title_one = obj.getString("job_title");
                        String employer_one = obj.getString("employer");
                        String education_one = obj.getString("education");
                        String sect_one = obj.getString("sect");
                        String religious_one = obj.getString("religious");
                        String prays_one = obj.getString("prays");
                        String eating_one = obj.getString("eating");
                        String alchohal_one = obj.getString("alchohal");
                        String smoke_one = obj.getString("smoke");
                        String marriage_one = obj.getString("marriage");
                        String move_abroad_one = obj.getString("move_abroad");

                        if(main_image_one.length()>0){
                            Picasso.get().load(main_image_one).placeholder(R.drawable.placeholder).into(main_image);
                        }else{

                        }
                        Picasso.get().load(flag_first_one).into(flag_first);
                        Picasso.get().load(flag_two_one).into(flag_two);
                        if(second_image_one.length()>0){
                            Picasso.get().load(second_image_one).placeholder(R.drawable.placeholder).into(second_image);
                        }else{

                        }
                        if(fourth_image_one.length()>0){
                            Picasso.get().load(fourth_image_one).placeholder(R.drawable.placeholder).into(fourth_image);
                        }else{

                        }
                        if(third_image_one.length()>0){
                            Picasso.get().load(third_image_one).placeholder(R.drawable.placeholder).into(third_image);
                        }else{

                        }
                        if(fifth_image_one.length()>0){
                            Picasso.get().load(fifth_image_one).placeholder(R.drawable.placeholder).into(fifth_image);
                        }else{

                        }
                        user_name.setText(user_name_one);
                        age.setText(age_one);
                        prof.setText(prof_one);
                        origin.setText(origin_one);
                        about.setText(about_one);
                        maritial.setText(maritial_one);
                        height.setText(height_one);
                        origin_two.setText(origin_two_one);
                        prof_two.setText(prof_two_one);
                        job_title.setText(job_title_one);
                        employer.setText(employer_one);
                        education.setText(education_one);
                        sect.setText(sect_one);
                        religious.setText(religious_one);
                        prays.setText(prays_one);
                        eating.setText(eating_one);
                        alchohal.setText(alchohal_one);
                        smoke.setText(smoke_one);
                        marriage.setText(marriage_one);
                        move_abroad.setText(move_abroad_one);




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("id",id);
                data.put("action","getUserProfile");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void likeUser(final String myid, final String uid){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("uid",uid);
                data.put("myid",myid);
                data.put("action","likeUser");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void dislikeUser(final String myid, final String uid){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("uid",uid);
                data.put("myid",myid);
                data.put("action","dislikeUser");
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
    private void unBlockUser(final String myid, final String id){
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
                        unblock.setVisibility(View.GONE);
                        favorite.setVisibility(View.VISIBLE);
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
                data.put("myid",myid);
                data.put("uid",id);
                data.put("action","unblockuser");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
    private void makefav(final String myid, final String id){
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
                        favorite.setVisibility(View.GONE);
                        unfavorite.setVisibility(View.VISIBLE);
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
                data.put("myid",myid);
                data.put("uid",id);
                data.put("action","makeFav");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void unfav(final String myid, final String id){
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
                        unfavorite.setVisibility(View.GONE);
                        favorite.setVisibility(View.VISIBLE);
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
                data.put("myid",myid);
                data.put("uid",id);
                data.put("action","unfavuser");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
}
