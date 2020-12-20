package com.www.itechanalogy.seenminglemaking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Activity.AdminMessage;
import com.www.itechanalogy.seenminglemaking.Activity.CompleteDashboard;
import com.www.itechanalogy.seenminglemaking.Activity.Filters;
import com.www.itechanalogy.seenminglemaking.Activity.Message;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Activity.UnMatched;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.ChatBoxList;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends Fragment {
    View view;
    private Button start_discovering;
    private RelativeLayout edit_filter;
    private ImageButton unmatched;
    private RelativeLayout admin_chat;
    private RecyclerView chat_history;
    private SessionManager sessionManager;
    private String id,name;
    private TextView admin_text;
    private ArrayList<ChatBoxList> chatBoxLists = new ArrayList<>();
    public Chat newInstance(){
        Chat chat = new Chat();
        return chat;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat,container,false);
        checkNetwork();
        start_discovering = view.findViewById(R.id.start_discovering);
        edit_filter = view.findViewById(R.id.edit_filter);
        unmatched = view.findViewById(R.id.unmatched);
        admin_chat = view.findViewById(R.id.admin_chat);
        chat_history = view.findViewById(R.id.chat_history);
        admin_text = view.findViewById(R.id.admin_text);
        chat_history.setHasFixedSize(true);
        chat_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        sessionManager = new SessionManager(getActivity());
        HashMap<String,String> data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        name = data.get(SessionManager.KEY_NAME);
        admin_text.setText("Welcome to SeneMingle "+name);
        getMyChat(id);
        admin_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AdminMessage.class);
                startActivity(i);
            }
        });
        unmatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UnMatched.class);
                startActivity(i);
            }
        });
        start_discovering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CompleteDashboard.class);
                startActivity(i);
            }
        });
        edit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Filters.class);
                startActivity(i);
            }
        });
        return view;
    }
    private void getMyChat(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE========>",response);
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        ChatBoxList cl = new ChatBoxList(id,obj.getString("uid"),obj.getString("uname"),obj.getString("prof"),obj.getString("message"),obj.getString("time"),obj.getString("uimage"),obj.getString("num"));
                        chatBoxLists.add(cl);
                    }
                    ChatBoxAdapter chatBoxAdapter = new ChatBoxAdapter(chatBoxLists,getActivity());
                    chat_history.setAdapter(chatBoxAdapter);
                    if(chatBoxLists.isEmpty()){
                        chat_history.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String>data =new HashMap<>();
                data.put("id",id);
                data.put("action","getChatBox");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    final class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder>{
        ArrayList<ChatBoxList>chatBoxLists;
        Context context;

        public ChatBoxAdapter(ArrayList<ChatBoxList> chatBoxLists, Context context) {
            this.chatBoxLists = chatBoxLists;
            this.context = context;
        }

        @NonNull
        @Override
        public ChatBoxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.chatbox,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatBoxAdapter.MyViewHolder holder, int position) {
            final ChatBoxList cl = chatBoxLists.get(position);
            if(cl.getUimage().length()>0){
                Picasso.get().load(cl.getUimage()).placeholder(R.drawable.placeholder).into(holder.user_image);
            }else{

            }
            holder.user_name.setText(cl.getUname());
            holder.message.setText(cl.getMessage());
            holder.time.setText(cl.getTime());
            holder.prof.setText(cl.getProf());
            if(cl.getNum().length()>0){
                holder.num.setVisibility(View.VISIBLE);
                holder.num.setText(cl.getNum());
            }else{

            }
            holder.time.setText(cl.getTime());
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), Message.class);
                    i.putExtra("name",cl.getUname());
                    i.putExtra("image",cl.getUimage());
                    i.putExtra("uid",cl.getUid());
                    i.putExtra("myid",cl.getMyid());
                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return chatBoxLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout holder;
            CircleImageView user_image;
            TextView user_name,prof,message,time,num;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                holder = itemView.findViewById(R.id.holder);
                user_image = itemView.findViewById(R.id.user_image);
                user_name = itemView.findViewById(R.id.user_name);
                prof = itemView.findViewById(R.id.prof);
                message = itemView.findViewById(R.id.message);
                time = itemView.findViewById(R.id.time);
                num = itemView.findViewById(R.id.num);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkNetwork();
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

    public static class LikedYou {
    }
}
