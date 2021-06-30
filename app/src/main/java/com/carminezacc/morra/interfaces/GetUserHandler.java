package com.carminezacc.morra.interfaces;

import com.carminezacc.morra.models.User;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede un utente e il
 * thread UI
 */
public interface GetUserHandler {
    void resultReturned(User user);
}
