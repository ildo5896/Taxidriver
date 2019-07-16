package com.example.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class map_dialog_java extends DialogFragment implements OnMapReadyCallback {
        private GoogleMap map;
        private SupportMapFragment fragment;

    public map_dialog_java() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_map_dialog, container, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_dialog, fragment).commit();

        Button yesButton = view.findViewById(R.id.yesButton_dialog);
        Button noButton = view.findViewById(R.id.noButton);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }
}

