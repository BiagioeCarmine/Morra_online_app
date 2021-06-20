package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.GetClassificaHandler;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.User;

import java.util.Objects;


public class Ranking extends Fragment {
    void showServerDownDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_server_down_message)
                .setTitle(R.string.dialog_server_down_title)
                .setPositiveButton(R.string.dialog_server_down_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        String uriString = "mailto:" + Uri.encode("carmine@carminezacc.com") +
                                "?cc=" + Uri.encode("biagiogrimos@gmail.com") +
                                "&subject=" + Uri.encode(getString(R.string.dialog_server_down_email_title)) +
                                "&body=" + Uri.encode(getString(R.string.dialog_server_down_email_body));
                        emailIntent.setData(Uri.parse(uriString));
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.dialog_server_down_button)));
                    }
                });

        builder.create();
    }

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
                textViewFirst.setText("1  " + userList[0].getUsername() + "       " + userList[0].getPunteggio());
                textViewSecond.setText("2 " + userList[1].getUsername() + "       " + userList[1].getPunteggio());
                textViewThird.setText("3  " + userList[2].getUsername() + "       " + userList[2].getPunteggio());
                textViewFourth.setText("4 " + userList[3].getUsername() + "       " + userList[3].getPunteggio());
                textViewFifth.setText("5  " + userList[4].getUsername() + "       " + userList[4].getPunteggio());
                Log.d("user4", userList[3].getUsername());
            }
        }, new ServerErrorHandler() {
            @Override
            public void error(int statusCode) {
                showServerDownDialog();
            }
        });

    }
}
