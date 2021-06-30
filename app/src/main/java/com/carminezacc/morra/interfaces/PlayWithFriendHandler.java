package com.carminezacc.morra.interfaces;
/**
 * Interfaccia usata per la comunicazione tra il thread di Volley che fa una richiesta di giocare una partita con un
 * utente specifico e il thread UI
 */
public interface PlayWithFriendHandler {
    void handlerPlayWithFriend (boolean success, int matchId);
}
