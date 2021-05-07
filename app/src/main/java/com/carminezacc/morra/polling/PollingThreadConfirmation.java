package com.carminezacc.morra.polling;

import android.content.Context;

import com.carminezacc.morra.backend.MatchResultCallback;
import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.models.Match;

public class PollingThreadConfirmation implements Runnable {
    Context context;
    MatchResultCallback handler;
    int matchId;
    boolean running = true;

    public PollingThreadConfirmation(int matchId, Context context, MatchResultCallback handler) {
        this.context = context;
        this.matchId = matchId;
        this.handler = handler;
    }

    @Override
    public void run() {
        if(!running){
            return;
        }
        try {
            Thread.sleep(800);
            if(!running){
                return;
            }
            Matches.getMatch(matchId, context, new MatchResultCallback() {
                @Override
                public void resultReturned(Match match) {
                    if(!match.isConfirmed()) {
                        run();
                    } else {
                        handler.resultReturned(match);
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
