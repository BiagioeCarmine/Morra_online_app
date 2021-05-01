package com.carminezacc.morra.models;


import org.joda.time.DateTime;

public class LastRound {
    int curPoints1, curPoints2, hand1, hand2, prediction1, prediction2;
    DateTime nextRoundStart, nextRoundResults;

    public LastRound(int curPoints1, int curPoints2, int hand1, int hand2, int prediction1, int prediction2, DateTime nextRoundStart, DateTime nextRoundResults) {
        this.curPoints1 = curPoints1;
        this.curPoints2 = curPoints2;
        this.hand1 = hand1;
        this.hand2 = hand2;
        this.prediction1 = prediction1;
        this.prediction2 = prediction2;
        this.nextRoundStart = nextRoundStart;
        this.nextRoundResults = nextRoundResults;
    }

    public int getCurPoints1() {
        return curPoints1;
    }

    public int getCurPoints2() {
        return curPoints2;
    }

    public int getHand1() {
        return hand1;
    }

    public int getHand2() {
        return hand2;
    }

    public int getPrediction1() {
        return prediction1;
    }

    public int getPrediction2() {
        return prediction2;
    }

    public DateTime getNextRoundStart() {
        return nextRoundStart;
    }

    public DateTime getNextRoundResults() {
        return nextRoundResults;
    }
}
