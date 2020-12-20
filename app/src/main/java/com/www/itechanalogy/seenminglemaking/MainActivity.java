package com.www.itechanalogy.seenminglemaking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.merkmod.achievementtoastlibrary.AchievementToast;
import com.universalvideoview.UniversalVideoView;
import com.www.itechanalogy.seenminglemaking.Activity.CompleteDashboard;
import com.www.itechanalogy.seenminglemaking.Activity.Help;
import com.www.itechanalogy.seenminglemaking.Activity.Login;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Activity.SignupProfile;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private UniversalVideoView video_view;
    private ImageView info;
    private RelativeLayout continue_email,facebook_login,gmail_login;
    //
    private EditText email,screen_name,password,mobile;
    TextView dob;
    private RelativeLayout submit;
    final boolean NULL = true;
    private int v = 0;
    private RelativeLayout gender_container;
    LinearLayout info_container,male,female,social_container;
    private String PASSWORD = "",DOB="",SCREEN_NAME = "",GENDER = "",MOBILE="";
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private AccountManager accountManager;
    private CallbackManager callbackManager;
    private int RC_SIGN_IN =1;
    GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    SessionManager sessionManager;
    private TextView login;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        methodRequiresTwoPermission();
        video_view = findViewById(R.id.video_view);
        //
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        accountManager = AccountManager.get(this);
        callbackManager = CallbackManager.Factory.create();
        facebook_login = findViewById(R.id.facebook_login);
        gmail_login = findViewById(R.id.gmail_login);
        email = findViewById(R.id.email);
        screen_name = findViewById(R.id.screen_name);
        password = findViewById(R.id.password);
        dob = findViewById(R.id.dob);
        gender_container = findViewById(R.id.gender_container);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        mobile = findViewById(R.id.mobile);
        info_container = findViewById(R.id.info_container);
        social_container = findViewById(R.id.social_container);
        submit = findViewById(R.id.submit);
        sessionManager = new SessionManager(getApplicationContext());
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
        if(sessionManager.isUserLoggedIn()){
            if(isNetworkAvailable(getApplicationContext())){
                Intent i = new Intent(getApplicationContext(), CompleteDashboard.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(getApplicationContext(), NoInternet.class);
                startActivity(i);
            }
        }
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        final GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email","public_profile"));
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accesToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            URL profile_pic = new java.net.URL("http://graph.facebook.com/" + id + "/picture?type=large");
                            String pic = profile_pic.toString();
                            // socialLogin(name,email,pic,"Facebook");
                            Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Facebook Login Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                dob.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                                DOB = dayOfMonth + "-" + (month + 1) + "-" + year;
                            }


                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GENDER = "Male";
                if(GENDER.length()>0){
                    submitSignup(email.getText().toString(),SCREEN_NAME,PASSWORD,MOBILE,DOB,GENDER);
                }
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GENDER = "Female";
                if(GENDER.length()>0){
                    submitSignup(email.getText().toString(),SCREEN_NAME,PASSWORD,MOBILE,DOB,GENDER);
                }
            }
        });
        info = findViewById(R.id.info);
        continue_email = findViewById(R.id.continue_email);
        continue_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v==0){
                    social_container.setVisibility(View.GONE);
                    info_container.setVisibility(View.VISIBLE);
                    info_container.startAnimation(inFromRightAnimation());
                    v = v+1;
                }

//                Intent i = new Intent(getApplicationContext(), ContinueWithEmail.class);
//                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCREEN_NAME = screen_name.getText().toString();
                PASSWORD = password.getText().toString();
                MOBILE = mobile.getText().toString();
                if(v==1){
                    checkEmail(email.getText().toString());
                }else if(v==2){
                    email.setVisibility(View.GONE);
                    screen_name.setVisibility(View.GONE);
                    password.setVisibility(View.VISIBLE);
                    password.startAnimation(inFromRightAnimation());
                    v = v+1;
                }else if(v==3){


                    email.setVisibility(View.GONE);
                    screen_name.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    dob.setVisibility(View.VISIBLE);
                    dob.startAnimation(inFromRightAnimation());
                    v = v+1;

                }else if(v==4){

                    email.setVisibility(View.GONE);
                    screen_name.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    dob.setVisibility(View.GONE);
                    mobile.setVisibility(View.VISIBLE);
                    mobile.startAnimation(inFromRightAnimation());
                    v = v+1;


                }else if(v==5){

                    info_container.setVisibility(View.GONE);
                    gender_container.setVisibility(View.VISIBLE);
                    gender_container.startAnimation(inFromRightAnimation());

                }
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Help.class);
                startActivity(i);
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(sessionManager.isUserLoggedIn()){
            if(isNetworkAvailable(getApplicationContext())){
                Intent i = new Intent(getApplicationContext(), CompleteDashboard.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(getApplicationContext(), NoInternet.class);
                startActivity(i);
            }
        }
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

    }
    @AfterPermissionGranted(100)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Meetafghans wants some permission with you",
                    100, perms);
        }
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
    private void checkEmail(final String email1){
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
                        v = v++;
                        email.setVisibility(View.GONE);
                        screen_name.setVisibility(View.VISIBLE);
                        password.setVisibility(View.GONE);
                        screen_name.startAnimation(inFromRightAnimation());
                        v = v+1;
                    }else{
                        AchievementToast.makeAchievement(MainActivity.this, obj.getString("error"), AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();
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
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> data = new HashMap<>();
                data.put("action","checkEmail");
                data.put("email",email1);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void submitSignup(final String email, final String screen, final String password, final String mobile, final String dob, final String gender){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        final String token = FirebaseInstanceId.getInstance().getToken();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                        Intent i = new Intent(getApplicationContext(), SignupProfile.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }else{
                    //    AchievementToast.makeAchievement(MainActivity.this, object.getString("error"), AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.ic_problem)).show();

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
                data.put("action","doSignup");
                data.put("email",email);
                data.put("screen",screen);
                data.put("password",password);
                data.put("mobile",mobile);
                data.put("dob",dob);
                data.put("gender",gender);
                data.put("device_token",token);
                return data;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }  else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }

    }
    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){

        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("MissingPermission")
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
