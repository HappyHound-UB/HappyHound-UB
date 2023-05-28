package edu.ub.happyhound_app.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import edu.ub.happyhound_app.R;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name, description, date, time;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.detail);
        date = itemView.findViewById(R.id.dateRV);
        time = itemView.findViewById(R.id.timeRV);
    }
}

public class DynamicRV_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LOADING = 1;
    private static final int TYPE_PASEO = 100, TYPE_COMIDA = 200, TYPE_OTHERS = 300;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Reminder> remindersList;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public DynamicRV_Adapter(RecyclerView recyclerView, Activity activity, List<Reminder> remindersList) {
        this.activity = activity;
        this.remindersList = remindersList;

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = Objects.requireNonNull(layoutManager).getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) loadMore.onLoadMore();
                    isLoading = true;
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (remindersList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            String type = remindersList.get(position).getType();
            if (Objects.equals(type, "Paseo")) return TYPE_PASEO;
            else if (Objects.equals(type, "Comida")) return TYPE_COMIDA;
            else return TYPE_OTHERS;
        }
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_PASEO) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_paseo_reminder_layout, parent, false);
            return new ItemViewHolder(view);

        } else if (viewType == TYPE_COMIDA) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_comida_reminder_layout, parent, false);
            return new ItemViewHolder(view);

        } else if (viewType == TYPE_OTHERS) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_other_reminder_layout, parent, false);
            return new ItemViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_progressbar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            Reminder reminder = remindersList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.name.setText(reminder.getName());
            viewHolder.description.setText(reminder.getDescription());
            viewHolder.date.setText(reminder.getDate());
            viewHolder.time.setText(reminder.getTime());

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        }


    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setRemindersList(List<Reminder> remindersList) {
        this.remindersList = remindersList;
    }
}
