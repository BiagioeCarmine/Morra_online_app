package com.carminezacc.morra.interfaces;

import com.carminezacc.morra.models.Match;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede una partita e il
 * thread UI
 */
public interface MatchResultCallback {
    void resultReturned(Match match);
}
