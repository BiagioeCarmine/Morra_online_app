package com.carminezacc.morra.polling;

import android.content.Context;


import com.carminezacc.morra.backend.LastRoundCallback;
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
    public boolean running = true;
    int hand;
    int prediction;
    MatchSingleton matchSingleton = MatchSingleton.getInstance();

    public PollingThreadMatch(Context context, DateTime startTime, DateTime lastRoundTime) {
        this.context = context;
        this.startTime = startTime;
        this.lastRoundTime = lastRoundTime;
    }



    @Override
    public void run() {
        if(!running){
            return;
        }
        try {
            Thread.sleep(startTime.getMillis() - new DateTime().getMillis());
            if(!running){
                return;
            }
            hand = matchSingleton.getHand();
            prediction = matchSingleton.getPrediction();
            match = matchSingleton.getMatchData();
            Matches.setMove(match.getId(), hand, prediction, context, new SetMoveHandler() {
                @Override
                public void handleSetMove(boolean success) {
                    if(!success){
                        //TODO: ERROR HANDLING
                    }
                }
            });
            Thread.sleep(lastRoundTime.getMillis() - new DateTime().getMillis());
            Matches.lastRound(match.getId(), context, new LastRoundCallback() {
                @Override
                public void resultReturned(LastRound lastRound) {
                    //TODO: ricordarsi che lastround va usato per dare le info delle mani e delle predizioni
                    startTime = lastRound.getNextRoundStart();
                    lastRoundTime = lastRound.getNextRoundResults();
                    run();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
