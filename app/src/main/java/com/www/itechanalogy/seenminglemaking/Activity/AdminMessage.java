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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.AdminMessageList;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMessage extends AppCompatActivity {
    private RecyclerView admin_messages;
    private SessionManager sessionManager;
    private String myid;
    private ArrayList<AdminMessageList>adminMessageLists = new ArrayList<>();
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        myid = data.get(SessionManager.KEY_ID);
        admin_messages = findViewById(R.id.admin_messages);
        back = findViewById(R.id.back);
        admin_messages.setHasFixedSize(true);
        admin_messages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAdminMessage(myid);
        checkNetwork();
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
    private void getAdminMessage(String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        AdminMessageList aml = new AdminMessageList(id,obj.getString("mid"),obj.getString("message"),obj.getString("time"));
                        adminMessageLists.add(aml);
                    }
                    AdminMessageAdapter adminMessageAdapter = new AdminMessageAdapter(adminMessageLists,getApplicationContext());
                    admin_messages.setAdapter(adminMessageAdapter);
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
                data.put("action","getAdminMessage");
                data.put("myid",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    final class AdminMessageAdapter extends RecyclerView.Adapter<AdminMessageAdapter.MyViewHolder>{
        ArrayList<AdminMessageList>adminMessageLists;
        Context context;

        public AdminMessageAdapter(ArrayList<AdminMessageList> adminMessageLists, Context context) {
            this.adminMessageLists = adminMessageLists;
            this.context = context;
        }

        @NonNull
        @Override
        public AdminMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_admin_message,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminMessageAdapter.MyViewHolder holder, int position) {
            AdminMessageList al = adminMessageLists.get(position);
            holder.message.setText(al.getMessage());
            holder.time.setText(al.getTime());
        }

        @Override
        public int getItemCount() {
            return adminMessageLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView message,time;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.message);
                time = itemView.findViewById(R.id.time);

            }
        }
    }
}