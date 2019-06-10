package com.team.gajimarket.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team.gajimarket.R;
import com.team.gajimarket.activity.DetailActivity;
import com.team.gajimarket.activity.MainActivity;
import com.team.gajimarket.adapter.RecyclerAdapter;
import com.team.gajimarket.item.FurnitureData;
import com.team.gajimarket.item.RecyclerItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private int count;
    private String fullURL;

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

        FirebaseStorage storage = FirebaseStorage.getInstance();

        LayoutInflater layoutInflater = (LayoutInflater)((MainActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_recycler_view, null);

        recyclerView.setAdapter(mAdapter);

        ImageView ivItemImg = (ImageView)view.findViewById(R.id.itemImg);

        final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("목록을 불러오는 중입니다.\n잠시만 기다려주세요.");
        progressDialog.show();

        count = 0;

        ArrayList<RecyclerItem> Dataset = new ArrayList<>();
        Query query = mDatabase.child("furnitures").orderByChild("sellerID");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    count++;
                    String key = postSnapshot.getKey();

                    String name = postSnapshot.getValue(FurnitureData.class).getFurnitureName();
                    String price = postSnapshot.getValue(FurnitureData.class).getPrice();
                    String width = postSnapshot.getValue(FurnitureData.class).getWidth();
                    String depth = postSnapshot.getValue(FurnitureData.class).getDepth();
                    String height = postSnapshot.getValue(FurnitureData.class).getHeight();
                    String size = width + " X " + depth + " X " + height;

                    fullURL = "gs://eggplant-market.appspot.com";
                    StorageReference storageReference = storage.getReferenceFromUrl(fullURL)
                            .child("furnitures/" + key + ".png");

                    storageReference.getBytes(1024 * 1024 * 5)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Dataset.add(new RecyclerItem(key, name, price, size, BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                                    mAdapter.notifyDataSetChanged();
                                    if (count == mAdapter.getItemCount()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(mainActivity, "목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

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
                Intent intent = new Intent(mainActivity, DetailActivity.class);
                intent.putExtra("pickedItem", item);
                intent.putExtra("fullURL", fullURL);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
