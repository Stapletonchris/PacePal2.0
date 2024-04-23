package com.ssd.gps_30;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class RunHistoryViewHolder extends RecyclerView.ViewHolder {
    // Define view holder member variables
    TextView dateTextView;
    TextView distanceTextView;
    TextView timeTextView;

    public RunHistoryViewHolder(View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        distanceTextView = itemView.findViewById(R.id.distanceTextView);
        timeTextView = itemView.findViewById(R.id.timeTextView);
    }
}

