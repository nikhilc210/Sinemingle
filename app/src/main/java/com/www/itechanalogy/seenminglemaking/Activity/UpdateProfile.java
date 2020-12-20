package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.MyCountryList;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener{
    private LoopView loopView;
    ArrayList<String> list = new ArrayList<>();
    private int BACK_COUNT = 0;
    private int NEXT_COUNT = 0;
    private RelativeLayout height_container,maritial_status,education_leval,origin,religious_section,pray_box,married_box,halal_section,smoke_section,alchohal_section,children_section,abrod_section,revert_section,aboutyou_section;
    private Button btncontinue;
    private TextView never_married,divorced,separated,annulled,widowed;
    private TextView bachelors_degree,masters_degree,no_degree,collage,doctorate,secondry_school,education_other;
    private TextView very_practising,practising,moderately_practising,not_practising;
    private TextView always_pray,usually_pray,sometimes_pray,never_pray;
    private RecyclerView country_list;
    private TextView as_soon_marrige,one_to_two_year,three_to_four_years,four_plus_years;
    private ArrayList<MyCountryList>myCountryLists = new ArrayList<>();
    private TextView yes_halal,no_halal;
    private TextView yes_smoke,no_smoke;
    private TextView yes_alchol,no_alchol;
    private TextView yes_child,no_child;
    private TextView yes_abroad,no_abroad;
    private TextView yes_revert,no_revert;
    private EditText about;
    private Button btnsave;
    private ImageButton back;
    private SessionManager sessionManager;
    private String myid;
    private String MARATIAL_STATUS = "",EDUCATION="",ORIGIN="",RILIGIOUS="Removed",PRAY="",MARRIGE="", HALAL="",SMOKE="",ALCHOHOL="",CHILD="", ABROAD ="",REVERT = "",HEIGHT="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        checkNetwork();
        loopView = findViewById(R.id.loopView);
        height_container = findViewById(R.id.height_container);
        maritial_status = findViewById(R.id.maritial_status);
        btncontinue = findViewById(R.id.btncontinue);
        never_married = findViewById(R.id.never_married);
        divorced = findViewById(R.id.divorced);
        separated = findViewById(R.id.separated);
        annulled = findViewById(R.id.annulled);
        widowed = findViewById(R.id.widowed);
        education_leval = findViewById(R.id.education_leval);
        bachelors_degree = findViewById(R.id.bachelors_degree);
        masters_degree = findViewById(R.id.masters_degree);
        no_degree = findViewById(R.id.no_degree);
        collage = findViewById(R.id.collage);
        doctorate = findViewById(R.id.doctorate);
        secondry_school = findViewById(R.id.secondry_school);
        education_other = findViewById(R.id.education_other);
        religious_section = findViewById(R.id.religious_section);
        very_practising = findViewById(R.id.very_practising);
        practising = findViewById(R.id.practising);
        moderately_practising = findViewById(R.id.moderately_practising);
        not_practising = findViewById(R.id.not_practising);
        origin = findViewById(R.id.origin);
        pray_box = findViewById(R.id.pray_box);
        always_pray = findViewById(R.id.always_pray);
        usually_pray = findViewById(R.id.usually_pray);
        sometimes_pray = findViewById(R.id.sometimes_pray);
        never_pray = findViewById(R.id.never_pray);
        married_box = findViewById(R.id.married_box);
        as_soon_marrige = findViewById(R.id.as_soon_marrige);
        one_to_two_year = findViewById(R.id.one_to_two_year);
        three_to_four_years = findViewById(R.id.three_to_four_years);
        four_plus_years = findViewById(R.id.four_plus_years);
        halal_section = findViewById(R.id.halal_section);
        yes_halal = findViewById(R.id.yes_halal);
        no_halal = findViewById(R.id.no_halal);
        smoke_section = findViewById(R.id.smoke_section);
        yes_smoke = findViewById(R.id.yes_smoke);
        no_smoke = findViewById(R.id.no_smoke);
        alchohal_section = findViewById(R.id.alchohal_section);
        yes_alchol = findViewById(R.id.yes_alchol);
        no_alchol = findViewById(R.id.no_alchol);
        children_section = findViewById(R.id.children_section);
        yes_child = findViewById(R.id.yes_child);
        no_child = findViewById(R.id.no_child);
        abrod_section = findViewById(R.id.abrod_section);
        yes_abroad = findViewById(R.id.yes_abroad);
        no_abroad = findViewById(R.id.no_abroad);
        revert_section = findViewById(R.id.revert_section);
        yes_revert = findViewById(R.id.yes_revert);
        no_revert = findViewById(R.id.no_revert);
        aboutyou_section = findViewById(R.id.aboutyou_section);
        about = findViewById(R.id.about);
        btnsave = findViewById(R.id.btnsave);
        yes_revert.setOnClickListener(this);
        no_revert.setOnClickListener(this);
        yes_child.setOnClickListener(this);
        no_child.setOnClickListener(this);
        yes_alchol.setOnClickListener(this);
        no_alchol.setOnClickListener(this);
        yes_smoke.setOnClickListener(this);
        no_smoke.setOnClickListener(this);
        always_pray.setOnClickListener(this);
        usually_pray.setOnClickListener(this);
        sometimes_pray.setOnClickListener(this);
        never_pray.setOnClickListener(this);
        very_practising.setOnClickListener(this);
        practising.setOnClickListener(this);
        moderately_practising.setOnClickListener(this);
        not_practising.setOnClickListener(this);
        never_married.setOnClickListener(this);
        divorced.setOnClickListener(this);
        separated.setOnClickListener(this);
        annulled.setOnClickListener(this);
        widowed.setOnClickListener(this);
        bachelors_degree.setOnClickListener(this);
        masters_degree.setOnClickListener(this);
        no_degree.setOnClickListener(this);
        collage.setOnClickListener(this);
        doctorate.setOnClickListener(this);
        secondry_school.setOnClickListener(this);
        education_other.setOnClickListener(this);
        yes_abroad.setOnClickListener(this);
        no_abroad.setOnClickListener(this);
        as_soon_marrige.setOnClickListener(this);
        one_to_two_year.setOnClickListener(this);
        three_to_four_years.setOnClickListener(this);
        four_plus_years.setOnClickListener(this);
        yes_halal.setOnClickListener(this);
        no_halal.setOnClickListener(this);
        country_list = findViewById(R.id.country_list);
        country_list.setHasFixedSize(true);
        back = findViewById(R.id.back);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        myid = data.get(SessionManager.KEY_ID);

        country_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getCountry();
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(getApplicationContext(),HEIGHT,Toast.LENGTH_LONG).show();
                height_container.setVisibility(View.GONE);
                maritial_status.setVisibility(View.VISIBLE);
                maritial_status.setAnimation(inFromRightAnimation());
                btncontinue.setVisibility(View.GONE);
                BACK_COUNT = 1;
                NEXT_COUNT = 1;
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
        loopView.setInitPosition(20);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(myid,MARATIAL_STATUS,EDUCATION,ORIGIN,RILIGIOUS,PRAY,MARRIGE,HALAL,SMOKE,ALCHOHOL,CHILD,ABROAD,REVERT,HEIGHT,about.getText().toString());
                // AchievementToast.makeAchievement(UpdateProfile.this, "Profile Updated", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.com_facebook_button_like_icon_selected)).show();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BACK_COUNT==0){
                    finish();
                }else if(BACK_COUNT==11){
                    aboutyou_section.setVisibility(View.GONE);
                    abrod_section.setVisibility(View.VISIBLE);
                    abrod_section.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 10;
                }else if(BACK_COUNT==10){
                    aboutyou_section.setVisibility(View.GONE);
                    abrod_section.setVisibility(View.VISIBLE);
                    abrod_section.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 9;
                    // Toast.makeText(getApplicationContext(),"10",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==9){
                    abrod_section.setVisibility(View.GONE);
                    children_section.setVisibility(View.VISIBLE);
                    children_section.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 8;
                    // Toast.makeText(getApplicationContext(),"9",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==8){
                    children_section.setVisibility(View.GONE);
                    halal_section.setVisibility(View.VISIBLE);
                    halal_section.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 7;
                    // Toast.makeText(getApplicationContext(),"8",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==7){
                    halal_section.setVisibility(View.GONE);
                    married_box.setVisibility(View.VISIBLE);
                    married_box.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 6;
                    // Toast.makeText(getApplicationContext(),"7",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==6){
                    married_box.setVisibility(View.GONE);
                    pray_box.setVisibility(View.VISIBLE);
                    pray_box.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 4;
                    //  Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==5){
                    pray_box.setVisibility(View.GONE);
                    religious_section.setVisibility(View.VISIBLE);
                    religious_section.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 4;
                    // Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==4){
                    religious_section.setVisibility(View.GONE);
                    origin.setVisibility(View.VISIBLE);
                    origin.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 3;
                    //  Toast.makeText(getApplicationContext(),"4",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==3){
                    origin.setVisibility(View.GONE);
                    education_leval.setVisibility(View.VISIBLE);
                    education_leval.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 2;
                    //  Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==2){
                    education_leval.setVisibility(View.GONE);
                    maritial_status.setVisibility(View.VISIBLE);
                    maritial_status.startAnimation(inFromRightAnimation());
                    BACK_COUNT = 1;
                    //  Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                }else if(BACK_COUNT==1){
                    maritial_status.setVisibility(View.GONE);
                    height_container.setVisibility(View.VISIBLE);
                    height_container.startAnimation(inFromRightAnimation());
                    btncontinue.setVisibility(View.VISIBLE);
                    BACK_COUNT = 0;
                    //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void updateProfile(final String myid, final String MARATIAL_STATUS, final String EDUCATION, final String ORIGIN, final String RILIGIOUS, final String PRAY, final String MARRIGE, final String HALAL, final String SMOKE, final String ALCHOHOL, final String CHILD, final String ABROAD, final String REVERT, final String HEIGHT, final String about){
        Toast.makeText(getApplicationContext(),HEIGHT,Toast.LENGTH_LONG).show();
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
                        AchievementToast.makeAchievement(UpdateProfile.this, "Profile Updated", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.com_facebook_button_like_icon_selected)).show();
                        finish();
                    }else{
                        Log.d("ERROR======>",object.getString("error"));
                        AchievementToast.makeAchievement(UpdateProfile.this, object.getString("error"), AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","updateProfile");
                data.put("MYID",myid);
                data.put("MARATIAL_STATUS",MARATIAL_STATUS);
                data.put("EDUCATION",EDUCATION);
                data.put("ORIGIN",ORIGIN);
                data.put("RILIGIOUS",RILIGIOUS);
                data.put("PRAY",PRAY);
                data.put("MARRIGE",MARRIGE);
                data.put("HALAL",HALAL);
                data.put("SMOKE",SMOKE);
                data.put("ALCHOHOL",ALCHOHOL);
                data.put("CHILD",CHILD);
                data.put("ABROAD",ABROAD);
                data.put("REVERT",REVERT);
                data.put("HEIGHT",HEIGHT);
                data.put("about",about);
                return data;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(300);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.never_married:
                never_married.setBackground(getResources().getDrawable(R.drawable.round_border));
                divorced.setBackground(null);
                separated.setBackground(null);
                annulled.setBackground(null);
                widowed.setBackground(null);
                maritial_status.setVisibility(View.GONE);
                education_leval.setVisibility(View.VISIBLE);
                education_leval.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                MARATIAL_STATUS = "Never Married";
                break;
            case R.id.divorced:
                never_married.setBackground(null);
                divorced.setBackground(getResources().getDrawable(R.drawable.round_border));
                separated.setBackground(null);
                annulled.setBackground(null);
                widowed.setBackground(null);
                maritial_status.setVisibility(View.GONE);
                education_leval.setVisibility(View.VISIBLE);
                education_leval.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                MARATIAL_STATUS = "Divorced";
                break;
            case R.id.separated:
                never_married.setBackground(null);
                divorced.setBackground(null);
                separated.setBackground(getResources().getDrawable(R.drawable.round_border));
                annulled.setBackground(null);
                widowed.setBackground(null);
                maritial_status.setVisibility(View.GONE);
                education_leval.setVisibility(View.VISIBLE);
                education_leval.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                MARATIAL_STATUS = "Separated";
                break;
            case R.id.annulled:
                never_married.setBackground(null);
                divorced.setBackground(null);
                separated.setBackground(null);
                annulled.setBackground(getResources().getDrawable(R.drawable.round_border));
                widowed.setBackground(null);
                maritial_status.setVisibility(View.GONE);
                education_leval.setVisibility(View.VISIBLE);
                education_leval.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                MARATIAL_STATUS = "Annulled";
                break;
            case R.id.widowed:
                never_married.setBackground(null);
                divorced.setBackground(null);
                separated.setBackground(null);
                annulled.setBackground(null);
                widowed.setBackground(getResources().getDrawable(R.drawable.round_border));
                maritial_status.setVisibility(View.GONE);
                education_leval.setVisibility(View.VISIBLE);
                education_leval.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                MARATIAL_STATUS = "Widow";
                break;
            case R.id.bachelors_degree:
                bachelors_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "Bachelors Degree";
                break;
            case R.id.masters_degree:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "Masters Degree";
                break;
            case R.id.no_degree:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(getResources().getDrawable(R.drawable.round_border));
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "No Degree";
                break;
            case R.id.collage:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(getResources().getDrawable(R.drawable.round_border));
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "College";
                break;
            case R.id.doctorate:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(getResources().getDrawable(R.drawable.round_border));
                secondry_school.setBackground(null);
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "Doctorate";
                break;
            case R.id.secondry_school:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(getResources().getDrawable(R.drawable.round_border));
                education_other.setBackground(null);
                education_leval.setVisibility(View.GONE);
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "Secondary School";
                break;
            case R.id.education_other:
                bachelors_degree.setBackground(null);
                masters_degree.setBackground(null);
                no_degree.setBackground(null);
                collage.setBackground(null);
                doctorate.setBackground(null);
                secondry_school.setBackground(null);
                education_other.setBackground(getResources().getDrawable(R.drawable.round_border));
                education_leval.setVisibility(View.GONE);
                origin.setVisibility(View.VISIBLE);
                origin.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                EDUCATION = "Other";
                break;
            case R.id.very_practising:
                very_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                practising.setBackground(null);
                moderately_practising.setBackground(null);
                not_practising.setBackground(null);
                religious_section.setVisibility(View.GONE);
                pray_box.setVisibility(View.VISIBLE);
                pray_box.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                RILIGIOUS = "Very Practising";
                break;
            case R.id.practising:
                very_practising.setBackground(null);
                practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                moderately_practising.setBackground(null);
                not_practising.setBackground(null);
                religious_section.setVisibility(View.GONE);
                pray_box.setVisibility(View.VISIBLE);
                pray_box.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                RILIGIOUS = "Practising";
                break;
            case R.id.moderately_practising:
                very_practising.setBackground(null);
                practising.setBackground(null);
                moderately_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                not_practising.setBackground(null);
                religious_section.setVisibility(View.GONE);
                pray_box.setVisibility(View.VISIBLE);
                pray_box.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                RILIGIOUS = "Moderately Practising";
                break;
            case R.id.not_practising:
                very_practising.setBackground(null);
                practising.setBackground(null);
                moderately_practising.setBackground(null);
                not_practising.setBackground(getResources().getDrawable(R.drawable.round_border));
                religious_section.setVisibility(View.GONE);
                pray_box.setVisibility(View.VISIBLE);
                pray_box.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                RILIGIOUS = "Not Practising";
                break;
            case R.id.always_pray:
                always_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(null);
                never_pray.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                pray_box.setVisibility(View.GONE);
                married_box.setVisibility(View.VISIBLE);
                married_box.setAnimation(inFromRightAnimation());
                PRAY = "Always Pray";
                break;
            case R.id.usually_pray:
                always_pray.setBackground(null);
                usually_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                sometimes_pray.setBackground(null);
                never_pray.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                pray_box.setVisibility(View.GONE);
                married_box.setVisibility(View.VISIBLE);
                married_box.setAnimation(inFromRightAnimation());
                RILIGIOUS = "Usually Pray";
                break;
            case R.id.sometimes_pray:
                always_pray.setBackground(null);
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                never_pray.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                pray_box.setVisibility(View.GONE);
                married_box.setVisibility(View.VISIBLE);
                married_box.setAnimation(inFromRightAnimation());
                RILIGIOUS = "Sometimes Pray";
                break;
            case R.id.never_pray:
                always_pray.setBackground(null);
                usually_pray.setBackground(null);
                sometimes_pray.setBackground(null);
                never_pray.setBackground(getResources().getDrawable(R.drawable.round_border));
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                pray_box.setVisibility(View.GONE);
                married_box.setVisibility(View.VISIBLE);
                married_box.setAnimation(inFromRightAnimation());
                RILIGIOUS = "Never Pray";
                break;
            case R.id.as_soon_marrige:
                as_soon_marrige.setBackground(getResources().getDrawable(R.drawable.round_border));
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                married_box.setVisibility(View.GONE);
                halal_section.setVisibility(View.VISIBLE);
                halal_section.setAnimation(inFromRightAnimation());
                MARRIGE = "As Soon As Possible";

                break;
            case R.id.one_to_two_year:
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(getResources().getDrawable(R.drawable.round_border));
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                married_box.setVisibility(View.GONE);
                halal_section.setVisibility(View.VISIBLE);
                halal_section.setAnimation(inFromRightAnimation());
                MARRIGE = "One to Two Year";
                break;
            case R.id.three_to_four_years:
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(getResources().getDrawable(R.drawable.round_border));
                four_plus_years.setBackground(null);
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                married_box.setVisibility(View.GONE);
                halal_section.setVisibility(View.VISIBLE);
                halal_section.setAnimation(inFromRightAnimation());
                MARRIGE = "Three to Four Year";
                break;
            case R.id.four_plus_years:
                as_soon_marrige.setBackground(null);
                one_to_two_year.setBackground(null);
                three_to_four_years.setBackground(null);
                four_plus_years.setBackground(getResources().getDrawable(R.drawable.round_border));
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                married_box.setVisibility(View.GONE);
                halal_section.setVisibility(View.VISIBLE);
                halal_section.setAnimation(inFromRightAnimation());
                MARRIGE = "Four+ Year";
                break;
            case R.id.yes_halal:
                yes_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_halal.setBackground(null);
                halal_section.setVisibility(View.GONE);
                children_section.setVisibility(View.VISIBLE);
                children_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                HALAL = "yes";
                break;
            case R.id.no_halal:
                no_halal.setBackground(getResources().getDrawable(R.drawable.round_border));
                yes_halal.setBackground(null);
                halal_section.setVisibility(View.GONE);
                children_section.setVisibility(View.VISIBLE);
                children_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                HALAL = "No";
                break;
            case R.id.yes_smoke:
                yes_smoke.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_smoke.setBackground(null);
                smoke_section.setVisibility(View.GONE);
                alchohal_section.setVisibility(View.VISIBLE);
                alchohal_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                SMOKE = "Yes";
                break;
            case R.id.no_smoke:
                yes_smoke.setBackground(null);
                no_smoke.setBackground(getResources().getDrawable(R.drawable.round_border));
                smoke_section.setVisibility(View.GONE);
                alchohal_section.setVisibility(View.VISIBLE);
                alchohal_section.setAnimation(inFromRightAnimation());
                HALAL = "No";
                break;
            case R.id.yes_alchol:
                yes_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_alchol.setBackground(null);
                alchohal_section.setVisibility(View.GONE);
                children_section.setVisibility(View.VISIBLE);
                children_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                ALCHOHOL = "Yes";
                break;
            case R.id.no_alchol:
                yes_alchol.setBackground(null);
                no_alchol.setBackground(getResources().getDrawable(R.drawable.round_border));
                alchohal_section.setVisibility(View.GONE);
                children_section.setVisibility(View.VISIBLE);
                children_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                ALCHOHOL = "No";
                break;
            case R.id.yes_child:
                yes_child.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_child.setBackground(null);
                children_section.setVisibility(View.GONE);
                abrod_section.setVisibility(View.VISIBLE);
                abrod_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                CHILD = "Yes";
                break;
            case R.id.no_child:
                yes_child.setBackground(null);
                no_child.setBackground(getResources().getDrawable(R.drawable.round_border));
                children_section.setVisibility(View.GONE);
                abrod_section.setVisibility(View.VISIBLE);
                abrod_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                CHILD = "No";
                break;
            case R.id.yes_abroad:
                yes_abroad.setBackground(getResources().getDrawable(R.drawable.round_border));
                no_abroad.setBackground(null);
                abrod_section.setVisibility(View.GONE);
                aboutyou_section.setVisibility(View.VISIBLE);
                aboutyou_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                ABROAD = "Yes";
                btnsave.setVisibility(View.VISIBLE);
                break;
            case R.id.no_abroad:
                yes_abroad.setBackground(null);
                no_abroad.setBackground(getResources().getDrawable(R.drawable.round_border));
                abrod_section.setVisibility(View.GONE);
                aboutyou_section.setVisibility(View.VISIBLE);
                aboutyou_section.setAnimation(inFromRightAnimation());
                BACK_COUNT = BACK_COUNT+1;
                NEXT_COUNT = NEXT_COUNT+1;
                ABROAD = "No";
                btnsave.setVisibility(View.VISIBLE);
                break;




        }
    }
    private void getCountry(){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        MyCountryList ml = new MyCountryList(obj.getString("name"),obj.getString("image"));
                        myCountryLists.add(ml);
                    }
                    MyCountryAdapter myCountryAdapter = new MyCountryAdapter(myCountryLists,getApplicationContext());
                    country_list.setAdapter(myCountryAdapter);
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
                data.put("action","getCountry");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private final class MyCountryAdapter extends RecyclerView.Adapter<MyCountryAdapter.MyViewHolder>{
        ArrayList<MyCountryList>myCountryLists;
        Context context;

        public MyCountryAdapter(ArrayList<MyCountryList> myCountryLists, Context context) {
            this.myCountryLists = myCountryLists;
            this.context = context;
        }

        @NonNull
        @Override
        public MyCountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_my_country,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCountryAdapter.MyViewHolder holder, int position) {
            final MyCountryList ml = myCountryLists.get(position);
            if(ml.getImage().length()>0){
                Picasso.get().load(ml.getImage()).into(holder.image);
            }else{

            }
            holder.name.setText(ml.getName());
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pray_box.setVisibility(View.VISIBLE);
                    origin.setVisibility(View.GONE);
                    pray_box.setAnimation(inFromRightAnimation());
                    ORIGIN = ml.getName();
                    BACK_COUNT = BACK_COUNT+1;
                }
            });
        }

        @Override
        public int getItemCount() {
            return myCountryLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout holder;
            ImageView image;
            TextView name;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                holder = itemView.findViewById(R.id.holder);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
            }
        }
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
