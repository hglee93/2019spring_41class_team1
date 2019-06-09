package com.team.gajimarket.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.team.gajimarket.activity.FurnitureAdminActivity;
import com.team.gajimarket.activity.MainActivity;
import com.team.gajimarket.activity.UserData;

import java.util.HashMap;
import java.util.Map;

public class FragmentMypage extends Fragment {
    MainActivity mainActivity;

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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        String uid = mAuth.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();

        Button changeToSeller = (Button)rootView.findViewById(R.id.btnChangeToSeller);
        Button adminFurniture = (Button)rootView.findViewById(R.id.btnAdminFurniture);

        Query query = mDatabase.child("users").orderByChild("userName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(mAuth.getUid())) {
                        if (postSnapshot.getValue(UserData.class).getUserPos().equals("seller")) {
                            changeToSeller.setVisibility(changeToSeller.GONE);
                            adminFurniture.setVisibility(adminFurniture.VISIBLE);
                        } else {
                            changeToSeller.setVisibility(changeToSeller.VISIBLE);
                            adminFurniture.setVisibility(adminFurniture.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeToSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);

                alert.setTitle("판매자 전환");
                alert.setMessage("사업자등록번호를 입력하세요.");

                final EditText CRN = new EditText(mainActivity);
                alert.setView(CRN);

                alert.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mainActivity, "등록 요청 되었습니다.", Toast.LENGTH_SHORT).show();

                        String userCRN = CRN.getText().toString();

                        UserData userdata = new UserData(name, email, "seller", userCRN);
                        Map<String, Object> userdataValues = userdata.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/users/" + mAuth.getUid(), userdataValues);
                        mDatabase.updateChildren(childUpdates);

                        changeToSeller.setVisibility(changeToSeller.GONE);
                        adminFurniture.setVisibility(adminFurniture.VISIBLE);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();
            }
        });

        adminFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), FurnitureAdminActivity.class);
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
