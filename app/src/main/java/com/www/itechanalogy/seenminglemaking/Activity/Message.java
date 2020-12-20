package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.www.itechanalogy.seenminglemaking.BuildConfig;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.MessageList;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {
    private ImageButton back,send;
    private TextView user_name;
    CircleImageView user_image;
    private String name,image,uid,myid;
    private EditText type_message;
    private RecyclerView chat_history;
    private ArrayList<MessageList> messageLists = new ArrayList<>();
    private TextView blocked;
    private BroadcastReceiver receiver;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        checkNetwork();
        this.receiver = new Receiver();
        registerReceiver(receiver, new IntentFilter(BuildConfig.APPLICATION_ID));
        back = findViewById(R.id.back);
        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        name = getIntent().getExtras().getString("name");
        image = getIntent().getExtras().getString("image");
        uid = getIntent().getExtras().getString("uid");
        myid = getIntent().getExtras().getString("myid");
        type_message = findViewById(R.id.type_message);
        send = findViewById(R.id.send);
        type_message.setHint("Send Message to "+name);
        chat_history = findViewById(R.id.chat_history);
        scrollView = findViewById(R.id.scrollView);
        blocked = findViewById(R.id.blocked);
        chat_history.setHasFixedSize(true);
        chat_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getChat(uid,myid);
        if(image.length()>0){
            Picasso.get().load(image).placeholder(R.drawable.placeholder).into(user_image);
        }else{
            Picasso.get().load(R.drawable.placeholder).into(user_image);
        }
        user_name.setText(name);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type_message.getText().toString().length()>0){
                    sendMessage(myid,uid,type_message.getText().toString());
                }else{

                }
            }
        });


    }
    private class Receiver extends BroadcastReceiver {
        private Receiver() {
        }

        public void onReceive(Context arg0, Intent arg1) {
            String myid = arg1.getExtras().getString("myid");
            String uid = arg1.getExtras().getString("uid");
            String senderid = arg1.getExtras().getString("senderid");
            String message = arg1.getExtras().getString("message");
            String time = arg1.getExtras().getString("time");
            if(senderid.equals(uid)){
                MessageList ml = new MessageList(myid,uid,message,uid,time);
                messageLists.add(ml);
                MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),messageLists);
                chat_history.setAdapter(messageAdapter);
                scrollView.setFocusableInTouchMode(true);
                scrollView.postDelayed(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(130);
                    }
                }, 450);

            }
        }
    }
    private void sendMessage(final String myid, final String uid, final String message){
        type_message.setText("");
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        MessageList ml = new MessageList(myid,uid,message,myid,currentDateTimeString);
        messageLists.add(ml);
        MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),messageLists);
        chat_history.setAdapter(messageAdapter);
        scrollView.setFocusableInTouchMode(true);
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.fullScroll(130);
            }
        }, 450);
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){

                    }else{
                        blocked.setVisibility(View.VISIBLE);
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
                HashMap<String,String> data = new HashMap<>();
                data.put("myid",myid);
                data.put("uid",uid);
                data.put("message",message);
                data.put("action","sendMessage");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getChat(final String uid, final String myid){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        MessageList ml = new MessageList(myid,uid,obj.getString("message"),obj.getString("sender_id"),obj.getString("time"));
                        messageLists.add(ml);
                    }
                    MessageAdapter messageadapter = new MessageAdapter(getApplicationContext(),messageLists);
                    chat_history.setAdapter(messageadapter);
                    scrollView.setFocusableInTouchMode(true);
                    scrollView.postDelayed(new Runnable() {
                        public void run() {
                            scrollView.fullScroll(130);
                        }
                    }, 450);
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
                data.put("myid",myid);
                data.put("uid",uid);
                data.put("action","getMessages");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    final class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
        Context context;
        ArrayList<MessageList>messageLists;

        public MessageAdapter(Context context, ArrayList<MessageList> messageLists) {
            this.context = context;
            this.messageLists = messageLists;
        }

        @NonNull
        @Override
        public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_message,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
            MessageList ml = messageLists.get(position);
            if(ml.getSender_id().equals(myid)){
                holder.my_holder.setVisibility(View.VISIBLE);
                holder.my_message.setText(ml.getMessage());
                holder.my_time.setText(ml.getTime());
            }else{
                holder.user_holder.setVisibility(View.VISIBLE);
                holder.user_message.setText(ml.getMessage());
                holder.user_time.setText(ml.getTime());
            }
        }

        @Override
        public int getItemCount() {
            return messageLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout my_holder,user_holder;
            TextView my_message,my_time,user_message,user_time;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                my_holder = itemView.findViewById(R.id.my_holder);
                user_holder = itemView.findViewById(R.id.user_holder);
                my_message = itemView.findViewById(R.id.my_message);
                my_time = itemView.findViewById(R.id.my_time);
                user_message = itemView.findViewById(R.id.user_message);
                user_time = itemView.findViewById(R.id.user_time);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
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
