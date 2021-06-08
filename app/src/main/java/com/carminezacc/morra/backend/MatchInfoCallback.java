package com.carminezacc.morra.backend;

import org.joda.time.DateTime;

/*
* TODO: DECIDERE SE A QUESTO PUNTO È MEGLIO CAMBIARE NOME A SetMoveHandler E METTERE TUTTO LI AL POSTO DI AVERE QUEST' ALTRA INTERFACCIA
* */
//TODO: AGGIUNGERE I COMMENTI
//TODO: Cambiare il nome ai metodi
/**
 * Questo è ilsd sd jsdahbasj  jh dsajd
 */
public interface MatchInfoCallback {
    void dateCallback(DateTime startTimeCallback, DateTime lastRoundTimeCallback);
    void moveCallback(int hand1, int prediction1, int hand2, int prediction2);
    void handleSetMoveSuccess(boolean success);
}
