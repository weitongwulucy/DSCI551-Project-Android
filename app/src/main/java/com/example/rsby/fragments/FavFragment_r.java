package com.example.rsby.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rsby.R;
import com.example.rsby.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavFragment_r extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    List<String> s_name = new ArrayList<String>();
    List<String> s_loc = new ArrayList<String>();
    List<String> s_img = new ArrayList<String>();
    List<String> s_id = new ArrayList<String>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fav_frag_recycler, container, false);

        recyclerView = rootView.findViewById(R.id.fav_recyclerview);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadList();

        adapter = new RecyclerViewAdapter(this.getActivity(), s_name, s_loc, s_img, s_id, R.layout.restaurant_detail, R.id.r_row_item_id, R.layout.r_row, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    public void loadList() {
        s_name.clear();
        s_loc.clear();
        s_img.clear();
        s_id.clear();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        Set<String> set_restaurants = prefs.getStringSet("restaurants", new HashSet<String>());

        List<String> restaurants = new ArrayList<String>(set_restaurants);

        for (int i=0;i<restaurants.size();i++){
            String r = restaurants.get(i);
            List<String> r_data = Arrays.asList(r.split(","));
            s_name.add(r_data.get(0));
            s_loc.add(r_data.get(1));
            s_img.add(r_data.get(2));
            s_id.add(r_data.get(3));
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadList();
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
