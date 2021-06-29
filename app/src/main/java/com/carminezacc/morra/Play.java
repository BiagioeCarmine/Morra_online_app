package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
/**
 * Codice che implementa le funzionalità della schermata play (layout {@code res/layout/play.xml})
 */
public class Play extends Fragment {
    ImageButton mmButton; // tasto matchmaking
    ImageButton friendsButton;  // tasto gioca con amici


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

        mmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "public");
                NavHostFragment.findNavController(Play.this)
                        .navigate(R.id.goToMM, bundle);
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Play.this)
                        .navigate(R.id.goToFriends);
            }
        });
    }
}
