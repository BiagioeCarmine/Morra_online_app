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
            Thread.sleep(nextRoundStartTime.getMillis() - new DateTime().getMillis()-1000);
            Log.d("pollingThreadMatch", "t1");
            if (!running) {
                return;
            }
            int hand = handler.getUserHand();
            int prediction = handler.getUserPrediction();
            Log.d("pollingThreadMatch", "t2");
            Matches.setMove(matchId, hand, prediction, context, new SetMoveHandler() {
                @Override
                public void handleSetMove(boolean success) {
                    handler.moveSet(success);
                }
            });
            Thread.sleep(nextRoundResultsTime.getMillis() - new DateTime().getMillis());
            Matches.lastRound(matchId, context, new LastRoundCallback() {
                @Override
                public void resultReturned(LastRound lastRound) {
                    //TODO: partita finita
                    nextRoundStartTime = lastRound.getNextRoundStart();
                    nextRoundResultsTime = lastRound.getNextRoundResults();
                    waiting=false;
                    handler.lastRoundDataReceived(nextRoundStartTime, lastRound.getHand1(), lastRound.getHand2(), lastRound.getPrediction1(), lastRound.getPrediction2(), lastRound.getCurPoints1(), lastRound.getCurPoints2());
                }
            });
            while(waiting) {
                Log.d("pollingThreadMatch", "waiting");
                if(!running) break;
            }
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
