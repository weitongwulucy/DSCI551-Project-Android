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
import com.example.rsby.UserRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavFragment_u extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<String> s_name = new ArrayList<String>();
    List<String> s_avgs = new ArrayList<String>();
    List<String> s_fans = new ArrayList<String>();
    List<String> s_website = new ArrayList<String>();
    UserRecyclerViewAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fav_frag_recycler, container, false);

        recyclerView = rootView.findViewById(R.id.fav_recyclerview);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadList();

        adapter = new UserRecyclerViewAdapter(this.getActivity(), s_name, s_avgs, s_fans, s_website, R.id.u_row_item_id, R.layout.u_row, true);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public void loadList(){
        s_name.clear();
        s_avgs.clear();
        s_fans.clear();
        s_website.clear();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        Set<String> set_users = prefs.getStringSet("users", new HashSet<String>());

        List<String> users = new ArrayList<String>(set_users);

        for (int i=0;i<users.size();i++){
            String user = users.get(i);
            List<String> user_data = Arrays.asList(user.split(","));
            s_name.add(user_data.get(0));
            s_avgs.add(user_data.get(1));
            s_fans.add(user_data.get(2));
            s_website.add(user_data.get(3));
        }
    }

    @Override
    public void onRefresh() {
        Log.d("checklength", "here");
        mSwipeRefreshLayout.setRefreshing(true);
        loadList();
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
