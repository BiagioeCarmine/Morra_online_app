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
import com.carminezacc.morra.interfaces.LogInHandler;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogIn extends Fragment {

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

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button loginButton, signupButton;

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

                if (password.length() < 5 || password.length() > 50){
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_bad_password);
                    builder.setCancelable(true);
                    builder.show();
                    return;
                }
                if (username.length() < 3 || username.length() > 30){
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_bad_username);
                    builder.setCancelable(true);
                    builder.show();
                    return;
                }
                String pattern = "^[A-Za-z0-9]*$";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(username);
                boolean b = m.matches();
                if (!b){
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_unsupported_characters);
                    builder.setCancelable(true);
                    builder.show();
                    return;
                }

                Users.logIn(username, password, Objects.requireNonNull(LogIn.this.getContext()).getApplicationContext(), new LogInHandler() {
                    @Override
                    public void handleLogIn(boolean success, String jwt) {
                        if (success) {
                            Log.d("jwt", jwt);
                            SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", jwt);
                            editor.apply();
                            NavHostFragment.findNavController(LogIn.this)
                                    .navigate(R.id.goToHome);
                        } else {
                            builder.setTitle(R.string.dialog_something_wrong_title);
                            builder.setMessage(R.string.dialog_bad_credential);
                            builder.setCancelable(true);
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
