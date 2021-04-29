package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class Play extends Fragment {
    ImageButton mmButton; // tasto matchmaking
    ImageButton friendsButton;  // tasto gioca con amici
    Button aboutButton; // tasto "informazioni"


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.play, null);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mmButton = view.findViewById(R.id.imageButton2);
        friendsButton = view.findViewById(R.id.imageButton3);
        aboutButton = view.findViewById(R.id.aboutButton);

        mmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Play.this)
                        .navigate(R.id.goToMM);
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Play.this)
                        .navigate(R.id.goToFriends);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Play.this)
                        .navigate(R.id.goToAbout);
            }
        });
    }
}
