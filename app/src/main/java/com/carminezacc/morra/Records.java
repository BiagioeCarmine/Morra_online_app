package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.User;
import com.carminezacc.morra.state.SessionSingleton;

import java.util.Objects;
/**
 * Codice che implementa le funzionalità della schermata records (layout {@code res/layout/records.xml})
 */
public class Records extends Fragment {
    /**
     * Metodo che avvisa l'utente che c'è un problema con il server e gli consente di inviare una
     * mail
     */
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

        builder.show();
    }

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
                textViewId.setText(String.valueOf(user.getId()));
                textViewPunteggio.setText(String.valueOf(user.getPunteggio()));
                textViewVittorie.setText(String.valueOf(user.getVittorie()));
                textViewSconfitte.setText(String.valueOf(user.getSconfitte()));
            }
        }, new ServerErrorHandler() {
            @Override
            public void error(int statusCode) {
                if(statusCode == 404) {
                    NavHostFragment.findNavController(Records.this).navigate(R.id.login);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.dialog_user_deleted_title)
                            .setTitle(R.string.dialog_user_deleted_message);

                    builder.show();

                } else {
                    showServerDownDialog();
                }
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
