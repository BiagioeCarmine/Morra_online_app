package com.carminezacc.morra.backend;

import org.joda.time.DateTime;

public interface QueueStatusHandler {
    void handleMatchCreation(int matchId);
    void handlePolling(boolean inQueue, DateTime pollBefore);
}
