package com.ssd.gps_30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RunHistoryAdapter extends RecyclerView.Adapter<RunHistoryViewHolder> {
    private List<RunHistoryItem> runHistoryList;

    // Constructor
    public RunHistoryAdapter(List<RunHistoryItem> runHistoryList) {
        this.runHistoryList = runHistoryList;
    }

    @Override
    public RunHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View runHistoryView = inflater.inflate(R.layout.run_history_item, parent, false);

        // Return a new holder instance
        return new RunHistoryViewHolder(runHistoryView);
    }

    @Override
    public void onBindViewHolder(RunHistoryViewHolder holder, int position) {
        // Get the data model based on position
        RunHistoryItem item = runHistoryList.get(position);

        // Set item views based on your views and data model
        holder.dateTextView.setText(item.getTime());
        holder.distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", item.getDistance()));
        holder.timeTextView.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return runHistoryList.size();
    }
}

