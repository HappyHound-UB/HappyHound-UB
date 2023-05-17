package edu.ub.happyhound_app.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.ub.happyhound_app.model.Card_dog;
import edu.ub.happyhound_app.R;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.MyViewHolder> {

    private List<Card_dog> itemList;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    public UserCardAdapter(List<Card_dog> itemList) {
        this.itemList = itemList;
    }

    // Implementa los métodos necesarios de RecyclerView.Adapter

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;

        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView_card);
            image = itemView.findViewById(R.id.imageView_card);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card_dog c = itemList.get(position);
        holder.textView1.setText(c.getDog_name());
        Glide.with(holder.itemView.getContext())
                .load(c.getDog_url())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateDataList(List<Card_dog> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}



