package com.example.rsby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.rsby.fragments.FavFragment_r;
import com.example.rsby.fragments.FavFragment_u;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity{

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    FavFragment_r favFragment_r;
    FavFragment_u favFragment_u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        tabLayout = findViewById(R.id.tabLayout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Slide View
        List<Fragment> fragmentList = new ArrayList<>();
        favFragment_r = new FavFragment_r();
        favFragment_u = new FavFragment_u();
        fragmentList.add(favFragment_r);
        fragmentList.add(favFragment_u);

        pager = findViewById(R.id.fav_pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fragmentList);

        pager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText("Restaurants");
        tabLayout.getTabAt(1).setText("Users");
    }
}