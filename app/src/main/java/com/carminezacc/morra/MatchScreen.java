package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.carminezacc.morra.interfaces.MatchCallback;
import com.carminezacc.morra.polling.PollingThreadMatch;
import com.carminezacc.morra.state.SessionSingleton;

import org.joda.time.DateTime;

public class MatchScreen extends Fragment {

    TextView textViewPrediction;
    TextView textViewOpponentPoints;
    TextView textViewYourPoints;
    TextView textViewOpponentHand;
    TextView textViewOpponentPrediction;
    NumberPicker numberPicker;
    ImageButton[] imageButtons = new ImageButton[5];
    boolean isPressed;
    boolean isClicked = false;
    int isColored = 0;
    int hand;
    CountDownTimer countDownTimer;
    int matchId;
    int userId;
    DateTime roundStartTime;
    DateTime lastRoundResultsTime;
    PollingThreadMatch pollingThreadMatch;
    int matchUserId1; // per vedere se siamo user 1 o user 2

    void setHand(int n) {
        hand = n;
        for(int i = 0; i < 5; i++) {
            if(i != n-1) {
                imageButtons[i].setBackgroundResource(R.color.colorBackground);
            } else {
                imageButtons[i].setBackgroundResource(R.color.colorBackgroundButton);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            matchId = getArguments().getInt("matchId");
            roundStartTime = new DateTime(getArguments().getLong("startTime"));
            lastRoundResultsTime = new DateTime(getArguments().getLong("firstRoundResultsTime"));
            matchUserId1 = getArguments().getInt("userId1");
        } else {
            // TODO:error handling
        }


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.match_screen, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewPrediction = view.findViewById(R.id.textViewNumberPicker);
        textViewOpponentPoints = view.findViewById(R.id.textViewOpponentPoints);
        textViewYourPoints = view.findViewById(R.id.textViewYourPoints);
        textViewOpponentHand = view.findViewById(R.id.textViewOpponentHand);
        textViewOpponentPrediction = view.findViewById(R.id.textViewOpponentPrediction);
        final TextView textViewTime = view.findViewById(R.id.textViewTime);
        numberPicker = view.findViewById(R.id.numberPicker);
        imageButtons[0] = view.findViewById(R.id.imageButtonManoUno);
        imageButtons[1] = view.findViewById(R.id.imageButtonManoDue);
        imageButtons[2] = view.findViewById(R.id.imageButtonManoTre);
        imageButtons[3] = view.findViewById(R.id.imageButtonManoQuattro);
        imageButtons[4] = view.findViewById(R.id.imageButtonManoCinque);
        userId = SessionSingleton.getInstance().getUserId();

        //((ViewGroup) imageButton1.getParent()).removeView(imageButton1);
        //((ViewGroup) imageButton1.getParent()).addView(imageButton1);

        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(2);

        textViewPrediction.setText("HAI SCELTO DI GRIDARE: 0");
        textViewYourPoints.setText("Tu: 0");
        textViewOpponentPoints.setText("Avversario: 0");

        for(int i = 0; i < 4; i++) {
            final int finalI = i; // deve essere final per poter essere passata a setHand
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setHand(finalI + 1);
                }
            });
        }

        final long msToStart = roundStartTime.getMillis() - DateTime.now().getMillis();
        //TODO: fare in modo che il countdown si resetti ogni volta
        countDownTimer = new CountDownTimer(msToStart, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText("Ti rimangono " + msToStart / 1000 + " per fare la mossa");
            }

            @Override
            public void onFinish() {
            }
        }.start();


        //MatchSingleton.getInstance().setHand(hand);
        //MatchSingleton.getInstance().setPrediction(prediction);

        pollingThreadMatch = new PollingThreadMatch(MatchScreen.this.getContext().getApplicationContext(), matchId, roundStartTime, lastRoundResultsTime, new MatchCallback() {
            @Override
            public int getUserPrediction() {
                return numberPicker.getValue();
            }

            @Override
            public int getUserHand() {
                return hand;
            }

            @Override
            public void moveSet(boolean success) {
                // TODO: error handling, nascondere input
            }

            @Override
            public void lastRoundDataReceived(DateTime nextRoundStart, int hand1, int hand2, int prediction1, int prediction2, int punti1, int punti2) {
                roundStartTime = nextRoundStart;
                if (userId == matchUserId1) {
                    textViewYourPoints.setText("Tu: " + punti1);
                    textViewOpponentPoints.setText("Avversario: " + punti2);
                    textViewOpponentHand.setText("Il tuo avversario ha buttato: " + hand2);
                    textViewOpponentPrediction.setText("Il tuo avversario ha urlato: " + prediction2);
                } else {
                    textViewYourPoints.setText("Tu: " + punti1);
                    textViewOpponentPoints.setText("Avversario: " + punti2);
                    textViewOpponentHand.setText("Il tuo avversario ha buttato: " + hand1);
                    textViewOpponentPrediction.setText("Il tuo avversario ha urlato: " + prediction1);
                }
            }
        });
        Thread thread = new Thread(pollingThreadMatch);
        thread.start();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pollingThreadMatch != null)
            pollingThreadMatch.running = false;
    }
}
