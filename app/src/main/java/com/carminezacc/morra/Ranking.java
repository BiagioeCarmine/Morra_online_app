package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.GetClassificaHandler;
import com.carminezacc.morra.models.User;

import java.util.List;
import java.util.Objects;


public class Ranking extends Fragment {

    TextView textViewFirst;
    TextView textViewSecond;
    TextView textViewThird;
    TextView textViewFourth;
    TextView textViewFifth;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ranking, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewFirst = view.findViewById(R.id.textViewFirst);
        textViewSecond = view.findViewById(R.id.textViewSecond);
        textViewThird = view.findViewById(R.id.textViewThird);
        textViewFourth = view.findViewById(R.id.textViewFourth);
        textViewFifth = view.findViewById(R.id.textViewFifth);

        Users.getRanking(Objects.requireNonNull(Ranking.this.getContext()).getApplicationContext(), new GetClassificaHandler() {
            @Override
            public void resultReturned(User[] userList) {
              textViewFirst.setText(String.valueOf(userList[0].getPunteggio()));
              textViewSecond.setText(String.valueOf(userList[1].getPunteggio()));
              textViewThird.setText(String.valueOf(userList[2].getPunteggio()));
              textViewFourth.setText(String.valueOf(userList[3].getPunteggio()));
              textViewFifth.setText(String.valueOf(userList[4].getPunteggio()));
            }
        });

    }
}
