package com.carminezacc.morra.interfaces;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede di eseguire la registrazione e il
 * thread UI
 */
public interface SignUpHandler {
    void handleSignUp(boolean success);
}
