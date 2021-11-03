package com.carminezacc.morra;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.widget.Button;

/**
 * layout {@code res/layout/about.xml}
 */
public class About extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button goBackHomeButton = view.findViewById(R.id.aboutToHome);
        Button toGit = view.findViewById(R.id.toGitHub);
        Button toInstaGrimos = view.findViewById(R.id.toInstaGrimos);
        Button toInstaZac = view.findViewById(R.id.toInstaZac);

        goBackHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(About.this)
                        .navigate(R.id.about_to_home);
            }
        });

        toGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/BiagioeCarmine"));
                startActivity(intent);
            }
        });

        toInstaGrimos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/grimos10"));
                startActivity(intent);
            }
        });

        toInstaZac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/carminezacc"));
                startActivity(intent);
            }
        });
    }
}