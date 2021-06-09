package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.carminezacc.morra.backend.MatchInfoCallback;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;
import com.carminezacc.morra.polling.PollingThreadMatch;
import com.carminezacc.morra.state.MatchSingleton;
import com.carminezacc.morra.state.SessionSingleton;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.DateTime;

import java.util.Objects;

public class MatchScreen extends Fragment {

    private TextView textViewPrediction;
    TextView textViewOpponentPoints;
    TextView textViewYourPoints;
    TextView textViewOpponentHand;
    TextView textViewOpponentPrediction;
    public NumberPicker numberPicker;
    public ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5;
    boolean isPressed;
    boolean isClicked = false;
    int isColored = 0;
    int hand, prediction;
    CountDownTimer countDownTimer;
    Match match;
    int user;
    long secondStartTime;
    long millisStartTime;
    DateTime startTime;
    DateTime lastRoundTime;
    PollingThreadMatch pollingThreadMatch;


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
        imageButton1 = view.findViewById(R.id.imageButtonManoUno);
        imageButton2 = view.findViewById(R.id.imageButtonManoDue);
        imageButton3 = view.findViewById(R.id.imageButtonManoTre);
        imageButton4 = view.findViewById(R.id.imageButtonManoQuattro);
        imageButton5 = view.findViewById(R.id.imageButtonManoCinque);
        match = MatchSingleton.getInstance().getMatchData();
        user = SessionSingleton.getInstance().getUserId();
        startTime = match.getStartTime();

        lastRoundTime = match.getFirstRoundResults();

        //((ViewGroup) imageButton1.getParent()).removeView(imageButton1);
        //((ViewGroup) imageButton1.getParent()).addView(imageButton1);

        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(2);

        textViewPrediction.setText("HAI SCELTO DI GRIDARE: 0");
        textViewYourPoints.setText("Tu: " + match.getPunti1());
        textViewOpponentPoints.setText("Avversario: " + match.getPunti2());


        millisStartTime = (startTime.getMillis() - new DateTime().getMillis()) - 2000;
        Log.d("Time", String.valueOf(millisStartTime / 1000));


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textViewPrediction.setText("HAI SCELTO DI GRIDARE: " + newVal);
                prediction = newVal;
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    if(!isClicked){
                        v.setBackgroundResource(R.color.colorBackgroundButton);
                        hand = 1;
                        isClicked = true;
                        isColored = 1;
                    }
                }else if(isColored == 1){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    if(!isClicked){
                        v.setBackgroundResource(R.color.colorBackgroundButton);
                        hand = 2;
                        isClicked = true;
                        isColored = 2;
                    }
                }else if(isColored == 2){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    if(!isClicked){
                        v.setBackgroundResource(R.color.colorBackgroundButton);
                        hand = 3;
                        isClicked = true;
                        isColored = 3;
                    }
                }else if(isColored == 3){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    if(!isClicked){
                        v.setBackgroundResource(R.color.colorBackgroundButton);
                        hand = 4;
                        isClicked = true;
                        isColored = 4;
                    }
                }else if(isColored == 4){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    if(!isClicked){
                        v.setBackgroundResource(R.color.colorBackgroundButton);
                        hand = 5;
                        isClicked = true;
                        isColored = 5;
                    }
                }else if(isColored == 5){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });

        //TODO: fare in modo che il countdown si resetti ogni volta
        countDownTimer = new CountDownTimer(millisStartTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondStartTime =  millisUntilFinished / 1000;
                textViewTime.setText("Ti rimangono " + secondStartTime + " per fare la mossa");
            }

            @Override
            public void onFinish() {
                MatchSingleton.getInstance().setHand(hand);
                MatchSingleton.getInstance().setPrediction(prediction);
            }
        }.start();


        //MatchSingleton.getInstance().setHand(hand);
        //MatchSingleton.getInstance().setPrediction(prediction);

        pollingThreadMatch = new PollingThreadMatch(Objects.requireNonNull(MatchScreen.this.getContext()).getApplicationContext(), startTime, lastRoundTime, new MatchInfoCallback() {
            @Override
            public void dateCallback(DateTime startTimeCallback, DateTime lastRoundTimeCallback) {
                //TODO: parlare con Carmine del perche il tempo non è sync con i vari utenti
                startTime = startTimeCallback;
                lastRoundTime = lastRoundTimeCallback;
                millisStartTime = (startTime.getMillis() - new DateTime().getMillis()) - 2000;
                /*
                countDownTimer = new CountDownTimer(millisStartTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        secondStartTime =  millisUntilFinished / 1000;
                        Log.d("SecondStartTime", String.valueOf(secondStartTime));
                        textViewTime.setText("Ti rimangono " + secondStartTime + " per fare la mossa");
                    }

                    @Override
                    public void onFinish() {
                        MatchSingleton.getInstance().setHand(hand);
                        MatchSingleton.getInstance().setPrediction(prediction);
                    }
                }.start();

                 */
            }

            @Override
            public void moveCallback(int hand1, int prediction1, int hand2, int prediction2) {
                //TODO: parlare con Carmine del perchè a volte assegna il punto a chi non dovrebbe
                Log.d("userid1DaMatch", String.valueOf(match.getUserid1()));
                Log.d("userId1", String.valueOf(user));
                Log.d("hand1", String.valueOf(hand1));
                Log.d("prediction1", String.valueOf(prediction1));
                Log.d("hand2", String.valueOf(hand2));
                Log.d("prediction2", String.valueOf(prediction2));
                if(match.getUserid1() == user){
                    textViewYourPoints.setText("Tu: " + match.getPunti1());
                    textViewOpponentPoints.setText("Avversario: " + match.getPunti2());
                    textViewOpponentHand.setText("Il tuo avversario ha buttato: " + hand2);
                    textViewOpponentPrediction.setText("Il tuo avversario ha urlato: " + prediction2);
                }else{
                    textViewYourPoints.setText("Tu: " + match.getPunti2());
                    textViewOpponentPoints.setText("Avversario: " + match.getPunti1());
                    textViewOpponentHand.setText("Il tuo avversario ha buttato: " + hand1);
                    textViewOpponentPrediction.setText("Il tuo avversario ha urlato: " + prediction1);
                }
            }

            @Override
            public void handleSetMoveSuccess(boolean success) {
                if(success){
                    //TODO: Capire cosa fare con questo handler
                    //Snackbar.make(view, "La mossa e' stata settata", Snackbar.LENGTH_LONG).show();
                }else{
                    //Snackbar.make(view, "La mossa NON e' stata settata", Snackbar.LENGTH_LONG).show();
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
