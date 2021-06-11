package com.carminezacc.morra.interfaces;

import org.joda.time.DateTime;

/**
 * Questo è ilsd sd jsdahbasj  jh dsajd
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

    void moveSet(boolean success);

    void lastRoundDataReceived(DateTime nextRoundStart, int hand1, int hand2, int prediction1, int prediction2, int punti1, int punti2);

    void matchFinished(int punti1, int punti2);
}
