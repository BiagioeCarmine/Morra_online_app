package com.carminezacc.morra.interfaces;

import org.joda.time.DateTime;

/**
 * Interfaccia usata per la comunicazione all'utente se è stata trovata o meno una partita
 */
public interface QueueStatusHandler {
    void handleMatchCreation(int matchId);
    void handlePollingRequired(boolean inQueue, DateTime pollBefore);
}
