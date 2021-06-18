package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.GetUserHandler;
import com.carminezacc.morra.models.User;
import com.carminezacc.morra.state.SessionSingleton;

import java.util.Objects;

public class Records extends Fragment {

    TextView textViewId;
    TextView textViewPunteggio;
    TextView textViewSconfitte;
    TextView textViewVittorie;
    Button rankingButton;
    int userId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.records, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewId = view.findViewById(R.id.textViewId);
        textViewPunteggio = view.findViewById(R.id.textViewPunteggio);
        textViewSconfitte = view.findViewById(R.id.textViewSconfitte);
        textViewVittorie = view.findViewById(R.id.textViewVittorie);
        rankingButton = view.findViewById(R.id.rankingButton);
        userId = SessionSingleton.getInstance().getUserId();

        Users.getUser(userId, Objects.requireNonNull(Records.this.getContext()).getApplicationContext(), new GetUserHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void resultReturned(User user) {
                textViewId.setText("Il tuo id e': " + user.getId());
                textViewPunteggio.setText("Il tuo punteggio e' di: " + user.getPunteggio() + " punti");
                textViewVittorie.setText("Hai totalizzato " + user.getVittorie() + " vittorie");
                textViewSconfitte.setText("Hai totalizzato " + user.getSconfitte() + " sconfitte");
            }
        });

        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Records.this)
                        .navigate(R.id.records_to_ranking);
            }
        });

    }
}
