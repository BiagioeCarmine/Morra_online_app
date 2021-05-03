package com.carminezacc.morra;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.carminezacc.morra.backend.SignUpHandler;
import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.backend.QueueSingleton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends Fragment {

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword, textInputEditTextConfPassword;
    Button RegistratiButton;
    private AlertDialog mDialog;

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
        RegistratiButton = view.findViewById(R.id.buttonSignUp);
        textInputEditTextConfPassword = view.findViewById(R.id.conf_password);
        textInputEditTextUsername = view.findViewById(R.id.username_s);
        textInputEditTextPassword = view.findViewById(R.id.password_s);

        RegistratiButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onClick(View view) {

                final String username, password, confpass;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                confpass = String.valueOf(textInputEditTextConfPassword.getText());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                if (password.length() < 5 || password.length() > 50){
                    builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                    builder.setMessage("La password che hai usato è troppo corta o troppo lunga");
                    builder.setCancelable(true);
                    mDialog = builder.show();
                    return;
                }
                if (!(password.equals(confpass))){
                    builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                    builder.setMessage("Le 2 password messe non sono uguali");
                    builder.setCancelable(true);
                    mDialog = builder.show();
                    return;
                }
                if (username.length() < 3 || username.length() > 30){
                    builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                    builder.setMessage("L'username che hai usato è troppo corto o troppo lungo");
                    builder.setCancelable(true);
                    mDialog = builder.show();
                    return;
                }
                String pattern = "^[A-Za-z0-9]*$";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(username);
                boolean b = m.matches();
                if (!b){
                    builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                    builder.setMessage("L'username che hai usato ha caratteri non supportati");
                    builder.setCancelable(true);
                    mDialog = builder.show();
                    return;
                }
                Users.signUp(username, password, SignUp.this.getContext().getApplicationContext(), new SignUpHandler() {
                    @Override
                    public void handleSignUp(boolean success) {
                        if (success){
                            NavHostFragment.findNavController(SignUp.this)
                                    .navigate(R.id.goToLogin);
                        }
                        else{
                            builder.setTitle("OPS! QUALCOSA È ANDATO STORTO");
                            builder.setMessage("Esiste già un utente chiamato cosi");
                            builder.setCancelable(true);
                            mDialog = builder.show();
                        }
                    }
                });
            }
        });

    }



}

