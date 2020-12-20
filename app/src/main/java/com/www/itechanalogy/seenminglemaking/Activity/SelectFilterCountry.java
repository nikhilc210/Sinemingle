package com.www.itechanalogy.seenminglemaking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.print.PrinterId;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.merkmod.achievementtoastlibrary.AchievementToast;
import com.squareup.picasso.Picasso;
import com.www.itechanalogy.seenminglemaking.Helper.Country;
import com.www.itechanalogy.seenminglemaking.Helper.SessionManager;
import com.www.itechanalogy.seenminglemaking.Helper.URLS;
import com.www.itechanalogy.seenminglemaking.List.MyCountryList;
import com.www.itechanalogy.seenminglemaking.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectFilterCountry extends AppCompatActivity {
    private ImageButton back;
    private SessionManager sessionManager;
    private String id;
    private RecyclerView country_list;
    private EditText type;
    private Country country;
    private ArrayList<MyCountryList>countryLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_filter_country);
        checkNetwork();
        getApplicationContext().deleteDatabase("country.sqlite");
        country = new Country(getApplicationContext());
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String>data = sessionManager.getUserDetails();
        id = data.get(SessionManager.KEY_ID);
        country_list = findViewById(R.id.country_list);
        country_list.setHasFixedSize(true);
        country_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        type = findViewById(R.id.type);
        getMySelectedCountry(id);
        type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getCountry();


    }
    private void search(String q){
        countryLists.clear();
        country = new Country(getApplicationContext());
        SQLiteDatabase db = country.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM countryTable WHERE name LIKE '%"+q+"%'",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                MyCountryList cl = new MyCountryList(cursor.getString(0),cursor.getString(1));
                countryLists.add(cl);
                CountryAdapter countryAdapter = new CountryAdapter(countryLists,getApplicationContext());
                country_list.setAdapter(countryAdapter);

            }while (cursor.moveToNext());
        }else{

        }
    }
    private void getCountry(){
        final Dialog dialog = new Dialog(this);
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
                        SQLiteDatabase db = country.getWritableDatabase();
                        country.putData(obj.getString("name"),obj.getString("image"),db);
                        getList();
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
                data.put("action","getCountry");
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getList(){
        Country country = new Country(getApplicationContext());
        SQLiteDatabase db  = country.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM countryTable",null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                MyCountryList cl = new MyCountryList(cursor.getString(0),cursor.getString(1));
                countryLists.add(cl);
                CountryAdapter countryAdapter = new CountryAdapter(countryLists,getApplicationContext());
                country_list.setAdapter(countryAdapter);

            }while (cursor.moveToNext());
        }
    }
    final class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>{
        ArrayList<MyCountryList>myCountryLists;
        Context context;

        public CountryAdapter(ArrayList<MyCountryList> myCountryLists, Context context) {
            this.myCountryLists = myCountryLists;
            this.context = context;
        }

        @NonNull
        @Override
        public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.inner_my_country,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {
            final MyCountryList cl = myCountryLists.get(position);
            if(cl.getImage().length()>0){
                Picasso.get().load(cl.getImage()).into(holder.image);
            }else{

            }
            holder.name.setText(cl.getName());
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCountry(cl.getName(),id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return myCountryLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView name;
            LinearLayout holder;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                holder = itemView.findViewById(R.id.holder);
            }
        }
    }
    private void saveCountry(final String name, final String id){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLS.MY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                AchievementToast.makeAchievement(SelectFilterCountry.this, "Country Filter Updated", AchievementToast.LENGTH_SHORT, getResources().getDrawable(R.drawable.com_facebook_button_like_icon_selected)).show();
                //finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                HashMap<String,String>data = new HashMap<>();
                data.put("action","saveCountryFilter");
                data.put("name",name);
                data.put("id",id);
                return data;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    private void getMySelectedCountry(String id){

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
