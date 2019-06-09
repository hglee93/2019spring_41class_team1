package com.team.gajimarket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.team.gajimarket.R;
import com.team.gajimarket.activity.MainActivity;
import com.team.gajimarket.adapter.RecyclerAdapter;
import com.team.gajimarket.item.FurnitureData;
import com.team.gajimarket.item.RecyclerItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        String uid = mAuth.getUid();

        ArrayList<RecyclerItem> Dataset = new ArrayList<>();
        Query query = mDatabase.child("furnitures").child(uid).orderByChild("furnitureName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String name = postSnapshot.getValue(FurnitureData.class).getFurnitureName();
                    String price = postSnapshot.getValue(FurnitureData.class).getPrice();
                    String width = postSnapshot.getValue(FurnitureData.class).getWidth();
                    String depth = postSnapshot.getValue(FurnitureData.class).getDepth();
                    String height = postSnapshot.getValue(FurnitureData.class).getHeight();
                    String size = width + " X " + depth + " X " + height;

                    //DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                    //price = decimalFormat.format(price);

                    Dataset.add(new RecyclerItem(name, price, size));
                }

                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAdapter = new RecyclerAdapter(Dataset, new RecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(RecyclerItem item) {
                Toast.makeText(mainActivity, "Item Clicked", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
