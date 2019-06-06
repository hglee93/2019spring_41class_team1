package com.team.gajimarket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.gajimarket.R;
import com.team.gajimarket.activity.MainActivity;

public class FragmentMypage extends Fragment {
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        mainActivity = (MainActivity)getActivity();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
