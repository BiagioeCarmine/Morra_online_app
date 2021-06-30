package com.carminezacc.morra.interfaces;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede di settare una move e il
 * thread UI
 */
public interface SetMoveHandler {
    void handleSetMove();
}
