package edu.ub.happyhound_app.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.view.infoPerros;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.MyViewHolder> {

    private static final String TAG_INFOPERROS = "infoPerros_fragment";
    private FragmentManager fragmentManager;
    private List<Card_dog> itemList;

    public UserCardAdapter(FragmentManager supportFragmentManager, List<Card_dog> itemList) {
        this.fragmentManager = supportFragmentManager;
        this.itemList = itemList;
    }

    // Implementa los métodos necesarios de RecyclerView.Adapter

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card_dog c = itemList.get(position);
        holder.textView.setText(c.getDog_name());
        Glide.with(holder.itemView.getContext())
                .load(c.getDog_url())
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                infoPerros fragment = new infoPerros();

                // Pass the pet data to the fragment as arguments
                Bundle bundle = new Bundle();
                bundle.putString("name", holder.textView.getText().toString());
                fragment.setArguments(bundle);


                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragment, TAG_INFOPERROS);
                transaction.addToBackStack(null); // Add to back stack
                transaction.commit();
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.petName);
            image = itemView.findViewById(R.id.imageView_card);
        }
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



