package com.carminezacc.morra.polling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.carminezacc.morra.R;
import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.interfaces.QueueStatusHandler;
import com.carminezacc.morra.interfaces.ServerErrorHandler;

import org.joda.time.DateTime;


public class PollingThreadQueue implements Runnable {

    Context context;
    DateTime pollTime;
    boolean queueIsPublic;
    QueueStatusHandler handler;
    ServerErrorHandler serverErrorHandler;
    public boolean running = true;
    private boolean waiting = true;

    public PollingThreadQueue(DateTime date, boolean queueIsPublic, Context context, QueueStatusHandler handler, ServerErrorHandler serverErrorHandler) {
        this.context = context;
        this.pollTime = date;
        this.handler = handler;
        this.queueIsPublic = queueIsPublic;
        this.serverErrorHandler = serverErrorHandler;
    }

    @Override
    public void run() {
        while(running) {
            waiting = true;
            try {
                Thread.sleep(pollTime.getMillis() - new DateTime().getMillis());
                if (!running) {
                    return;
                }
                Matchmaking.queueStatus(context, new QueueStatusHandler() {
                    @Override
                    public void handleMatchCreation(int matchId) {
                        handler.handleMatchCreation(matchId);
                        running = false;
                    }

                    @Override
                    public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                        if (inQueue) {
                            pollTime = pollBefore;
                            waiting = false;
                        } else {
                            if (queueIsPublic) {
                                Matchmaking.addToPublicQueue(context, new QueueStatusHandler() {
                                    @Override
                                    public void handleMatchCreation(int matchId) {
                                        handler.handleMatchCreation(matchId);
                                        running = false;
                                    }

                                    @Override
                                    public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                        pollTime = pollBefore;
                                        waiting = false;
                                    }
                                }, new ServerErrorHandler() {
                                    @Override
                                    public void error(int statusCode) {
                                        serverErrorHandler.error(statusCode);
                                    }
                                });
                            } else {
                                Matchmaking.addToPrivateQueue(context, new QueueStatusHandler() {
                                    @Override
                                    public void handleMatchCreation(int matchId) {
                                        // NON VERRÀ MAI CHIAMATO PERCHÉ NON È POSSIBILE CHE VENGA CREATA UNA PARTITA QUANDO TI AGGIUNGI ALLA PRIV QUEUE
                                    }

                                    @Override
                                    public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                        pollTime = pollBefore;
                                        waiting = false;
                                    }
                                }, new ServerErrorHandler() {
                                    @Override
                                    public void error(int statusCode) {
                                        running = false;
                                        serverErrorHandler.error(statusCode);
                                    }
                                });
                            }
                        }
                    }
                }, new ServerErrorHandler() {
                    @Override
                    public void error(int statusCode) {
                        running = false;
                        serverErrorHandler.error(statusCode);
                    }
                });
                while(waiting) { // TODO: fare in maniera più sensata
                    if(!running) return;
                }
            } catch (InterruptedException e) {
                // TODO: bisogno di fare error handling per questo??
                e.printStackTrace();
            }
        }
    }
}
