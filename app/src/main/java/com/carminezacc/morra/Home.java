package com.carminezacc.morra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.interfaces.VerifyHandler;
import com.carminezacc.morra.state.SessionSingleton;

import java.util.Objects;

/**
 * Codice che implementa le funzionalità della schermata home (layout {@code res/layout/home.xml})
 */
public class Home extends Fragment {
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

    Button settingsButton; // tasto options
    Button recordsButton;  // tasto records
    Button aboutButton; // tasto "informazioni"
    Button playButton; //tasto play
    Button exitButton; //tasto exit
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        //settingsButton = view.findViewById(R.id.settingsButton);
        recordsButton = view.findViewById(R.id.recordsButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        playButton = view.findViewById(R.id.playButton);
        exitButton = view.findViewById(R.id.exitButton);


        playButton.setVisibility(View.INVISIBLE);
        recordsButton.setVisibility(View.INVISIBLE);



        final SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token","");
        Log.d("token", token);
        if (token.length() == 0){
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.goToLoginfromHome);
        }
        else{/*
        TODO: lascio commentata questa parte perchè ancora non sappiamo bene a cosa può servire la schermata settings... WORK IN PROGRESS

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Home.this)
                            .navigate(R.id.goToSettings);
                }
            });
*/
            recordsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Home.this)
                            .navigate(R.id.goToRecords);
                }
            });

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Home.this)
                            .navigate(R.id.goToPlay);
                }
            });

            aboutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Home.this)
                            .navigate(R.id.goToAbout);
                }
            });
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("token");
                    editor.apply();
                    SessionSingleton.getInstance().logOut();
                    NavHostFragment.findNavController(Home.this)
                            .navigate(R.id.goToLoginfromHome);
                }
            });
            Users.verify(token, Objects.requireNonNull(Home.this.getContext()).getApplicationContext(), new VerifyHandler() {
                @Override
                public void handleVerify(boolean success, int userId) {
                    if (success) {
                        SessionSingleton session = SessionSingleton.getInstance();
                        session.setSession(userId, token);
                        playButton.setVisibility(View.VISIBLE);
                        recordsButton.setVisibility(View.VISIBLE);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("token");
                        editor.apply();
                        Log.d("token", token);
                        NavHostFragment.findNavController(Home.this)
                                .navigate(R.id.goToLoginfromHome);
                        builder.setMessage(R.string.dialog_something_wrong_title)
                                .setTitle(R.string.dialog_bad_token);

                        builder.show();
                    }
                }
            }, new ServerErrorHandler() {
                @Override
                public void error(int statusCode) {
                    showServerDownDialog();
                }
            });
        }

    }
}