package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Filters extends Fragment {
    View view;
    private SessionManager sessionManager;
    private String id;
    private TextView clear_all;
    private ImageButton close;
    private RelativeLayout set_location,select_age,select_sect,ethenticity;
    private TextView location_limit_status,age_limit_status,sect_status,ethnicity_status;
    private Switch burudded_status;
    public static Filters newInstance(){
        Filters filters = new Filters();
        return filters;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_filters,container,false);
        checkNetwork();
        close = view.findViewById(R.id.close);
        set_location = view.findViewById(R.id.set_location);
        select_age = view.findViewById(R.id.select_age);
        select_sect = view.findViewById(R.id.select_sect);
        ethenticity = view.findViewById(R.id.ethenticity);
        clear_all = view.findViewById(R.id.clear_all);
        location_limit_status = view.findViewById(R.id.location_limit_status);
        age_limit_status = view.findViewById(R.id.age_limit_status);
        sect_status = view.findViewById(R.id.sect_status);
        ethnicity_status = view.findViewById(R.id.ethnicity_status);
        burudded_status = view.findViewById(R.id.burudded_status);
        sessionManager = new SessionManager(getActivity());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        getMyFilters(id);
        burudded_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    updateBlur(id,"Yes");
                }else{
                    updateBlur(id,"No");
                }
            }
        });
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFilters(id);
            }
        });
        ethenticity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Ethnicity.class);
                startActivity(i);
            }
        });
        select_sect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SelectSect.class);
                startActivity(i);
            }
        });
        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SetLocation.class);
                startActivity(i);
            }
        });
        select_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SelectAge.class);
                startActivity(i);
            }
        });

        return view;
    }


    private void clearAllFilters(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.names().get(0).equals("success")){
                        getMyFilters(id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","clearAllFilters");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    private void getMyFilters(final String id){
        final Dialog dialog = new Dialog(getActivity());
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
                        String limit_by_distance = obj.getString("limit_by_distance");
                        String limit_by_country = obj.getString("limit_by_country");
                        String sect = obj.getString("sect");
                        String ethenicity = obj.getString("ethenicity");
                        String hide_blurred_image = obj.getString("hide_blurred_image");
                        String aage = obj.getString("age");
                        age_limit_status.setText(aage);
                        location_limit_status.setText(limit_by_distance);
                        sect_status.setText(sect);
                        ethnicity_status.setText(ethenicity);
                        if(hide_blurred_image.equals("Yes")){
                            burudded_status.setChecked(true);
                        }else{
                            burudded_status.setChecked(false);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","getMyFilters");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    private void updateBlur(final String id, final String status){
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
                data.put("action","updateBlur");
                data.put("status",status);
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
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

    @Override
    public void onResume() {
        super.onResume();
        getMyFilters(id);
    }
}
