package com.carminezacc.morra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.GetUserHandler;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;
import com.carminezacc.morra.polling.PollingThreadConfirmation;
import com.carminezacc.morra.state.SessionSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class WaitingForMatchConfirmation extends Fragment {

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.dialog_problemone_message)
                    .setTitle(R.string.dialog_problemone_title);

            builder.show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiting_for_match_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView IDText = view.findViewById(R.id.idPartita);
        final TextView DataText = view.findViewById(R.id.datiPartita);
        IDText.setText(String.valueOf(matchId));

        Matches.getMatch(matchId, Objects.requireNonNull(WaitingForMatchConfirmation.this.getContext()).getApplicationContext(), new MatchResultCallback() {
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
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        if(statusCode == 404) {
                            NavHostFragment.findNavController(WaitingForMatchConfirmation.this).navigate(R.id.login); // TODO: controllare se funziona
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setMessage(R.string.dialog_user_deleted_title)
                                    .setTitle(R.string.dialog_user_deleted_message);

                            builder.show();

                        } else {
                            showServerDownDialog();
                        }
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
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        showServerDownDialog();
                    }
                });

                Thread thread = new Thread(pollingThreadConfirmation);
                thread.start();
            }
        }, new ServerErrorHandler() {
            @Override
            public void error(int statusCode) {
                NavHostFragment.findNavController(WaitingForMatchConfirmation.this).navigate(R.id.home);
                showServerDownDialog();
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