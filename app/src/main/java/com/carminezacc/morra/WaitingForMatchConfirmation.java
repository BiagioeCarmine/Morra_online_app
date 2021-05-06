package com.carminezacc.morra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carminezacc.morra.backend.MatchResultCallback;
import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.models.Match;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaitingForMatchConfirmation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaitingForMatchConfirmation extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "matchId";

    private int matchId;
    Match match;

    public WaitingForMatchConfirmation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param matchId Parameter 1.
     * @return A new instance of fragment WaitingForMatchConfirmation.
     */
    public static WaitingForMatchConfirmation newInstance(int matchId) {
        WaitingForMatchConfirmation fragment = new WaitingForMatchConfirmation();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, matchId);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
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
                IDText.setText(String.valueOf(match.getId()));
                DataText.setText(String.valueOf(match.getUserid1())+" vs "+String.valueOf(match.getUserid2()));
            }
        });

    }
}