package com.carminezacc.morra.polling;

import android.content.Context;

import com.carminezacc.morra.MatchMakingScreen;
import com.carminezacc.morra.backend.Matchmaking;
import com.carminezacc.morra.backend.QueueStatusHandler;

import org.joda.time.DateTime;


public class PollingThreadQueue implements Runnable {
    Context context;
    DateTime pollTime;
    boolean queueIsPublic;
    QueueStatusHandler handler;
    boolean waiting = true;
    public boolean running = true;

    public PollingThreadQueue(DateTime date, boolean queueIsPublic, Context context, QueueStatusHandler handler) {
        this.context = context;
        this.pollTime = date;
        this.handler = handler;
        this.queueIsPublic = queueIsPublic;
    }

    @Override
    public void run() {
        if(!running){
            return;
        }
        try {
            Thread.sleep(pollTime.getMillis() - new DateTime().getMillis());
            if(!running){
                return;
            }
            Matchmaking.queueStatus(context, new QueueStatusHandler() {
                @Override
                public void handleMatchCreation(int matchId) {
                    handler.handleMatchCreation(matchId);
                }

                @Override
                public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                    if (inQueue) {
                        pollTime = pollBefore;
                        run();
                    }
                    else{
                        if(queueIsPublic){
                            Matchmaking.addToPublicQueue(context, new QueueStatusHandler() {
                                @Override
                                public void handleMatchCreation(int matchId) {
                                    handler.handleMatchCreation(matchId);
                                }

                                @Override
                                public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                    pollTime = pollBefore;
                                    run();
                                }
                            });
                        }
                        else{
                            Matchmaking.addToPrivateQueue(context, new QueueStatusHandler() {
                                @Override
                                public void handleMatchCreation(int matchId) {
                                    // NON VERRÀ MAI CHIAMATO PERCHÉ LO GESTISCE IL THREAD
                                    handler.handleMatchCreation(matchId);
                                }

                                @Override
                                public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                    pollTime = pollBefore;
                                    run();
                                }
                            });
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
