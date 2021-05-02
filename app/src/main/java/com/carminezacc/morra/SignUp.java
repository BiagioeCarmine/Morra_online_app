package com.carminezacc.morra;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends Fragment {

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword, textInputEditTextConfPassword;
    Button loginButton;
    ProgressBar progressBar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.buttonLogIn);
        textInputEditTextConfPassword = view.findViewById(R.id.conf_password);
        textInputEditTextUsername = view.findViewById(R.id.username_s);
        textInputEditTextPassword = view.findViewById(R.id.password_s);
        progressBar = view.findViewById(R.id.progress); //TODO: rinominare progress con 2 nomi

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onClick(View view) {

                final String username, password, confpass;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                confpass = String.valueOf(textInputEditTextConfPassword.getText());
                if (password.length() < 5 || password.length() > 50){
                    //TODO: FARE QUALCOSA
                }
                if (!(password.equals(confpass))){
                    //TODO: FARE QUALCOSA
                }
                String pattern = "^[A-Za-z0-9]*$";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(username);
                boolean b = m.matches();
                if (b){
                    //TODO: FINIREEEEEEE
                }





            }
        });

    }



}

