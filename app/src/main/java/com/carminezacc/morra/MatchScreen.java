package com.carminezacc.morra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MatchScreen extends Fragment {

    private TextView textView;
    public NumberPicker numberPicker;
    private ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5;
    boolean isPressed;

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
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    v.setBackgroundResource(R.color.colorBackgroundButton);
                }else{
                    v.setBackgroundResource(R.color.colorBackground);
                }
                isPressed = !isPressed;
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    v.setBackgroundResource(R.color.colorBackgroundButton);
                }else{
                    v.setBackgroundResource(R.color.colorBackground);
                }
                isPressed = !isPressed;
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    v.setBackgroundResource(R.color.colorBackgroundButton);
                }else{
                    v.setBackgroundResource(R.color.colorBackground);
                }
                isPressed = !isPressed;
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    v.setBackgroundResource(R.color.colorBackgroundButton);
                }else{
                    v.setBackgroundResource(R.color.colorBackground);
                }
                isPressed = !isPressed;
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressed){
                    v.setBackgroundResource(R.color.colorBackgroundButton);
                }else{
                    v.setBackgroundResource(R.color.colorBackground);
                }
                isPressed = !isPressed;
            }
        });
    }
}
