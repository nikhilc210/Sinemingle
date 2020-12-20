package com.www.itechanalogy.seenminglemaking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.www.itechanalogy.seenminglemaking.Activity.NoInternet;
import com.www.itechanalogy.seenminglemaking.Helper.SectionPageAdapter;
import com.www.itechanalogy.seenminglemaking.R;

public class Explore extends Fragment {
    View view;
    private ImageButton user_info;
    private TabLayout tabs;
    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;
    public Explore newInstance(){
        Explore explore = new Explore();
        return explore;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_explore,container,false);
        checkNetwork();
        user_info = view.findViewById(R.id.user_info);
        tabs = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewPager);
        tabs.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        return view;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getActivity().getSupportFragmentManager());
        sectionPageAdapter.addFrament(new LikedYou(),"Liked you");
        sectionPageAdapter.addFrament(new Visited(),"Visited you");
       // sectionPageAdapter.addFrament(new Favouirted(),"Favourite");
        //sectionPageAdapter.addFrament(new Passed(),"Passed");
        sectionPageAdapter.addFrament(new Liked(),"Liked");
        sectionPageAdapter.addFrament(new Blocked(),"Blocked");
        viewPager.setAdapter(sectionPageAdapter);
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
