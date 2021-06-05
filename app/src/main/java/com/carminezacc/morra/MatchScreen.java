package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.polling.PollingThreadMatch;
import com.carminezacc.morra.state.MatchSingleton;

import org.joda.time.DateTime;

public class MatchScreen extends Fragment {

    private TextView textView;
    public NumberPicker numberPicker;
    public ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5;
    boolean isPressed;
    boolean isClicked = false;
    int isFree = 0;
    int hand, prediction;
    Match match;
    DateTime startTime;
    DateTime lastRoundTime;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.match_screen, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.textViewNumberPicker);
        numberPicker = view.findViewById(R.id.numberPicker);
        imageButton1 = view.findViewById(R.id.imageButtonManoUno);
        imageButton2 = view.findViewById(R.id.imageButtonManoDue);
        imageButton3 = view.findViewById(R.id.imageButtonManoTre);
        imageButton4 = view.findViewById(R.id.imageButtonManoQuattro);
        imageButton5 = view.findViewById(R.id.imageButtonManoCinque);

        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(2);

        textView.setText("HAI SCELTO DI GRIDARE: ");

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textView.setText("HAI SCELTO DI GRIDARE: " + newVal);
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
                        isFree = 1;
                    }
                }else if(isFree == 1){
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
                        isFree = 2;
                    }
                }else if(isFree == 2){
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
                        isFree = 3;
                    }
                }else if(isFree == 3){
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
                        isFree = 4;
                    }
                }else if(isFree == 4){
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
                        isFree = 5;
                    }
                }else if(isFree == 5){
                    v.setBackgroundResource(R.color.colorBackground);
                    isClicked = false;
                }
                isPressed = !isPressed;
            }
        });
/*TODO:
        MatchSingleton.getInstance().setHand(hand);
        MatchSingleton.getInstance().setPrediction(prediction);
        match = MatchSingleton.getInstance().getMatchData();
        startTime = match.getStartTime();
        lastRoundTime = match.getFirstRoundResults();
        PollingThreadMatch pollingThreadMatch = new PollingThreadMatch(MatchScreen.this.getContext().getApplicationContext(), startTime, lastRoundTime);

        Thread thread = new Thread(pollingThreadMatch);
        thread.start();
*/
    }
}
