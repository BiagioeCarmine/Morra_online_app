package com.carminezacc.morra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carminezacc.morra.backend.GetUserHandler;
import com.carminezacc.morra.backend.MatchResultCallback;
import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;

public class WaitingForMatchConfirmation extends Fragment {
    private static final String ARG_PARAM1 = "matchId";

    private int matchId;
    Match match;
    User user1, user2;

    public WaitingForMatchConfirmation() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_PARAM1);
        } else {
            // TODO:error handling
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiting_for_match_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView IDText = view.findViewById(R.id.idPartita);
        final TextView DataText = view.findViewById(R.id.datiPartita);

        Matches.getMatch(matchId, WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new MatchResultCallback() {
            @Override
            public void resultReturned(Match returnedMatch) {
                match = returnedMatch;
                Users.getUser(match.getUserid1(), WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new GetUserHandler() {
                    @Override
                    public void resultReturned(User user) {
                        user1 = user;
                        if(user2 != null){
                            IDText.setText(String.valueOf(match.getId()));
                            DataText.setText((user1.getUsername()+" vs "+user2.getUsername()));
                        }
                    }
                });
                Users.getUser(match.getUserid2(), WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new GetUserHandler() {
                    @Override
                    public void resultReturned(User user) {
                        user2 = user;
                        if(user1 != null){
                            IDText.setText(String.valueOf(match.getId()));
                            DataText.setText((user1.getUsername()+" vs "+user2.getUsername()));
                        }
                    }
                });
            }
        });

    }
}