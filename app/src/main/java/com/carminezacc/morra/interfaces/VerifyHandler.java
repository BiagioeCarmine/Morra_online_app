package com.carminezacc.morra.interfaces;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede di verificare un utente e il
 * thread UI
 */
public interface VerifyHandler {
    void handleVerify(boolean success, int userId);

}
