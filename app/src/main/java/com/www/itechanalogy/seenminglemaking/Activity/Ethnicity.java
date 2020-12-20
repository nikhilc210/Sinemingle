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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ethnicity extends AppCompatActivity {
    private RecyclerView ethenticity;
    private ImageButton back;
    private TextView select_eth;
    private SessionManager sessionManager;
    private String id,MY_ETH = "";
    private ArrayList<SectList>sectLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethnicity);
        checkNetwork();
        ethenticity = findViewById(R.id.ethenticity);
        back = findViewById(R.id.back);
        select_eth = findViewById(R.id.select_eth);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        ethenticity.setHasFixedSize(true);
        ethenticity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getMySelectedEth(id);
        getEthList();

    }
    private void getMySelectedEth(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        MY_ETH = obj.getString("my_eth");
                        select_eth.setText(MY_ETH);
                    }else{
                        MY_ETH = "All";
                        select_eth.setText(MY_ETH);
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
                data.put("action","getMySelectedEth");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getEthList(){
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
                    EthAdapter ethAdapter = new EthAdapter(sectLists,getApplicationContext());
                    ethenticity.setAdapter(ethAdapter);
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
                data.put("action","getEthList");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private final class EthAdapter extends RecyclerView.Adapter<EthAdapter.MyViewHolder>{
        ArrayList<SectList>sectLists;
        Context context;

        public EthAdapter(ArrayList<SectList> sectLists, Context context) {
            this.sectLists = sectLists;
            this.context = context;
        }

        @NonNull
        @Override
        public EthAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_sect,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EthAdapter.MyViewHolder holder, int position) {
            final SectList sl = sectLists.get(position);
            holder.name.setText(sl.getName());
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_eth.setText(sl.getName());
                    updateMyEth(id,sl.getName());
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
    private void updateMyEth(final String id, final String name)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                data.put("action","updatemyEth");
                data.put("id",id);
                data.put("name",name);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMySelectedEth(id);
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
