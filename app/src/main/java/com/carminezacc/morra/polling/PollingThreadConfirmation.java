package com.carminezacc.morra.polling;

import android.content.Context;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.models.Match;

public class PollingThreadConfirmation implements Runnable {
    Context context;
    MatchResultCallback handler;
    int matchId;
    public boolean running = true;
    private boolean waiting = true;

    public PollingThreadConfirmation(int matchId, Context context, MatchResultCallback handler) {
        this.context = context;
        this.matchId = matchId;
        this.handler = handler;
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
                        if(!match.isConfirmed()) {
                            waiting = false;
                        } else {
                            handler.resultReturned(match);
                            running = false;
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(waiting) {
                if(!running) return;
            }
        }

    }
}
