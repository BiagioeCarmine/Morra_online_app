package com.carminezacc.morra.runnables;

import android.content.Context;

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
    final Object sync = new Object();

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
            synchronized (sync) {
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
                            synchronized (sync) {
                                sync.notify();
                            }
                        }

                        @Override
                        public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                            if (inQueue) {
                                pollTime = pollBefore;
                                synchronized (sync) {
                                    sync.notify();
                                }
                            } else {
                                if (queueIsPublic) {
                                    Matchmaking.addToPublicQueue(context, new QueueStatusHandler() {
                                        @Override
                                        public void handleMatchCreation(int matchId) {
                                            handler.handleMatchCreation(matchId);
                                            running = false;
                                            synchronized (sync) {
                                                sync.notify();
                                            }
                                        }

                                        @Override
                                        public void handlePollingRequired(boolean inQueue, DateTime pollBefore) {
                                            pollTime = pollBefore;
                                            synchronized (sync) {
                                                sync.notify();
                                            }
                                        }
                                    }, new ServerErrorHandler() {
                                        @Override
                                        public void error(int statusCode) {
                                            serverErrorHandler.error(statusCode);
                                            running = false;
                                            synchronized (sync) {
                                                sync.notify();
                                            }
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
                                }
                            }
                        }
                    }, new ServerErrorHandler() {
                        @Override
                        public void error(int statusCode) {
                            running = false;
                            serverErrorHandler.error(statusCode);
                            synchronized (sync) {
                                sync.notify();
                            }

                        }
                    });
                    sync.wait();
                } catch (InterruptedException e) {
                    // TODO: bisogno di fare error handling per questo??
                    e.printStackTrace();
                }
            }
        }
    }
}
