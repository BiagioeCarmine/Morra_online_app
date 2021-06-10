package com.carminezacc.morra.polling;

import android.content.Context;
import android.util.Log;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.interfaces.LastRoundCallback;
import com.carminezacc.morra.interfaces.MatchCallback;
import com.carminezacc.morra.interfaces.SetMoveHandler;
import com.carminezacc.morra.models.LastRound;

import org.joda.time.DateTime;

public class PollingThreadMatch implements Runnable {

    Context context;
    DateTime nextRoundStartTime;
    DateTime nextRoundResultsTime;
    MatchCallback handler;
    public boolean running = true;
    int matchId;
    boolean waiting;

    public PollingThreadMatch(Context context, int matchId, DateTime nextRoundStartTime, DateTime nextRoundResultsTime, MatchCallback handler) {
        this.matchId = matchId;
        this.context = context;
        this.nextRoundStartTime = nextRoundStartTime;
        this.nextRoundResultsTime = nextRoundResultsTime;
        this.handler = handler;
    }


    @Override
    public void run() {
        try {
            waiting = true;
            Log.d("startTimeThread", String.valueOf((nextRoundStartTime.getMillis() - new DateTime().getMillis()) / 1000));
            Thread.sleep(nextRoundStartTime.getMillis() - new DateTime().getMillis());
            if (!running) {
                return;
            }
            int hand = handler.getUserHand();
            int prediction = handler.getUserPrediction();
            Log.d("matchId", String.valueOf(matchId));
            Log.d("hand", String.valueOf(hand));
            Log.d("prediction", String.valueOf(prediction));
            Matches.setMove(matchId, hand, prediction, context, new SetMoveHandler() {
                @Override
                public void handleSetMove(boolean success) {
                    handler.moveSet(success);
                }
            });
            Log.d("lastRoundTimeThread", String.valueOf((nextRoundResultsTime.getMillis() - new DateTime().getMillis()) / 1000));
            Thread.sleep(nextRoundResultsTime.getMillis() - new DateTime().getMillis());
            Matches.lastRound(matchId, context, new LastRoundCallback() {
                @Override
                public void resultReturned(LastRound lastRound) {
                    //TODO: ricordarsi che lastround va usato per dare le info delle mani e delle predizioni
                    nextRoundStartTime = lastRound.getNextRoundStart();
                    nextRoundResultsTime = lastRound.getNextRoundResults();
                    waiting=false;
                    handler.lastRoundDataReceived(nextRoundStartTime, lastRound.getHand1(), lastRound.getHand2(), lastRound.getPrediction1(), lastRound.getPrediction2(), lastRound.getCurPoints1(), lastRound.getCurPoints2());
                }
            });
            while(waiting) {
                if(!running) break;
            }
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
