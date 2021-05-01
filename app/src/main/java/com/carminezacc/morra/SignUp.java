package com.carminezacc.morra;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;


public class SignUp extends Fragment {

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword, textInputEditTextConfPassword;
    Button loginButton;

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
        /*
        boolean validateUsername(){
            String username = textInputEditTextUsername.toString();
            if (username.length() < 3 || username.length() > 30){
                return false;
            }
        }*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SignUp.this)
                        .navigate(R.id.goToLogin);
            }
        });

    }



}

