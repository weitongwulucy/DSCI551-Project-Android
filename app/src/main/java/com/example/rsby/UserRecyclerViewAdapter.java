package com.example.rsby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {


    List<String> names = new ArrayList<>();
    List<String> avgss = new ArrayList<>();
    List<String> fanss = new ArrayList<>();
    List<String> websites = new ArrayList<>();

    Context context;
    int user_recycler_id;
    int user_recycler_layout;

    boolean is_fav;

    public UserRecyclerViewAdapter(Context ct, List<String>s_name, List<String>s_avgs, List<String>s_fans, List<String>s_website, int user_recycler_id, int user_recycler_layout, boolean is_fav){
        this.context = ct;
        this.names = s_name;
        this.avgss = s_avgs;
        this.fanss = s_fans;
        this.websites = s_website;
        this.user_recycler_id = user_recycler_id;
        this.user_recycler_layout = user_recycler_layout;
        this.is_fav = is_fav;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(user_recycler_layout, parent, false);
        final UserRecyclerViewAdapter.ViewHolder vHolder = new ViewHolder(view);

        final Button fav_btn = view.findViewById(R.id.u_btn_fav);

        if (!is_fav) {
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Saved to favs
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Set<String> set_users = prefs.getStringSet("users", new HashSet<String>());

                    String user = names.get(vHolder.getAdapterPosition()) + ","
                            + avgss.get(vHolder.getAdapterPosition()) + ","
                            + fanss.get(vHolder.getAdapterPosition()) + ","
                            + websites.get(vHolder.getAdapterPosition());

                    set_users.add(user);

                    prefs.edit().putStringSet("users", set_users).apply();

                    //Toggle button imgs
                    int img_fav = R.drawable.star_filled;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                    String toast_txt = "Added User: \n" + names.get(vHolder.getAdapterPosition()) + " to Favourites";
                    Toast toast = Toast.makeText(context, toast_txt, Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
        } else {
            int img_fav = R.drawable.star_filled;
            fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);

            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Set<String> set_users = prefs.getStringSet("users", new HashSet<String>());

                    Log.d("checklength_before", String.valueOf(set_users.size()));

                    String user = names.get(vHolder.getAdapterPosition()) + ","
                            + avgss.get(vHolder.getAdapterPosition()) + ","
                            + fanss.get(vHolder.getAdapterPosition()) + ","
                            + websites.get(vHolder.getAdapterPosition());

                    set_users.remove(user);

                    Log.d("checklength_after", String.valueOf(set_users.size()));

                    prefs.edit().putStringSet("users", set_users).apply();

                    //Toggle button imgs
                    int img_fav = R.drawable.star_border;
                    fav_btn.setCompoundDrawablesWithIntrinsicBounds(img_fav, 0, 0, 0);
                    String toast_txt = "Removed User: \n" + names.get(vHolder.getAdapterPosition());
                    Toast toast = Toast.makeText(context, toast_txt, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }

        vHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open webview with user's website url
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(names.get(vHolder.getAdapterPosition()));

                WebView wv = new WebView(context);
                WebSettings webSettings = wv.getSettings();
                webSettings.setJavaScriptEnabled(true);
                wv.loadUrl(websites.get(vHolder.getAdapterPosition()));
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.avgs.setText(avgss.get(position));
        holder.fans.setText(fanss.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView avgs;
        TextView fans;
        private LinearLayout item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(user_recycler_id);
            name = itemView.findViewById(R.id.u_name);
            avgs = itemView.findViewById(R.id.u_avgs);
            fans = itemView.findViewById(R.id.u_fans);
        }
    }


}
