package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.interfaces.QueueStatusHandler;
import com.carminezacc.morra.polling.PollingThreadQueue;
import com.carminezacc.morra.state.SessionSingleton;

import org.joda.time.DateTime;

public class MatchMakingScreen extends Fragment {
    private static final String ARG_PARAM1 = "type";

    PollingThreadQueue pollingThreadQueue;

    TextView userIdText;

    String type;


    private void playMatch(int matchId) {
        Bundle bundle = new Bundle();
        bundle.putInt("matchId", matchId);
        NavHostFragment.findNavController(MatchMakingScreen.this).navigate(R.id.mm_to_confirm, bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        } else {
            // TODO:error handling
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.matchmaking, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        userIdText = view.findViewById(R.id.userIdText);
        super.onViewCreated(view, savedInstanceState);
        if (type == "public")
            Matchmaking.addToPublicQueue(MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler() {
                @Override
                public void handleMatchCreation(int matchId) {
                    playMatch(matchId);
                }

                @Override
                public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                    if (inQueue) {

                        pollingThreadQueue = new PollingThreadQueue(pollBefore, true, MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler() {
                            @Override
                            public void handleMatchCreation(int matchId) {
                                playMatch(matchId);
                            }

                            @Override
                            public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                // NON VERRÀ MAI CHIAMATO PERCHÉ LO GESTISCE IL THREAD
                            }
                        });
                        Thread t = new Thread(pollingThreadQueue);
                        t.start();
                    }
                }
            });
        else {
            userIdText.setText("User ID: " + String.valueOf(SessionSingleton.getInstance().getUserId()));
            Matchmaking.addToPrivateQueue(MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler() {
                @Override
                public void handleMatchCreation(int matchId) {
                    // MAI CHIAMATO, VISTO CHE AGGIUNGIAMO ALLA CODA PRIVATA
                }

                @Override
                public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                    pollingThreadQueue = new PollingThreadQueue(pollBefore, false, MatchMakingScreen.this.getContext().getApplicationContext(), new QueueStatusHandler() {
                        @Override
                        public void handleMatchCreation(int matchId) {
                            playMatch(matchId);
                        }

                        @Override
                        public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                            // NON VERRÀ MAI CHIAMATO PERCHÉ LO GESTISCE IL THREAD
                        }
                    });
                    Thread t = new Thread(pollingThreadQueue);
                    t.start();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pollingThreadQueue != null)
            pollingThreadQueue.running = false;
    }
}