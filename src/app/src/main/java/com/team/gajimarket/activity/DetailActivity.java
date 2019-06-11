package com.team.gajimarket.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.logging.Logger;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team.gajimarket.R;
import com.team.gajimarket.item.RecyclerItem;

import java.io.InputStream;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ImageView imgItem;
    TextView txtName, txtPrice, txtSize;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        RecyclerItem pickedItem = intent.getParcelableExtra("pickedItem");
        String fullURL = intent.getStringExtra("fullURL");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("furnitures/" + pickedItem.getKey() + ".png");

        imgItem = (ImageView)findViewById(R.id.pickedItemImage);
        txtName = (TextView)findViewById(R.id.pickedItemName);
        txtPrice = (TextView)findViewById(R.id.pickedItemPrice);
        txtSize = (TextView)findViewById(R.id.pickedItemSize);

        txtName.setText(pickedItem.getName());
        txtPrice.setText(pickedItem.getPrice() + " KRW");
        txtSize.setText(pickedItem.getSize());
        Glide.with(this).load(storageReference).into(imgItem);

        Button btnPickedItemVR = (Button)findViewById(R.id.btnPickedItemVR);
        btnPickedItemVR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArActivity.class);
                startActivity(intent);
            }
        });
    }
}
