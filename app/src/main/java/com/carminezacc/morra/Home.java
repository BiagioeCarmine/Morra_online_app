package com.carminezacc.morra;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.backend.VerifyHandler;
import com.carminezacc.morra.state.SessionSingleton;

public class Home extends Fragment {
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
        return inflater.inflate(R.layout.home, null);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsButton = view.findViewById(R.id.settingsButton);
        recordsButton = view.findViewById(R.id.recordsButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        playButton = view.findViewById(R.id.playButton);
        exitButton = view.findViewById(R.id.exitButton);

        //TODO: approfondire mancata rimozione bad token

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token","");
        Log.d("token", token);
        if (token.length() == 0){
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.goToLoginfromHome);
        }
        else{
            Users.verify(token, Home.this.getContext().getApplicationContext(), new VerifyHandler() {
                @Override
                public void handleVerify(boolean success, int userId) {
                    if (success){
                        SessionSingleton session = SessionSingleton.getInstance();
                        session.setSession(userId, token);
                    }
                    else{
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("token");
                        editor.commit();
                        Log.d("token", token);
                        NavHostFragment.findNavController(Home.this)
                                .navigate(R.id.goToLoginfromHome);
                    }
                }
            });
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Home.this)
                        .navigate(R.id.goToSettings);
            }
        });

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
                editor.commit();
                SessionSingleton.getInstance().logOut();
                NavHostFragment.findNavController(Home.this)
                        .navigate(R.id.goToLoginfromHome);
            }
        });
    }
}