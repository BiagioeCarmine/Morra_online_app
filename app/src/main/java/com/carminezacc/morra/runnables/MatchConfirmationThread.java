package com.carminezacc.morra.runnables;

import android.content.Context;

import com.carminezacc.morra.backend.Matches;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.models.Match;

import org.joda.time.DateTime;

public class MatchConfirmationThread implements Runnable {
    Context context;
    MatchResultCallback handler;
    ServerErrorHandler serverErrorHandler;
    Match match;

    public MatchConfirmationThread(Match match, Context context, MatchResultCallback handler, ServerErrorHandler serverErrorHandler) {
        this.context = context;
        this.match = match;
        this.handler = handler;
        this.serverErrorHandler = serverErrorHandler;
    }

    @Override
    public void run() {
            try {
                Thread.sleep(match.getConfirmationTime().getMillis()- DateTime.now().getMillis());
                Matches.getMatch(match.getId(), context, new MatchResultCallback() {
                    @Override
                    public void resultReturned(Match match) {
                        handler.resultReturned(match);
                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        serverErrorHandler.error(statusCode);
                    }
                });
            } catch (InterruptedException e) {
                // TODO: bisogno di fare error handling per questo??
                e.printStackTrace();
            }

        }

}
