package com.carminezacc.morra;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Classe che imposta i valori da mostrare all'utente a fine partita (layout {@code res/layout/
 * fragment_match_finished_screen.xml})
 */
public class MatchFinishedScreen extends Fragment {

    private static final String ARG_PARAM1 = "puntiAvversario";
    private static final String ARG_PARAM2 = "puntiUtente";
    private static final String ARG_PARAM3 = "opponentName";

    private int puntiUtente;
    private int puntiAvversario;
    private String opponentName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            puntiAvversario = getArguments().getInt(ARG_PARAM1);
            puntiUtente = getArguments().getInt(ARG_PARAM2);
            opponentName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_finished_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nomeAvversarioView = view.findViewById(R.id.nomeAvversario);
        TextView puntiUtenteView = view.findViewById(R.id.puntiUtente);
        TextView puntiAvversarioView = view.findViewById(R.id.puntiAvversario);
        Button backHomeButton = view.findViewById(R.id.backHomeButton);

        nomeAvversarioView.setText(String.valueOf(opponentName));
        puntiUtenteView.setText(String.valueOf(puntiUtente));
        puntiAvversarioView.setText(String.valueOf(puntiAvversario));

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MatchFinishedScreen.this)
                        .navigate(R.id.match_finished_to_home);
            }
        });
    }
}