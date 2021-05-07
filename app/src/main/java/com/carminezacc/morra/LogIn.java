package com.carminezacc.morra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.LogInHandler;
import com.carminezacc.morra.backend.Users;
import com.google.android.material.textfield.TextInputEditText;


public class LogIn extends Fragment {
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button loginButton, signupButton;
    private AlertDialog mDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.buttonLogIn);
        signupButton = view.findViewById(R.id.buttonSignUp_2);
        textInputEditTextUsername = view.findViewById(R.id.username_l);
        textInputEditTextPassword = view.findViewById(R.id.password_l);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username, password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                Users.logIn(username, password, LogIn.this.getContext().getApplicationContext(), new LogInHandler() {
                    @Override
                    public void handleLogIn(boolean success, String jwt) {
                        if (success){
                            Log.d("jwt", jwt);
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", jwt);
                            editor.apply();
                            NavHostFragment.findNavController(LogIn.this)
                                    .navigate(R.id.goToHome);
                        }
                        else{
                            builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                            builder.setMessage("Si è verificato un errore durante l'accesso");
                            builder.setCancelable(true);
                            mDialog = builder.show();
                        }
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogIn.this)
                        .navigate(R.id.goToSignup);
            }
        });
    }
}
