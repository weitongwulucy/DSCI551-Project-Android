package com.example.rsby.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsby.R;
import com.example.rsby.UserRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment_u extends Fragment {

    RecyclerView recyclerView;
    List<String> s_name = new ArrayList<String>();
    List<String> s_avgs = new ArrayList<String>();
    List<String> s_fans = new ArrayList<String>();
    List<String> s_website = new ArrayList<String>();
    UserRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_recycler, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);

        adapter = new UserRecyclerViewAdapter(this.getActivity(), s_name, s_avgs, s_fans, s_website, R.id.u_row_item_id, R.layout.u_row,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public void adapterClear(){
        s_name.clear();
        s_avgs.clear();
        s_fans.clear();
        s_website.clear();
        adapter.notifyDataSetChanged();
    }

    public void setRecyclerView(List<String> ids){
        adapterClear();
        getUsers(ids);
    }

    public void getUsers(List<String> ids){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://18.144.83.137:5000/recommender/top10users?restaurants=";
        for (int i = 0; i < ids.size(); i++) {
            url += ids.get(i) + ", ";
        }
        url = url.substring(0, url.length() - 2);
        url.replace(" ", "%20");

        Log.d("json", url);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("json", "connect");
                        Log.d("json", response.toString());
                        for (int i=0;i<response.length();i++) {
                            try {
                                JSONObject restaurant = response.getJSONObject(i);
                                String u_name = restaurant.getString("name");
                                String u_avgs = restaurant.getString("average_stars");
                                String u_fans = restaurant.getString("fans");
                                String u_web = restaurant.getString("user_website");

                                s_name.add(u_name);
                                s_avgs.add(u_avgs);
                                s_fans.add(u_fans);
                                s_website.add(u_web);

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("json", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}
