package com.carminezacc.morra.interfaces;

import org.joda.time.DateTime;

public interface QueueStatusHandler {
    void handleMatchCreation(int matchId);
    void handlePollingRequired(boolean inQueue, DateTime pollBefore);
}
