package com.carminezacc.morra.interfaces;

import com.carminezacc.morra.models.LastRound;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede lastround e il
 * thread UI
 */
public interface LastRoundCallback {
    void resultReturned(LastRound lastRound);

}
