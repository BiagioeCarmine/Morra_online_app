package com.carminezacc.morra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.carminezacc.morra.backend.Users;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.interfaces.SignUpHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends Fragment {

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

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword, textInputEditTextConfPassword;
    Button RegistratiButton;

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
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_bad_password);
                    builder.show();
                    return;
                }
                if (!(password.equals(confpass))){
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_password_not_equals);
                    builder.show();
                    return;
                }
                if (username.length() < 3 || username.length() > 30){
                    builder.setTitle(R.string.dialog_something_wrong_title);
                    builder.setMessage(R.string.dialog_bad_username);
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
                    builder.show();
                    return;
                }
                Users.signUp(username, password, Objects.requireNonNull(SignUp.this.getContext()).getApplicationContext(), new SignUpHandler() {
                    @Override
                    public void handleSignUp(boolean success) {
                        if (success) {
                            NavHostFragment.findNavController(SignUp.this)
                                    .navigate(R.id.goToLogin);
                        } else {
                            builder.setTitle(R.string.dialog_something_wrong_title);
                            builder.setMessage(R.string.dialog_existing_user);
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

    }
}