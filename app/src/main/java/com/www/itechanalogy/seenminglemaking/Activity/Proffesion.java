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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.ProfList;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Proffesion extends AppCompatActivity {
    private String id,status;
    private ImageButton close;
    private RecyclerView prof_list;
    private TextView my_selected;
    private ArrayList<ProfList>profLists = new ArrayList<>();
    private Button update;
    private EditText my_prof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proffesion);
        checkNetwork();
        id = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getString("status");
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prof_list = findViewById(R.id.prof_list);
        my_selected = findViewById(R.id.my_selected);
        prof_list.setHasFixedSize(true);
        prof_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if(status.length()>0){
            my_selected.setText(status);
        }else{
            my_selected.setText("None");
        }
        my_prof = findViewById(R.id.my_prof);
        update = findViewById(R.id.update);
        my_prof.setText(status);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(my_prof.getText().toString().length()>0){
                    updateProf(id,my_prof.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Please write something",Toast.LENGTH_LONG).show();
                }
            }
        });
        getProf();
    }
    private void getProf(){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        ProfList pl = new ProfList(obj.getString("name"));
                        profLists.add(pl);
                    }
                    ProfAdapter adapter = new ProfAdapter(profLists,getApplicationContext());
                    prof_list.setAdapter(adapter);
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
                data.put("action","getProf");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    final class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.MyViewHolder>{
        ArrayList<ProfList>profLists;
        Context context;

        public ProfAdapter(ArrayList<ProfList> profLists, Context context) {
            this.profLists = profLists;
            this.context = context;
        }

        @NonNull
        @Override
        public ProfAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_prof,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfAdapter.MyViewHolder holder, int position) {
            final ProfList pl = profLists.get(position);
            holder.prof.setText(pl.getName());
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    my_selected.setText(pl.getName());

                }
            });
        }

        @Override
        public int getItemCount() {
            return profLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout holder;
            TextView prof;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                holder = itemView.findViewById(R.id.holder);
                prof = itemView.findViewById(R.id.prof);
            }
        }
    }
    private void updateProf(final String id, final String prof){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
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
                data.put("prof",prof);
                data.put("action","updateProf");
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
