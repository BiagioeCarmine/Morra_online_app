package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.backend.QueueStatusHandler;

import org.joda.time.DateTime;

public class MatchMakingScreen extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.matchmaking, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Matchmaking.addToPublicQueue(MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler() {
            @Override
            public void handleMatchCreation(int matchId) {
                //VIENE CREATA LA PARTITA E QUINDI BISOGNA PASSARE IL MATCHID A MATCHES
            }

            @Override
            public void handlePolling(boolean inQueue, DateTime pollBefore) {
                if (inQueue){
                    //CHIAMARE FUNZIONE PER IL POLLING
                }
            }
        });
    }
}