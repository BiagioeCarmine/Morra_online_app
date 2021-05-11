package com.carminezacc.morra.state;

import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;

/**
 * Classe usata per manterenere i dati della partita in memoria.
 * Trattandosi di un Singleton, il costruttore è privato
 * e si accede all'unica istanza esistente attraverso il
 * metodo {@link #getInstance()}.
 */
public class MatchSingleton {

    /**
     * Istanza unica del singleton
     */
    static private MatchSingleton instance = null;

    Match matchData = null;
    User user1 = null;
    User user2 = null;

    /**
     * @return Dati della partita corrente.
     */
    public Match getMatchData() {
        return matchData;
    }

    /**
     * @return Dati dell'utente 1 della partita corrente.
     */
    public User getUser1() {
        return user1;
    }

    /**
     * @return Dati dell'utente 2 della partita corrente.
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Costruttore privato per assicurarsi che la classe non venga istanziata più di una volta.
     */
    private MatchSingleton() {}

    /**
     * Imposta i dati della partita da giocare. Da eseguire
     */
    public void setMatchData(Match match, User user1, User user2) {
        this.matchData = match;
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Elimina i dati contenuti nel Singleton.
     */
    public void wipe() {
        this.matchData = null;
        this.user1 = null;
        this.user2 = null;
    }


    /**
     * Metodo usato per ottenere l'istanza del Singleton, che è unica per l'intera app in
     * esecuzione (trattandosi di un singleton sarebbe scorretto il contrario, ovviamente),
     * questo metodo la crea se non esiste ancora.
     * @return istanza del Singleton
     */
    static public MatchSingleton getInstance() {
        if(instance == null) {
            instance = new MatchSingleton();
        }

        return instance;
    }
}
