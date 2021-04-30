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

public class Home extends Fragment {
    Button settingsButton; // tasto options
    Button recordsButton;  // tasto records
    Button aboutButton; // tasto "informazioni"
    Button playButton; //tasto play
    Button exitButton; //tasto exit
/*
* CI SONO ERRORI, MA NON È NIENTE DI GRAVE, SO COME FIXARE
* */

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.play, null);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsButton = view.findViewById(R.id.settingsButton);
        recordsButton = view.findViewById(R.id.recordsButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        playButton = view.findViewById(R.id.playButton);
        exitButton = view.findViewById(R.id.exitButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Home.this)
                        .navigate(R.id.goToSettings);
            }
        });

        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Home.this)
                        .navigate(R.id.goToRecords);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Home.this)
                        .navigate(R.id.goToAbout);
            }
        });
    }
}