package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class UpdateProfilePicture extends AppCompatActivity implements UploadStatusDelegate {
    private TextView add_photo,changeImage;
    private ImageView image;
    private static final int PICK_IMAGE = 100;
    private Uri bitmap,finalURI;
    private Button save_photo;
    private String email;
    private String finalPath;
    private UploadServiceSingleBroadcastReceiver uploadReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_picture);
        checkNetwork();
        UploadService.NAMESPACE = "www.balaisamajmatrimonial.com.android";
        uploadReceiver = new UploadServiceSingleBroadcastReceiver(this);
        email = getIntent().getExtras().getString("email");
        add_photo = findViewById(R.id.add_photo);
        changeImage = findViewById(R.id.changeImage);
        image = findViewById(R.id.image);
        save_photo = findViewById(R.id.save_photo);
        save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload(email, finalPath);
            }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });

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
    private void openGallary(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            bitmap = data.getData();
            finalPath = FileUtils.getPath(getApplicationContext(), bitmap);
            add_photo.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            changeImage.setVisibility(View.VISIBLE);
            Picasso.get().load(bitmap).into(image);
            finalURI = bitmap;
        }
    }
    private void upload(final String email, String file){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, URLS.MY_URL)
                    .addFileToUpload(file, "file") //Adding file
                    .addParameter("email", email)
                    .addParameter("action", "uploadProfile")
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
                            dialog.dismiss();
                            getMyProfile(email);
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
    private void getMyProfile(final String email){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    String id = obj.getString("id");
                    String name = obj.getString("name");
                    String email = obj.getString("email");
                    String mobile = obj.getString("mobile");
                    String gender = obj.getString("gender");
                    String dob = obj.getString("dob");
                    String account = "";
                    String image = obj.getString("image");
                    sessionManager.clearData();
                    sessionManager.createUserSession(id,name,email,mobile,gender,dob,account,image);
                    Toast.makeText(getApplicationContext(),"Profile Picture Updated",Toast.LENGTH_LONG).show();
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
                data.put("action","getMyProfile");
                data.put("email",email);
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