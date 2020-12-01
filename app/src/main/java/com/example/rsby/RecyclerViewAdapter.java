package com.example.rsby;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<String> names;
    List<String> location;
    List<String> images;
    List<String> ids;
    List<String> reviews = new ArrayList<>();
    List<String> usefuls = new ArrayList<>();
    List<String> stars = new ArrayList<>();

    Context context;
    Dialog my_dialog;
    int dialog_layout;
    int recycler_id;
    int recycler_layout;

    boolean is_fav;
    Button fav_btn;

    public RecyclerViewAdapter(Context ct, List<String> s_name, List<String> s_loc, List<String> s_img, List<String> s_id, int dialog_layout, int recycler_id, int recycler_layout, boolean is_fav){
        this.context = ct;
        this.names = s_name;
        this.location = s_loc;
        this.images = s_img;
        this.ids = s_id;
        this.dialog_layout = dialog_layout;
        this.recycler_id = recycler_id;
        this.recycler_layout = recycler_layout;
        this.is_fav = is_fav;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(recycler_layout, parent, false);


        final ViewHolder vHolder = new ViewHolder(view);

        // setup and show detail dialog
        my_dialog = new Dialog(context);
        my_dialog.setContentView(R.layout.restaurant_detail);


        fav_btn = my_dialog.findViewById(R.id.detail_btn_fav);

        vHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = vHolder.getAdapterPosition();
                setupDialogue(my_dialog, pos);
                my_dialog.show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.loc.setText(location.get(position));
        Picasso.get().load(images.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView loc;
        ImageView image;
        private LinearLayout item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(recycler_id);
            name = itemView.findViewById(R.id.r_name);
            loc = itemView.findViewById(R.id.r_location);
            image = itemView.findViewById(R.id.rimageView);
        }
    }

    public void showDetail(String id, Context context, final Dialog dialog){


        RecyclerView detailRecyclerView  = dialog.findViewById(R.id.detail_recyclerview);
        final DetailRecyclerViewAdapter d_adapter = new DetailRecyclerViewAdapter(dialog.getContext(), reviews, usefuls, stars, R.id.d_row_item_id, R.layout.d_row);

        detailRecyclerView.setAdapter(d_adapter);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getOwnerActivity()));

        // call rest api
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://18.144.83.137:5000/recommender/reviews?business_id=" + id;

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("json", "connect");
                        Log.d("json", response.toString());
                        for (int i=0;i<response.length();i++) {
                            try {
                                JSONObject review = response.getJSONObject(i);
                                String review_txt = review.getString("text");
                                String review_useful = review.getString("useful");
                                String review_star = review.getString("stars");

                                reviews.add(review_txt);
                                usefuls.add(review_useful);
                                stars.add(review_star);

                                d_adapter.notifyDataSetChanged();
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

        // clear data when dialog cancelled
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                reviews.clear();
                usefuls.clear();
                stars.clear();
                d_adapter.notifyDataSetChanged();
                //Toggle button imgs
                if (!is_fav) {
                    int img_fav = R.drawable.star_border;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                } else {
                    int img_fav = R.drawable.star_filled;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                }
            }
        });
    }

    public void setupDialogue(Dialog dialog, int position){
        TextView item_name = dialog.findViewById(R.id.detail_restaurant_name);
        TextView item_address = dialog.findViewById(R.id.detail_restaurant_address);
        ImageView item_img = dialog.findViewById(R.id.detail_restaurant_img);

        item_name.setText(names.get(position));
        item_address.setText(location.get(position));
        Picasso.get().load(images.get(position)).into(item_img);

        setupFavButton(position);

        showDetail(ids.get(position), dialog.getContext(),dialog);
    }

    public void setupFavButton(final int position){
        if (is_fav) {
            int img_fav = R.drawable.star_filled;
            fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
            fav_btn.setText("Remove from Fav");
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Saved to favs
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Set<String> set_restaurants = prefs.getStringSet("restaurants", new HashSet<String>());


                    String r = names.get(position) + ","
                            + location.get(position) + ","
                            + images.get(position) + ","
                            + ids.get(position);

                    set_restaurants.remove(r);
                    prefs.edit().putStringSet("restaurants", set_restaurants).apply();

                    //Toggle button imgs
                    int img_fav = R.drawable.star_border;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                    String toast_txt = "Removed Restaurant: \n" + names.get(position);
                    Toast toast = Toast.makeText(context, toast_txt, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else {
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Saved to favs
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Set<String> set_restaurants = prefs.getStringSet("restaurants", new HashSet<String>());

                    String r = names.get(position) + ","
                            + location.get(position) + ","
                            + images.get(position) + ","
                            + ids.get(position);

                    set_restaurants.add(r);
                    prefs.edit().putStringSet("restaurants", set_restaurants).apply();


                    //Toggle button imgs
                    int img_fav = R.drawable.star_filled;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                    String toast_txt = "Added Restaurant: \n" + names.get(position) + " to Favourites";
                    Toast toast = Toast.makeText(context, toast_txt, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}
