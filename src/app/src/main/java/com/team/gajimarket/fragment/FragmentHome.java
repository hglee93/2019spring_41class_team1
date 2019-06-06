package com.team.gajimarket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.gajimarket.R;
import com.team.gajimarket.activity.MainActivity;
import com.team.gajimarket.adapter.RecyclerAdapter;
import com.team.gajimarket.item.RecyclerItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        mainActivity = (MainActivity)getActivity();
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<RecyclerItem> Dataset = new ArrayList<>();
        Dataset.add(new RecyclerItem("Dummy1"));
        Dataset.add(new RecyclerItem("Dummy2"));
        Dataset.add(new RecyclerItem("Dummy3"));
        Dataset.add(new RecyclerItem("Dummy4"));

        mAdapter = new RecyclerAdapter(Dataset);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
