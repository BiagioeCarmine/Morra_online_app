package com.carminezacc.morra.runnables;

import android.content.Context;

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
    /**
     * Booleano usato per fermare l'esecuzione del thread quando viene impostato a false
     */
    public boolean running = true;
    int matchId;
    Object sync = new Object();

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
                        running = false;
                        synchronized (sync) {
                            sync.notify();
                        }
                        serverErrorHandler.error(statusCode);
                    }
                });
                Thread.sleep(nextRoundResultsTime.getMillis() - new DateTime().getMillis());
                if(!running) break;
                Matches.lastRound(matchId, context, new LastRoundCallback() {
                    @Override
                    public void resultReturned(LastRound lastRound) {
                        if (lastRound.getNextRoundStart() == null) {
                            running = false;
                            handler.matchFinished(lastRound.getCurPoints1(), lastRound.getCurPoints2());
                        } else {
                            nextRoundStartTime = lastRound.getNextRoundStart();
                            nextRoundResultsTime = lastRound.getNextRoundResults();
                            handler.lastRoundDataReceived(nextRoundStartTime, lastRound.getHand1(), lastRound.getHand2(), lastRound.getPrediction1(), lastRound.getPrediction2(), lastRound.getCurPoints1(), lastRound.getCurPoints2());
                        }
                        synchronized (sync) {
                            sync.notify();
                        }


                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        running = false;
                        synchronized (sync) {
                            sync.notify();
                        }
                        serverErrorHandler.error(statusCode);
                    }
                });
                synchronized (sync) {
                    sync.wait();
                }
            } catch (InterruptedException e) {
                // TODO: bisogno di fare error handling per questo??
                e.printStackTrace();
            }
        }
    }
}
