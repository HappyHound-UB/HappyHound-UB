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
    private final FragmentManager fragmentManager;
    private List<Card_dog> itemList;
    private Card_dog c;
    private FirebaseAuthManager<UserCardAdapter> authManager;
    private SearchDatabase searchDatabase;

    public UserCardAdapter(FragmentManager supportFragmentManager, List<Card_dog> itemList) {
        this.fragmentManager = supportFragmentManager;
        this.itemList = itemList;
        authManager = new FirebaseAuthManager<>();
        searchDatabase = new SearchDatabase();

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        c = itemList.get(position);
        holder.nameView.setText(c.getDog_name());
        Glide.with(holder.itemView.getContext())
                .load(c.getDog_url())
                .into(holder.image);

        String uId = authManager.getUser().getUid();
        searchDatabase.searchPetData("Users/" + uId + "/Lista Perros/" + c.getDog_name(), holder);

        // para abrir un perro especifico
        holder.itemView.setOnClickListener(view -> {

            infoPerros fragment = new infoPerros();

            // Pass the pet data to the fragment as arguments
            Bundle bundle = new Bundle();
            bundle.putString("name", holder.nameView.getText().toString());
            fragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, fragment, TAG_INFOPERROS);
            transaction.addToBackStack(null); // Add to back stack
            transaction.commit();
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
        TextView nameView, ageView;
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.petName);
            image = itemView.findViewById(R.id.imageView_card);
            ageView = itemView.findViewById(R.id.petAge);
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



