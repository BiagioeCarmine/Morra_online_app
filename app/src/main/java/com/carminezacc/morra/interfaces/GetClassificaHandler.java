package com.carminezacc.morra.interfaces;

import com.carminezacc.morra.models.User;

/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che richiede la classifica e il
 * thread UI
 */
public interface GetClassificaHandler {
    /**
     * Questo metodo viene chiamato quando viene restituita dal backend la classifica dei primi n
     * utenti
     * @param userList array di utenti contenente i primi utenti in classifica
     */
    void resultReturned(User[] userList);

}
