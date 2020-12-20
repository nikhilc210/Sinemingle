package com.www.itechanalogy.seenminglemaking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Activity.Filters;
import com.www.itechanalogy.seenminglemaking.Activity.Help;
import com.www.itechanalogy.seenminglemaking.Activity.MainSettings;
import com.www.itechanalogy.seenminglemaking.Activity.MyProfile;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Activity.ReportProblem;
import com.www.itechanalogy.seenminglemaking.Activity.UpdateProfile;
import com.www.itechanalogy.seenminglemaking.Activity.UpdateProfilePicture;
import com.www.itechanalogy.seenminglemaking.Activity.UserInfo;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends Fragment {
    View view;
    private ImageButton user_info,edit_profile;
    private TextView is_incomplete,first_name,tap_to_complete;
    private CircleImageView user_image;
    private ImageButton filters,help_center,settings,report_problem,change;
    private SessionManager sessionManager;
    private String uname,uid,uimage,email;
    private boolean IS_CHECKED = false;
    public Setting newInstance(){
        Setting setting = new Setting();
        return setting;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_settings,container,false);
        checkNetwork();

        user_info = view.findViewById(R.id.user_info);
        is_incomplete = view.findViewById(R.id.is_incomplete);
        first_name = view.findViewById(R.id.first_name);
        tap_to_complete = view.findViewById(R.id.tap_to_complete);
        user_image = view.findViewById(R.id.user_image);
        filters = view.findViewById(R.id.filters);
        help_center = view.findViewById(R.id.help_center);
        is_incomplete = view.findViewById(R.id.is_incomplete);
        first_name = view.findViewById(R.id.first_name);
        tap_to_complete = view.findViewById(R.id.tap_to_complete);
        settings = view.findViewById(R.id.settings);
        edit_profile = view.findViewById(R.id.edit_profile);
        report_problem = view.findViewById(R.id.report_problem);
        change = view.findViewById(R.id.change);
        sessionManager = new SessionManager(getActivity());
        HashMap<String,String> data = sessionManager.getUserDetails();
        uname = data.get(SessionManager.KEY_NAME);
        uid = data.get(SessionManager.KEY_ID);
        email = data.get(SessionManager.KEY_EMAIL);
        getStatus(uid);
        getUserImage(uid);
        uimage = data.get(SessionManager.KEY_IMAGE);
        Log.d("IMAGE=========>","IM"+uimage);
//        if(uimage.length()>0){
//            Picasso.get().load(uimage).placeholder(R.drawable.placeholder).into(user_image);
//        }else{
//            //Picasso.get().load(uimage).placeholder(R.drawable.blocked).into(user_image);
//        }
        first_name.setText(uname);
        report_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ReportProblem.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainSettings.class);
                startActivity(i);
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IS_CHECKED){
                    Intent i = new Intent(getActivity(), MyProfile.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getActivity(), UpdateProfile.class);
                    startActivity(i);
                }
            }
        });
        tap_to_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IS_CHECKED){
                    Intent i = new Intent(getActivity(), MyProfile.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getActivity(), UpdateProfile.class);
                    startActivity(i);
                }
            }
        });
        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Filters.class);
                startActivity(i);
            }
        });
        help_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Help.class);
                startActivity(i);
            }
        });
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInfo.class);
                startActivity(i);
            }
        });
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UpdateProfilePicture.class);
                i.putExtra("email",email);
                i.putExtra("image",uimage);
                startActivity(i);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UpdateProfilePicture.class);
                i.putExtra("email",email);
                i.putExtra("image",uimage);
                startActivity(i);
            }
        });

        return view;
    }
    private void getStatus(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        String status = obj.getString("status");
                        if(status.equals("Yes")){
                            IS_CHECKED = true;
                            tap_to_complete.setText("Tap to edit");
                            is_incomplete.setVisibility(View.GONE);
                        }else{
                            IS_CHECKED = false;
                            tap_to_complete.setVisibility(View.VISIBLE);
                            tap_to_complete.setText("Tap to Complete");
                            is_incomplete.setVisibility(View.VISIBLE);
                        }
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
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String>data = new HashMap<>();
                data.put("id",id);
                data.put("action","getProfileStatus");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    @Override
    public void onResume() {
        super.onResume();
        checkNetwork();

        sessionManager = new SessionManager(getActivity());
        HashMap<String,String> data = sessionManager.getUserDetails();
        uname = data.get(SessionManager.KEY_NAME);
        uid = data.get(SessionManager.KEY_ID);
        email = data.get(SessionManager.KEY_EMAIL);
        getStatus(uid);
        getUserImage(uid);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private void checkNetwork(){
        if(isNetworkAvailable(getActivity())){

        }else{
            Intent i = new Intent(getActivity(), NoInternet.class);
            startActivity(i);
        }
    }
    private void getUserImage(String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                        uimage = object.getString("image");
                        if(uimage.length()>0){
                            Picasso.get().load(uimage).placeholder(R.drawable.placeholder).into(user_image);
                        }else{
                            Picasso.get().load(R.drawable.placeholder).into(user_image);
                        }
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
                data.put("action","getUserProfilePic");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
