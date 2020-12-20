package com.www.itechanalogy.seenminglemaking.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.github.florent37.shapeofview.ShapeOfView;
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Activity.CompleteDashboard;
import com.www.itechanalogy.seenminglemaking.Activity.Filters;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Activity.UserProfile;
import com.www.itechanalogy.seenminglemaking.Helper.ExpandableHeightGridView;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.BlockedList;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Blocked extends Fragment {
    private View view;
    private RelativeLayout no_blocked_section,edit_filter;
    private Button start_discovering;
    private ExpandableHeightGridView blocked;
    private SessionManager sessionManager;
    private String id;
    private ArrayList<BlockedList>blockedLists = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inner_blocked,container,false);
        checkNetwork();
        no_blocked_section = view.findViewById(R.id.no_blocked_section);
        sessionManager = new SessionManager(getActivity());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        getBlocked(id);
        edit_filter = view.findViewById(R.id.edit_filter);
        start_discovering = view.findViewById(R.id.start_discovering);
        blocked = view.findViewById(R.id.blocked);
        edit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Filters.class);
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
        return view;
    }
    private void getBlocked(final String id){
        blockedLists.clear();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);

                        BlockedList bl = new BlockedList(id,obj.getString("uid"),obj.getString("uname"),obj.getString("uimage"),obj.getString("age"),obj.getString("country"),obj.getString("flag"),obj.getString("prof"),obj.getString("when"));
                        blockedLists.add(bl);
                        if(blockedLists.size()>0){
                            no_blocked_section.setVisibility(View.GONE);
                        }
                    }
                    final BlockedAdapter blockedAdapter = new BlockedAdapter(blockedLists,getActivity());
                    blocked.setExpanded(true);
                    blocked.setAdapter(blockedAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","getBlockedList");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    final class BlockedAdapter extends BaseAdapter{
        ArrayList<BlockedList>blockedLists;
        Context context;

        public BlockedAdapter(ArrayList<BlockedList> blockedLists, Context context) {
            this.blockedLists = blockedLists;
            this.context = context;
        }

        @Override
        public int getCount() {
            return blockedLists.size();
        }

        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view = LayoutInflater.from(context).inflate(R.layout.inner_fragment,viewGroup,false);
                ShapeOfView holder = view.findViewById(R.id.holder);
                ImageView user_image = view.findViewById(R.id.user_image);
                ImageView flag = view.findViewById(R.id.flag);
                TextView username = view.findViewById(R.id.username);
                TextView prof = view.findViewById(R.id.prof);
                TextView origin = view.findViewById(R.id.origin);
                TextView when = view.findViewById(R.id.when);
                final BlockedList bl = blockedLists.get(i);
                if(bl.getUimage().length()>0){
                    Picasso.get().load(bl.getUimage()).placeholder(R.drawable.placeholder).into(user_image);
                }
                if(bl.getCountry_flag().length()>0){
                    Picasso.get().load(bl.getCountry_flag()).placeholder(R.drawable.placeholder).into(flag);
                }
                username.setText(bl.getUname());
                prof.setText(bl.getProf());
                origin.setText(bl.getCountry());
                when.setText(bl.getWhen());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), UserProfile.class);
                        i.putExtra("id",bl.getUid());
                        i.putExtra("type","Blocked");
                        startActivity(i);
                    }
                });

            }

            return view;
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
}

