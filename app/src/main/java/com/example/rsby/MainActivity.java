package com.example.rsby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rsby.fragments.MainFragment_r;
import com.example.rsby.fragments.MainFragment_u;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button searchBtn;
    Button clearBtn;
    MainFragment_r mainFragment_r;
    MainFragment_u mainFragment_u;
    EditText category_txt;
    EditText city_txt;
    Spinner rating_spinner;
    CheckBox price_1;
    CheckBox price_2;
    CheckBox price_3;
    CheckBox price_4;
    Spinner states_spinner;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        category_txt = findViewById(R.id.category_txt);
        city_txt = findViewById(R.id.city_txt);
        rating_spinner = findViewById(R.id.spinner_rating);
        states_spinner = findViewById(R.id.spinner_states);
        price_1 = findViewById(R.id.price_1);
        price_2 = findViewById(R.id.price_2);
        price_3 = findViewById(R.id.price_3);
        price_4 = findViewById(R.id.price_4);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_search);

//        //Restaurant Recommendation
//        View page_1_view = getLayoutInflater().inflate(R.layout.frag_recycler, null);

        // Slide View
        List<Fragment> fragmentList = new ArrayList<>();
        mainFragment_r = new MainFragment_r();
        mainFragment_u = new MainFragment_u();

        fragmentList.add(mainFragment_r);
        fragmentList.add(mainFragment_u);

        // set up pager
        pager = findViewById(R.id.main_pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fragmentList);

        pager.setAdapter(pagerAdapter);

        //Button onclick
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeybaord(view);

                // Send data from main to fragments
                Bundle bundle = new Bundle();
                bundle.putString("category", String.valueOf(category_txt.getText()));

                if (price_1.isChecked()) {
                    bundle.putString("price", "$");
                } else if (price_2.isChecked()) {
                    bundle.putString("price", "$$");
                } else if (price_3.isChecked()) {
                    bundle.putString("price", "$$$");
                } else if (price_4.isChecked()) {
                    bundle.putString("price", "$$$$");
                }

                bundle.putString("rating", rating_spinner.getSelectedItem().toString());
                bundle.putString("city", String.valueOf(city_txt.getText()));
                bundle.putString("state", states_spinner.getSelectedItem().toString());

                mainFragment_r.setArguments(bundle);
                mainFragment_r.searchUpdate();

                pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 1){
                            mainFragment_u.setRecyclerView(mainFragment_r.returnIDs());
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });

        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_txt.getText().clear();
                city_txt.getText().clear();
                rating_spinner.setAdapter(null);
                states_spinner.setAdapter(null);
                price_1.setChecked(false);
                price_2.setChecked(false);
                price_3.setChecked(false);
                price_4.setChecked(false);

                mainFragment_r.clearResults();
                mainFragment_u.adapterClear();
                pager.setCurrentItem(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_search:
                break;
            case R.id. nav_star:
                Intent intent = new Intent(MainActivity.this, FavActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}