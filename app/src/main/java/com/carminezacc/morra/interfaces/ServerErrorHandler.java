package com.carminezacc.morra.interfaces;


public interface ServerErrorHandler {
    /**
     * Funzione chiamata quando c'è un errore che si può probabilmente attribuire ad un malfunzionamento
     * del backend.
     * @param statusCode: status code della response oppure 0 se errore dovuto a formattazione
     *                  errata della risposta (nel caso di richieste di oggetti JSON)
     */
    void error(int statusCode);
}
