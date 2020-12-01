package com.example.rsby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {


    List<String> txts;
    List<String> usefuls;
    List<String> stars;
    Context context;
    int detail_recycler_id;
    int detail_recycler_layout;

    public DetailRecyclerViewAdapter(Context ct, List<String>s_review_txt, List<String>s_review_useful, List<String>s_review_star, int detail_recycler_id, int detail_recycler_layout){
        this.context = ct;
        this.txts = s_review_txt;
        this.usefuls = s_review_useful;
        this.stars = s_review_star;
        this.detail_recycler_id = detail_recycler_id;
        this.detail_recycler_layout = detail_recycler_layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(detail_recycler_layout, parent, false);
        final DetailRecyclerViewAdapter.ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt.setText(txts.get(position));
        holder.useful.setText(usefuls.get(position));
        holder.star.setText(stars.get(position));
    }

    @Override
    public int getItemCount() {
        return txts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt;
        TextView useful;
        TextView star;
        private LinearLayout item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(detail_recycler_id);
            txt = itemView.findViewById(R.id.r_review);
            useful = itemView.findViewById(R.id.r_useful);
            star = itemView.findViewById(R.id.r_star);
        }
    }
}
