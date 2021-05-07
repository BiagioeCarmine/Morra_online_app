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
import com.carminezacc.morra.polling.PollingThread;

import org.joda.time.DateTime;

public class MatchMakingScreen extends Fragment {

    PollingThread pollingThread;



    private void playMatch(int matchId){
        Bundle bundle = new Bundle();
        bundle.putInt("matchId", matchId);
        NavHostFragment.findNavController(MatchMakingScreen.this).navigate(R.id.mm_to_confirm, bundle);
    }

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
                playMatch(matchId);
            }

            @Override
            public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                if (inQueue){

                    pollingThread = new PollingThread(pollBefore, true, MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler(){
                        @Override
                        public void handleMatchCreation(int matchId) {
                            playMatch(matchId);
                        }

                        @Override
                        public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                            //NIENTE
                        }
                    });
                    Thread t = new Thread(pollingThread);
                    t.start();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pollingThread != null)
            pollingThread.running = false;
    }
}