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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.interfaces.QueueStatusHandler;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.polling.PollingThreadQueue;
import com.carminezacc.morra.state.SessionSingleton;

import org.joda.time.DateTime;

import java.util.Objects;

public class MatchMakingScreen extends Fragment {

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.dialog_problemone_message)
                    .setTitle(R.string.dialog_problemone_title);

            builder.show();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.matchmaking, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        userIdText = view.findViewById(R.id.userIdText);
        super.onViewCreated(view, savedInstanceState);
        if (type.equals("public"))
            Matchmaking.addToPublicQueue(Objects.requireNonNull(MatchMakingScreen.this.getContext()).getApplicationContext(), new QueueStatusHandler() {
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
                        }, new ServerErrorHandler() {
                            @Override
                            public void error(int statusCode) {
                                NavHostFragment.findNavController(MatchMakingScreen.this).navigate(R.id.home);
                                showServerDownDialog();
                            }
                        });
                        Thread t = new Thread(pollingThreadQueue);
                        t.start();
                    }
                }
            }, new ServerErrorHandler() {
                @Override
                public void error(int statusCode) {
                    showServerDownDialog();
                }
            });
        else {
            userIdText.setText("User ID: " + SessionSingleton.getInstance().getUserId());
            Matchmaking.addToPrivateQueue(Objects.requireNonNull(MatchMakingScreen.this.getContext()).getApplicationContext(), new QueueStatusHandler() {
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
                    }, new ServerErrorHandler() {
                        @Override
                        public void error(int statusCode) {
                            showServerDownDialog();
                        }
                    });
                    Thread t = new Thread(pollingThreadQueue);
                    t.start();
                }
            }, new ServerErrorHandler() {
                @Override
                public void error(int statusCode) {
                    showServerDownDialog();
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