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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsby.R;
import com.example.rsby.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment_r extends Fragment {

    RecyclerView recyclerView;
    List<String> s_name = new ArrayList<String>();
    List<String> s_loc = new ArrayList<String>();
    List<String> s_img = new ArrayList<String>();
    List<String> s_id = new ArrayList<String>();
    RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_recycler, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);

        adapter = new RecyclerViewAdapter(getContext(), s_name, s_loc, s_img, s_id, R.layout.restaurant_detail, R.id.r_row_item_id, R.layout.r_row, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    public void searchUpdate(){

        clearResults();
        // receive data from main activity
        String category = getArguments().getString("category");
        String price = getArguments().getString("price");
        String rating = getArguments().getString("rating");
        String city = getArguments().getString("city");
        city.replace(" ", "%20");
        String state = getArguments().getString("state");
        // call rest api
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://18.144.83.137:5000/recommender/top10restaurants?category="  + category +
                "&price=" + price + "&rating=" + rating + "&city=" + city + "&state=" + state;

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
                                String r_name = restaurant.getString("name");
                                String r_address = restaurant.getString("address");
                                String r_img = restaurant.getString("photo");
                                String r_id = restaurant.getString("business_id");

                                s_name.add(r_name);
                                s_loc.add(r_address);
                                s_img.add(r_img);
                                s_id.add(r_id);

                                Log.d("json", r_name + ", " + r_address + ", " + r_img + ", " + r_id);

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
        queue.add(stringRequest);
    }

    public void clearResults(){
        s_name.clear();
        s_loc.clear();
        s_img.clear();
        s_id.clear();
        adapter.notifyDataSetChanged();
    }

    public ArrayList<String> returnIDs(){
        return (ArrayList<String>) s_id;
    }


}
