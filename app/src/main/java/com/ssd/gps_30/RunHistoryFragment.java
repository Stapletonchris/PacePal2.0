package com.ssd.gps_30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RunHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RunHistoryAdapter adapter;
    private List<RunHistoryItem> runHistoryItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_history, container, false);
        recyclerView = view.findViewById(R.id.run_history_recycler_view);

        // Initialize your data list
        runHistoryItems = new ArrayList<>();
        // Populate the list with your data

        adapter = new RunHistoryAdapter(runHistoryItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void returnToMain(View view) {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }
}

