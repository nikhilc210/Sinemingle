package com.www.itechanalogy.seenminglemaking.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.www.itechanalogy.seenminglemaking.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "meetAfghan";
    private static final String IS_USER_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME  = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "dob";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_IMAGE = "image";
    public SessionManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        this.editor = sharedPreferences.edit();
    }
    public void createUserSession(String id, String name, String email, String mobile, String gender, String dob, String account, String image){
        editor.putBoolean(IS_USER_LOGGED_IN,true);
        editor.putString(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_MOBILE,mobile);
        editor.putString(KEY_GENDER,gender);
        editor.putString(KEY_DOB,dob);
        editor.putString(KEY_ACCOUNT,account);
        editor.putString(KEY_IMAGE,image);
        editor.commit();
    }
    public HashMap<String,String>getUserDetails(){
        HashMap<String,String>user = new HashMap<>();
        user.put(KEY_ID,sharedPreferences.getString(KEY_ID,null ));
        user.put(KEY_NAME,sharedPreferences.getString(KEY_NAME,null));
        user.put(KEY_EMAIL,sharedPreferences.getString(KEY_EMAIL,null));
        user.put(KEY_MOBILE,sharedPreferences.getString(KEY_MOBILE,null));
        user.put(KEY_GENDER,sharedPreferences.getString(KEY_GENDER,null));
        user.put(KEY_DOB,sharedPreferences.getString(KEY_DOB,null));
        user.put(KEY_ACCOUNT,sharedPreferences.getString(KEY_ACCOUNT,null));
        user.put(KEY_IMAGE,sharedPreferences.getString(KEY_IMAGE,null));
        return user;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
    public boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN,false);
    }
    public boolean checkActivityLoggedIn(){
        if(!this.isUserLoggedIn()){
            return false;
        }else{
            return true;
        }
    }
    public void clearData(){
        editor.clear();
        editor.commit();
    }
}

