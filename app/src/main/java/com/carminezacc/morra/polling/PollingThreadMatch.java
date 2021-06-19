package com.carminezacc.morra.polling;

import android.content.Context;
import android.util.Log;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.interfaces.LastRoundCallback;
import com.carminezacc.morra.interfaces.MatchCallback;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.interfaces.SetMoveHandler;
import com.carminezacc.morra.models.LastRound;

import org.joda.time.DateTime;

public class PollingThreadMatch implements Runnable {

    Context context;
    DateTime nextRoundStartTime;
    DateTime nextRoundResultsTime;
    MatchCallback handler;
    ServerErrorHandler serverErrorHandler;
    public boolean running = true;
    int matchId;
    boolean waiting = true;

    public PollingThreadMatch(Context context, int matchId, DateTime nextRoundStartTime, DateTime nextRoundResultsTime, MatchCallback handler, ServerErrorHandler serverErrorHandler) {
        this.matchId = matchId;
        this.context = context;
        this.nextRoundStartTime = nextRoundStartTime;
        this.nextRoundResultsTime = nextRoundResultsTime;
        this.handler = handler;
        this.serverErrorHandler = serverErrorHandler;
    }


    @Override
    public void run() {
        while(running) {
            waiting = true;
            try {
                Thread.sleep(nextRoundStartTime.getMillis() - new DateTime().getMillis() - 1000);
                if (!running) {
                    return;
                }
                int hand = handler.getUserHand();
                int prediction = handler.getUserPrediction();
                Matches.setMove(matchId, hand, prediction, context, new SetMoveHandler() {
                    @Override
                    public void handleSetMove() {
                        handler.moveSet();
                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        serverErrorHandler.error(statusCode);
                    }
                });
                Thread.sleep(nextRoundResultsTime.getMillis() - new DateTime().getMillis());
                Matches.lastRound(matchId, context, new LastRoundCallback() {
                    @Override
                    public void resultReturned(LastRound lastRound) {
                        if (lastRound.getNextRoundStart() == null) {
                            running = false;
                            handler.matchFinished(lastRound.getCurPoints1(), lastRound.getCurPoints2());
                        } else {
                            nextRoundStartTime = lastRound.getNextRoundStart();
                            nextRoundResultsTime = lastRound.getNextRoundResults();
                            waiting = false;
                            handler.lastRoundDataReceived(nextRoundStartTime, lastRound.getHand1(), lastRound.getHand2(), lastRound.getPrediction1(), lastRound.getPrediction2(), lastRound.getCurPoints1(), lastRound.getCurPoints2());
                        }

                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        serverErrorHandler.error(statusCode);
                    }
                });
                while (waiting) { // TODO: fare in maniera più sensata
                    if (!running) return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
