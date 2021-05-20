package com.carminezacc.morra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.backend.PlayWithFriendHandler;
import com.google.android.material.snackbar.Snackbar;

public class PlayWithFriends extends Fragment {
    Button createLobbyButton;
    EditText lobbyIdText;
    Button joinFriendButton;

    private void playMatch(int matchId) {
        Bundle bundle = new Bundle();
        bundle.putInt("matchId", matchId);
        NavHostFragment.findNavController(PlayWithFriends.this).navigate(R.id.friends_to_confirm, bundle);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.play_with_friends, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createLobbyButton = view.findViewById(R.id.createLobbyButton);
        lobbyIdText = view.findViewById(R.id.lobbyIdInput);
        joinFriendButton = view.findViewById(R.id.playWithFriendButton);

        createLobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "private");
                NavHostFragment.findNavController(PlayWithFriends.this).navigate(R.id.friends_to_mm, bundle);
            }
        });

        joinFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendId = lobbyIdText.getText().toString();
                for (char el : friendId.toCharArray()) {
                    if (el < '0' || el > '9') {
                        Snackbar.make(view, "L'ID Utente deve essere intero positivo", 4000).show();
                        return;
                    }
                }
                Matchmaking.playWithFriend(friendId, PlayWithFriends.this.getContext().getApplicationContext(), new PlayWithFriendHandler() {
                    @Override
                    public void handlerPlayWithFriend(boolean success, int matchId) {
                        if (success) {
                            playMatch(matchId);
                        } else {
                            Snackbar.make(view, "L'amico non è online", 4000).show();
                        }
                    }
                });
            }
        });

    }
}