package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.SectList;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectSect extends AppCompatActivity {
    private SessionManager sessionManager;
    private String id;
    private ImageButton back;
    private RecyclerView sect;
    private ArrayList<SectList>sectLists = new ArrayList<>();
    private String MY_SECT = "";
    private TextView select_sect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sect);
        checkNetwork();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        back = findViewById(R.id.back);
        select_sect = findViewById(R.id.select_sect);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sect = findViewById(R.id.sect);
        sect.setHasFixedSize(true);
        sect.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getMySelectedSect(id);
        getMySectFilter(id);
    }
    private void getMySelectedSect(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        MY_SECT = obj.getString("sect");
                        select_sect.setText(MY_SECT);
                    }else{
                        MY_SECT = "All";
                        select_sect.setText(MY_SECT);
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
                data.put("action","getMySelectSect");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getMySectFilter(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        SectList sl = new SectList(obj.getString("name"));
                        sectLists.add(sl);
                    }
                    SectAdapter sectAdapter = new SectAdapter(sectLists,getApplicationContext());
                    sect.setAdapter(sectAdapter);
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
                data.put("action","getMyFilterSect");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    final class  SectAdapter extends RecyclerView.Adapter<SectAdapter.MyViewHolder>{
        ArrayList<SectList>sectLists;
        Context context;

        public SectAdapter(ArrayList<SectList> sectLists, Context context) {
            this.sectLists = sectLists;
            this.context = context;
        }

        @NonNull
        @Override
        public SectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_sect,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SectAdapter.MyViewHolder holder, int position) {
            final SectList sl = sectLists.get(position);
            holder.name.setText(sl.getName());
            if(sl.getName().equals(MY_SECT)){

            }
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_sect.setText(sl.getName());
                    saveSect(id,sl.getName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return sectLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout holder;
            TextView name;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                holder = itemView.findViewById(R.id.holder);
                name = itemView.findViewById(R.id.name);

            }
        }
    }
    private void saveSect(final String id, final String name){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
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
                data.put("action","saveMySect");
                data.put("id",id);
                data.put("sect",name);
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