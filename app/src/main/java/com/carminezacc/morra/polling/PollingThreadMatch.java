package com.carminezacc.morra.polling;

import android.content.Context;
import android.util.Log;


import com.carminezacc.morra.backend.LastRoundCallback;
import com.carminezacc.morra.backend.MatchInfoCallback;
import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.backend.SetMoveHandler;
import com.carminezacc.morra.models.LastRound;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.state.MatchSingleton;

import org.joda.time.DateTime;

public class PollingThreadMatch implements Runnable{

    Context context;
    DateTime startTime;
    DateTime lastRoundTime;
    Match match;
    MatchInfoCallback handler;
    public boolean running = true;
    int hand;
    int prediction;
    int lastPunti1 = 0;
    int lastPunti2 = 0;
    MatchSingleton matchSingleton = MatchSingleton.getInstance();

    public PollingThreadMatch(Context context, DateTime startTime, DateTime lastRoundTime, MatchInfoCallback handler) {
        this.context = context;
        this.startTime = startTime;
        this.lastRoundTime = lastRoundTime;
        this.handler = handler;
    }



    @Override
    public void run() {
        if(!running){
            return;
        }
        try {
            Log.d("startTimeThread", String.valueOf((startTime.getMillis() - new DateTime().getMillis()) / 1000));
            Thread.sleep(startTime.getMillis() - new DateTime().getMillis());
            if(!running){
                return;
            }
            hand = matchSingleton.getHand();
            prediction = matchSingleton.getPrediction();
            match = matchSingleton.getMatchData();
            Log.d("matchId", String.valueOf(match.getId()));
            Log.d("hand", String.valueOf(hand));
            Log.d("prediction", String.valueOf(prediction));
            Matches.setMove(match.getId(), hand, prediction, context, new SetMoveHandler() {
                @Override
                public void handleSetMove(boolean success) {
                    if(!success){
                        //TODO: ERROR HANDLING
                        handler.handleSetMoveSuccess(false);
                    }else{
                        handler.handleSetMoveSuccess(true);
                    }
                }
            });
            Log.d("lastRoundTimeThread", String.valueOf((lastRoundTime.getMillis() - new DateTime().getMillis()) / 1000));
            Thread.sleep(lastRoundTime.getMillis() - new DateTime().getMillis());
            Matches.lastRound(match.getId(), context, new LastRoundCallback() {
                @Override
                public void resultReturned(LastRound lastRound) {
                    //TODO: ricordarsi che lastround va usato per dare le info delle mani e delle predizioni
                    startTime = lastRound.getNextRoundStart();
                    lastRoundTime = lastRound.getNextRoundResults();
                    if(lastPunti1 != lastRound.getCurPoints1()){
                        matchSingleton.incrementPunti1();
                        lastPunti1 = lastRound.getCurPoints1();
                    }
                    if(lastPunti2 != lastRound.getCurPoints2()){
                        matchSingleton.incrementPunti2();
                        lastPunti2 = lastRound.getCurPoints2();
                    }
                    handler.dateCallback(startTime, lastRoundTime);
                    handler.moveCallback(lastRound.getHand1(), lastRound.getPrediction1(), lastRound.getHand2(), lastRound.getPrediction2());
                    run();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
