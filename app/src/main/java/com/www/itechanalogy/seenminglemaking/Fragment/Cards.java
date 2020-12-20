package com.www.itechanalogy.seenminglemaking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Activity.Message;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Activity.UserInfo;
import com.www.itechanalogy.seenminglemaking.Activity.UserProfile;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.SwipeFlingAdapterView;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.CardsList;
import com.www.itechanalogy.seenminglemaking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cards extends Fragment {
    View view;
    private ImageButton user_info;
    private SessionManager sessionManager;
    private String id,self_image;
    private SwipeFlingAdapterView swipeLayout;
    private List<CardsList> cardsLists = new ArrayList<>();
    private ImageButton like,dislike;
    private arrayAdapter arrayAdapter;
    private TextView title;
    private Typeface ttf1;
    private RelativeLayout liked_container;
    private CircleImageView my_image,user_image;
    private TextView message;
    private Button message_user,keep_swipe;
    public static Cards newInstance(){
        Cards cards = new Cards();
        return cards;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cards,container,false);
        checkNetwork();
        user_info = view.findViewById(R.id.user_info);
        swipeLayout = view.findViewById(R.id.swipeView);
        sessionManager = new SessionManager(getActivity());
        HashMap<String,String> data = sessionManager.getUserDetails();
        like = view.findViewById(R.id.like);
        dislike = view.findViewById(R.id.dislike);

        id = data.get(SessionManager.KEY_ID);
        self_image = data.get(SessionManager.KEY_IMAGE);
        title = view.findViewById(R.id.title);
        liked_container = view.findViewById(R.id.liked_container);
        my_image = view.findViewById(R.id.my_image);
        user_image = view.findViewById(R.id.user_image);
        message = view.findViewById(R.id.message);
        message_user = view.findViewById(R.id.message_user);
        keep_swipe = view.findViewById(R.id.keep_swipe);
        ttf1 = Typeface.createFromAsset(getActivity().getAssets(),"matchmaker.ttf");
        title.setTypeface(ttf1);
        getProfiles(id);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(cardsLists.size()>0){
                 swipeLayout.getTopCardListener().selectRight();
             }else{

             }
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardsLists.size()>0){
                    swipeLayout.getTopCardListener().selectLeft();
                }else{

                }

            }
        });
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInfo.class);
                startActivity(i);
            }
        });
        return view;
    }
    private void getProfiles(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length(); i++){

                        JSONObject obj = array.getJSONObject(i);
                        Log.d("Response=======>",obj.toString());
                        //cardsLists.clear();
                        CardsList cl = new CardsList(id,obj.getString("uid"),obj.getString("uname"),obj.getString("uimage"),obj.getString("age"),obj.getString("country"),obj.getString("country_flag"),obj.getString("prof"),obj.getString("about"),obj.getString("current_status"),obj.getString("already_liked"));
                        cardsLists.add(cl);
                    }
                    //  arrayAdapter = new ArrayAdapter<>(this, R.layout.item,R.id.user_name, cardsLists );
                    if(cardsLists.size()>0){

                    }else{
                        like.setVisibility(View.GONE);
                        dislike.setVisibility(View.GONE);
                    }
                    if(cardsLists.size()>0){
                        arrayAdapter = new arrayAdapter(getActivity(),R.id.fragment_container,cardsLists);
                        swipeLayout.setAdapter(arrayAdapter);
                    }

                    swipeLayout.setMaxVisible(4);
                    arrayAdapter.notifyDataSetChanged();
                    checkAdapter();
                    swipeLayout.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                        @Override
                        public void removeFirstObjectInAdapter() {
                            Log.d("LIST", "removed object!");
                            cardsLists.remove(0);
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onLeftCardExit(Object dataObject) {
                            CardsList obj = (CardsList)dataObject;
                            String uid = obj.getUid();
                            String myid = obj.getMyid();
                            dislikeUser(uid,myid);
                            checkAdapter();
                            //  cardsLists.remove(arrayAdapter.getPosition(obj));

                        }

                        @Override
                        public void onRightCardExit(Object dataObject) {
                            final CardsList obj = (CardsList)dataObject;
                            String uid = obj.getUid();
                            String myid = obj.getMyid();
                            if(obj.getAlready_liked().equals("Yes")){
                                MediaPlayer player = MediaPlayer.create(getActivity(), R.raw.match);
                                player.start();
                                sendNotificationToUser(id,obj.getUid());
                                liked_container.setVisibility(View.VISIBLE);
                                if(obj.getUimage().length()>0){
                                    Picasso.get().load(obj.getUimage()).placeholder(R.drawable.placeholder).into(user_image);
                                }else{
                                    Picasso.get().load(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(user_image);
                                }
                                Picasso.get().load(self_image).into(my_image);
                                message.setText("You and "+obj.getUname()+" have like each other");
                                message_user.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        liked_container.setVisibility(View.GONE);
                                        Intent i = new Intent(getActivity(), Message.class);
                                        i.putExtra("name",obj.getUname());
                                        i.putExtra("image",obj.getUimage());
                                        i.putExtra("uid",obj.getUid());
                                        i.putExtra("myid",id);
                                        startActivity(i);
                                    }
                                });
                                keep_swipe.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        liked_container.setVisibility(View.GONE);
                                    }
                                });
                            }
                            likeUser(uid,myid);
                            checkAdapter();

                            // cardsLists.remove(arrayAdapter.getPosition(obj));
                        }

                        @Override
                        public void onAdapterAboutToEmpty(int itemsInAdapter) {

                        }

                        @Override
                        public void onScroll(float scrollProgressPercent) {
                            View view = swipeLayout.getSelectedView();
                            view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                            view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

                        }
                    });
                    if(swipeLayout.getAdapter() != null){
                        // Toast.makeText(getApplicationContext(),String.valueOf(arrayAdapter.getCount()),Toast.LENGTH_LONG).show();
                    }else{
                        // Toast.makeText(getApplicationContext(),"Not Setted",Toast.LENGTH_LONG).show();
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
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String>data = new HashMap<>();
                data.put("action","getAllProfiles");
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    private void checkAdapter() {
//       arrayAdapter arrayAdapter = new arrayAdapter(getApplicationContext(), R.id.fragment_container, cardsLists);
//        if (arrayAdapter.getCount() == 0) {
////            like.setVisibility(View.GONE);
////            dislike.setVisibility(View.GONE);
//        } else {
//
//        }
        if(arrayAdapter.isEmpty()){
            like.setVisibility(View.GONE);
            dislike.setVisibility(View.GONE);
        }else{
            like.setVisibility(View.VISIBLE);
            dislike.setVisibility(View.VISIBLE);
        }

    }
    private void likeUser(final String myid, final String uid){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        //  finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("uid",myid);
                data.put("myid",uid);
                data.put("action","likeUser");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    private void dislikeUser(final String myid, final String uid){
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.names().get(0).equals("success")){
                        // finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("uid",myid);
                data.put("myid",uid);
                data.put("action","dislikeUser");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
    public class arrayAdapter extends ArrayAdapter<CardsList> {


        public arrayAdapter(Context context, int resourceId, List<CardsList> items){
            super(context, resourceId, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final CardsList cardsList = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
                ImageView user_image = convertView.findViewById(R.id.user_image);
                TextView user_name = convertView.findViewById(R.id.user_name);
                TextView user_age = convertView.findViewById(R.id.user_age);
                TextView prof = convertView.findViewById(R.id.prof);
                ImageView country_flag = convertView.findViewById(R.id.country_flag);
                TextView country = convertView.findViewById(R.id.country);
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.abril_fatface);
                TextView item_swipe_left_indicator = convertView.findViewById(R.id.item_swipe_left_indicator);
                TextView item_swipe_right_indicator = convertView.findViewById(R.id.item_swipe_right_indicator);
                RelativeLayout instant = convertView.findViewById(R.id.instant);
                RelativeLayout holder = convertView.findViewById(R.id.holder);
                item_swipe_left_indicator.setTypeface(typeface);
                item_swipe_right_indicator.setTypeface(typeface);
                user_name.setTypeface(typeface);
                user_age.setTypeface(typeface);
                instant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), Message.class);
                        i.putExtra("name",cardsList.getUname());
                        i.putExtra("image",cardsList.getUimage());
                        i.putExtra("uid",cardsList.getUid());
                        i.putExtra("myid",cardsList.getMyid());
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), UserProfile.class);
                        i.putExtra("id",cardsList.getUid());
                        i.putExtra("type","profile");
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(i);
                    }
                });

                if(cardsList.getUimage().length()>0){
                    Picasso.get().load(cardsList.getUimage()).into(user_image);
                }
                user_name.setText(cardsList.getUname()+", ");
                user_age.setText(cardsList.getAge());
                prof.setText(cardsList.getProffesion());
                if(cardsList.getCountry_flag().length()>0){
                    Picasso.get().load(cardsList.getCountry_flag()).into(country_flag);
                }
                country.setText(cardsList.getCountry());


            }
            return convertView;
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
    private void sendNotificationToUser(final String myid, final String user_id){
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
                data.put("myid",myid);
                data.put("uid",user_id);
                data.put("action","sendMatchNotification");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}

