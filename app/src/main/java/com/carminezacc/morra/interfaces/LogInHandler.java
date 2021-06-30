package com.carminezacc.morra.interfaces;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede di eseguire il login e il
 * thread UI
 */
public interface LogInHandler {
    void handleLogIn(boolean success, String jwt);
}
