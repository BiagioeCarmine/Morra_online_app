package com.carminezacc.morra.interfaces;

import org.joda.time.DateTime;

/**
 * Interfaccia usata per la comunicazione tra il thread UI e il thread di PollingThreadMatch
 */
public interface MatchCallback {
    /**
     * Utilizzata per comunicazione di scelta mano utente da UI thread a PollingThreadMatch.
     *
     * @return mano scelta da utente
     */
    int getUserHand();

    /**
     * Utilizzata per comunicazione di scelta previsione utente da UI thread a PollingThreadMatch.
     *
     * @return previsione scelta da utente
     */
    int getUserPrediction();

    /**
     * Metodo che viene chiamato dopo che è stata impostata una mossa per un dato round
     */
    void moveSet();

    /**
     * Metodo che viene chiamato dopo che viene giocato un round e passa i risultati del dato round
     */
    void lastRoundDataReceived(DateTime nextRoundStart, int hand1, int hand2, int prediction1, int prediction2, int punti1, int punti2);

    /**
     * Metodo che viene chiamato quando la partita è finita e passa i risultati della partita
     */
    void matchFinished(int punti1, int punti2);
}
