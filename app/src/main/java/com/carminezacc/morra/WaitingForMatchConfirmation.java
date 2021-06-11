package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.GetUserHandler;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;
import com.carminezacc.morra.polling.PollingThreadConfirmation;
import com.carminezacc.morra.state.SessionSingleton;
import com.google.android.material.snackbar.Snackbar;

public class WaitingForMatchConfirmation extends Fragment {
    private static final String ARG_PARAM1 = "matchId";

    private int matchId;
    Match match;
    PollingThreadConfirmation pollingThreadConfirmation;
    String opponentName;


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
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView IDText = view.findViewById(R.id.idPartita);
        final TextView DataText = view.findViewById(R.id.datiPartita);
        IDText.setText(String.valueOf(matchId));

        Matches.getMatch(matchId, WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new MatchResultCallback() {
            @Override
            public void resultReturned(Match returnedMatch) {
                match = returnedMatch;
                int opponentId = (match.getUserid1() == SessionSingleton.getInstance().getUserId()) ? match.getUserid2() : match.getUserid1();
                // dati dei due utenti da mostrare all'utente
                Users.getUser(opponentId, WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new GetUserHandler() {
                    @Override
                    public void resultReturned(User user) {
                        opponentName = user.getUsername();
                        DataText.setText(opponentName);
                    }
                });

                // attendiamo la conferma della partita
                pollingThreadConfirmation = new PollingThreadConfirmation(matchId, WaitingForMatchConfirmation.this.getContext().getApplicationContext(), new MatchResultCallback() {
                    @Override
                    public void resultReturned(Match match) {
                        Snackbar.make(view, "La partita è stata confermata", Snackbar.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("matchId", matchId);
                        bundle.putLong("startTime", match.getStartTime().getMillis());
                        bundle.putLong("firstRoundResultsTime", match.getFirstRoundResults().getMillis());
                        bundle.putInt("userId1", match.getUserid1());
                        bundle.putString("opponentName", opponentName);
                        NavHostFragment.findNavController(WaitingForMatchConfirmation.this).navigate(R.id.confirm_to_match, bundle);
                    }
                });

                Thread thread = new Thread(pollingThreadConfirmation);
                thread.start();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pollingThreadConfirmation != null)
            pollingThreadConfirmation.running = false;
    }
}