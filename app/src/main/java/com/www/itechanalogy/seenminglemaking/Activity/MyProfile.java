package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.suke.widget.SwitchButton;
import com.tubitv.ui.TubiLoadingView;
import com.www.itechanalogy.seenminglemaking.Helper.FileUtils;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceSingleBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyProfile extends AppCompatActivity implements UploadStatusDelegate {
    private ImageButton close;
    private ImageView main_image,image_one,image_two,image_three,image_four;
    private SwitchButton blur_switch;
    private RelativeLayout nickname_container,dob_container,gender_container,status_container,about_container,education_container,pref_container,job_container,employer_container,sect_container,convert_container,religiosity_container,praying_container,halal_container,alchohal_container,smoker_container,height_container,maritial_container,children_container,marriage_container,relocation_container;
    private TextView relocation,marriage,children,marital,height,smoker,alcohol,halal,praying,religiosity,convert,sect,employer,job,prof,education,about,status_message,gender,dob,nickname;
    private SessionManager sessionManager;
    private String id,main_image_one,image_one_one,image_two_one,image_three_one,image_four_one,blur_one,relocation_one,marriage_one,children_one,marital_one,height_one,smoker_one,alcohol_one,halal_one,praying_one,religiosity_one,convert_one,sect_one,employer_one,job_one,prof_one,education_one,status_message_one,gender_one,dob_one,nickname_one,about_one;
    private int PICK_IMAGE_ONE = 100,PICK_IMAGE_TWO=101,PICK_IMAGE_THREE=102,PICK_IMAGE_FOUR=103,PICK_IMAGE_FIVE=104;
    private Uri bitmap,finalURI;
    private String finalPath;
    private UploadServiceSingleBroadcastReceiver uploadReceiver;
    private ImageButton remove_one,remove_two,remove_three,remove_four;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        checkNetwork();
        UploadService.NAMESPACE = "www.balaisamajmatrimonial.com.android";
        uploadReceiver = new UploadServiceSingleBroadcastReceiver(this);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        main_image = findViewById(R.id.main_image);
        image_one = findViewById(R.id.image_one);
        image_two = findViewById(R.id.image_two);
        image_three = findViewById(R.id.image_three);
        image_four = findViewById(R.id.image_four);
        blur_switch = findViewById(R.id.blur_switch);
        remove_one = findViewById(R.id.remove_one);
        remove_two = findViewById(R.id.remove_two);
        remove_three = findViewById(R.id.remove_three);
        remove_four = findViewById(R.id.remove_four);
        nickname_container = findViewById(R.id.nickname_container);
        dob_container = findViewById(R.id.dob_container);
        gender_container = findViewById(R.id.gender_container);
        status_container = findViewById(R.id.status_container);
        about_container = findViewById(R.id.about_container);
        education_container = findViewById(R.id.education_container);
        pref_container = findViewById(R.id.pref_container);
        job_container = findViewById(R.id.job_container);
        sect_container = findViewById(R.id.sect_container);
        convert_container = findViewById(R.id.convert_container);
        religiosity_container = findViewById(R.id.religiosity_container);
        praying_container = findViewById(R.id.praying_container);
        halal_container = findViewById(R.id.halal_container);
        employer_container = findViewById(R.id.employer_container);
        alchohal_container = findViewById(R.id.alchohal_container);
        smoker_container = findViewById(R.id.smoker_container);
        height_container = findViewById(R.id.height_container);
        maritial_container = findViewById(R.id.maritial_container);
        children_container = findViewById(R.id.children_container);
        marriage_container = findViewById(R.id.marriage_container);
        relocation_container = findViewById(R.id.relocation_container);
        relocation = findViewById(R.id.relocation);
        marriage = findViewById(R.id.marriage);
        children = findViewById(R.id.children);
        marital = findViewById(R.id.marital);
        height = findViewById(R.id.height);
        smoker = findViewById(R.id.smoker);
        alcohol = findViewById(R.id.alcohol);
        halal = findViewById(R.id.halal);
        praying = findViewById(R.id.praying);
        religiosity = findViewById(R.id.religiosity);
        convert = findViewById(R.id.convert);
        sect = findViewById(R.id.sect);
        employer = findViewById(R.id.employer);
        job = findViewById(R.id.job);
        prof = findViewById(R.id.prof);
        education = findViewById(R.id.education);
        about = findViewById(R.id.about);
        status_message = findViewById(R.id.status_message);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        nickname = findViewById(R.id.nickname);
        loading = findViewById(R.id.loading);
        main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ONE);
            }
        });
        image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TWO);
            }
        });
        image_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_THREE);
            }
        });
        image_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FOUR);
            }
        });
        image_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FIVE);
            }
        });
        getupdatedProfile(id);
        nickname_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NickName.class);
                i.putExtra("status",nickname_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        dob_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DOB.class);
                i.putExtra("status",dob_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        gender_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Gender.class);
                i.putExtra("status",gender_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        status_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Status.class);
                i.putExtra("status",status_message_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });

        about_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), About.class);
                i.putExtra("status",about_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        education_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Education.class);
                i.putExtra("status",education_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        pref_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Proffesion.class);
                i.putExtra("status",prof_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        job_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Job.class);
                i.putExtra("status",job_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        employer_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Employer.class);
                i.putExtra("status",employer_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        sect_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Sect.class);
                i.putExtra("status",sect_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        convert_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Convert.class);
                i.putExtra("status",sect_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        religiosity_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Religion.class);
                i.putExtra("status",religiosity_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        praying_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Praying.class);
                i.putExtra("status",praying_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        halal_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Halal.class);
                i.putExtra("status",halal_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        alchohal_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Alchohal.class);
                i.putExtra("status",alcohol_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        smoker_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Smoker.class);
                i.putExtra("status",smoker_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        height_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Height.class);
                i.putExtra("status",height_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        maritial_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Maritial.class);
                i.putExtra("status",marital_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        children_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Children.class);
                i.putExtra("status",children_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        marriage_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Marriage.class);
                i.putExtra("status",marriage_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        relocation_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Relocation.class);
                i.putExtra("status",relocation_one);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        remove_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(id,"One");
                Picasso.get().load(R.drawable.placeholder).into(image_one);
            }
        });
        remove_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(id,"Two");
                Picasso.get().load(R.drawable.placeholder).into(image_two);
            }
        });
        remove_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(id,"Three");
                Picasso.get().load(R.drawable.placeholder).into(image_three);
            }
        });
        remove_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(id,"Four");
                Picasso.get().load(R.drawable.placeholder).into(image_four);
            }
        });


    }
    private void deleteImage(final String id, final String which){
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
                data.put("action","removeUserProfileimages");
                data.put("uid",id);
                data.put("which",which);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getupdatedProfile(final String id){

        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        main_image_one = obj.getString("main_image");
                        image_one_one = obj.getString("image_one");
                        image_two_one = obj.getString("image_two");
                        image_three_one = obj.getString("image_three");
                        image_four_one = obj.getString("image_four");
                        blur_one = obj.getString("blur");
                        relocation_one = obj.getString("relocation");
                        marriage_one = obj.getString("marriage");
                        children_one = obj.getString("children");
                        marital_one = obj.getString("marital");
                        height_one = obj.getString("height");
                        smoker_one = obj.getString("smoker");
                        alcohol_one = obj.getString("alcohol");
                        halal_one = obj.getString("halal");
                        praying_one = obj.getString("praying");
                        religiosity_one = obj.getString("religiosity");
                        convert_one = obj.getString("convert");
                        sect_one = obj.getString("sect");
                        employer_one = obj.getString("employer");
                        job_one = obj.getString("job");
                        prof_one = obj.getString("prof");
                        education_one = obj.getString("education");
                        status_message_one = obj.getString("status_message");
                        gender_one = obj.getString("gender");
                        dob_one = obj.getString("dob");
                        nickname_one = obj.getString("nickname");
                        about_one = obj.getString("about");
                        if(main_image_one.length()>0){
                            Picasso.get().load(main_image_one).placeholder(R.drawable.placeholder).into(main_image);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(main_image);
                        }

                        if(image_two_one.length()>0){
                            Picasso.get().load(image_two_one).placeholder(R.drawable.placeholder).into(image_two);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(image_two);
                        }
                        if(image_one_one.length()>0){
                            Picasso.get().load(image_one_one).placeholder(R.drawable.placeholder).into(image_one);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(image_one);
                        }
                        if(image_three_one.length()>0){
                            Picasso.get().load(image_three_one).placeholder(R.drawable.placeholder).into(image_three);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(image_three);
                        }
                        if(image_four_one.length()>0){
                            Picasso.get().load(image_four_one).placeholder(R.drawable.placeholder).into(image_four);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(image_three);
                        }
                        if(blur_one.equals("Yes")){
                            blur_switch.setChecked(true);
                        }else{
                            blur_switch.setChecked(false);
                        }
                        relocation.setText(relocation_one);
                        marriage.setText(marriage_one);
                        children.setText(children_one);
                        marital.setText(marital_one);
                        height.setText(height_one);
                        smoker.setText(smoker_one);
                        alcohol.setText(alcohol_one);
                        halal.setText(halal_one);
                        praying.setText(praying_one);
                        religiosity.setText(religiosity_one);
                        convert.setText(convert_one);
                        sect.setText(sect_one);
                        employer.setText(employer_one);
                        job.setText(job_one);
                        prof.setText(prof_one);
                        education.setText(education_one);
                        status_message.setText(status_message_one);
                        gender.setText(gender_one);
                        dob.setText(dob_one);
                        nickname.setText(nickname_one);
                        about.setText(about_one);





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
                data.put("action","getUpdatedProfile");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_ONE && resultCode == RESULT_OK) {
            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            Picasso.get().load(bitmap).into(main_image);
            finalURI = bitmap;
            upload("Main",finalPath,id);
        }else if(requestCode==PICK_IMAGE_TWO && resultCode == RESULT_OK){
            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            Picasso.get().load(bitmap).into(image_one);
            finalURI = bitmap;
            upload("One",finalPath,id);
        }else if(requestCode==PICK_IMAGE_THREE && resultCode==RESULT_OK){
            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            Picasso.get().load(bitmap).into(image_two);
            finalURI = bitmap;
            upload("Two",finalPath,id);
        }else if(requestCode==PICK_IMAGE_FOUR && resultCode==RESULT_OK){
            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            Picasso.get().load(bitmap).into(image_three);
            finalURI = bitmap;
            upload("Three",finalPath,id);
        }else if(requestCode==PICK_IMAGE_FIVE && resultCode==RESULT_OK){
            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            Picasso.get().load(bitmap).into(image_four);
            finalURI = bitmap;
            upload("Four",finalPath,id);
        }
    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {

    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {

    }
    private void upload(String which, String image, String id){
        if(which.equals("Main")){

            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                        .addFileToUpload(image, "file") //Adding file
                        .addParameter("id", id)
                        .addParameter("action", "MainImage")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(which.equals("One")){

            remove_one.setVisibility(View.VISIBLE);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                        .addFileToUpload(image, "file") //Adding file
                        .addParameter("id", id)
                        .addParameter("action", "FirstImage")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

//                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(i);
//                            finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(which.equals("Two")){

            remove_two.setVisibility(View.VISIBLE);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                        .addFileToUpload(image, "file") //Adding file
                        .addParameter("id", id)
                        .addParameter("action", "TwoImage")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

//                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(i);
//                            finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(which.equals("Three")){

            remove_three.setVisibility(View.VISIBLE);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                        .addFileToUpload(image, "file") //Adding file
                        .addParameter("id", id)
                        .addParameter("action", "ThirdImage")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

//                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(i);
//                            finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(which.equals("Four")){

            remove_four.setVisibility(View.VISIBLE);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                        .addFileToUpload(image, "file") //Adding file
                        .addParameter("id", id)
                        .addParameter("action", "FourImage")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                remove_four.setVisibility(View.GONE);
//                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(i);
//                            finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
        getupdatedProfile(id);
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