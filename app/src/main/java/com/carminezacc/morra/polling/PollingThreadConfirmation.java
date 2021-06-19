package com.carminezacc.morra.polling;

import android.content.Context;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.Match;

public class PollingThreadConfirmation implements Runnable {
    Context context;
    MatchResultCallback handler;
    ServerErrorHandler serverErrorHandler;
    int matchId;
    public boolean running = true;
    private boolean waiting = true;

    public PollingThreadConfirmation(int matchId, Context context, MatchResultCallback handler, ServerErrorHandler serverErrorHandler) {
        this.context = context;
        this.matchId = matchId;
        this.handler = handler;
        this.serverErrorHandler = serverErrorHandler;
    }

    @Override
    public void run() {
        while(running) {
            waiting = true;
            try {
                Thread.sleep(800);
                if(!running){
                    return;
                }
                Matches.getMatch(matchId, context, new MatchResultCallback() {
                    @Override
                    public void resultReturned(Match match) {
                        if (!match.isConfirmed()) {
                            waiting = false;
                        } else {
                            handler.resultReturned(match);
                            running = false;
                        }
                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        serverErrorHandler.error(statusCode);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(waiting) { // TODO: fare in maniera più sensata
                if(!running) return;
            }
        }

    }
}
