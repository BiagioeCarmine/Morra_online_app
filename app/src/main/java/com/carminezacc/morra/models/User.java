package com.carminezacc.morra.models;

/**
 * Classe che rappresenta il modello di un utente nell'app come appare nel db
 */
public class User {
    int id, punteggio, sconfitte, vittorie;
    String username;

    public User(int id, int punteggio, int sconfitte, int vittorie, String username) {
        this.id = id;
        this.punteggio = punteggio;
        this.sconfitte = sconfitte;
        this.vittorie = vittorie;
        this.username = username;
    }

    /**
     * Metodo che ritorna l'id di un utente
     */
    public int getId() {
        return id;
    }
    /**
     * Metodo che ritorna il punteggio di un utente
     */
    public int getPunteggio() {
        return punteggio;
    }
    /**
     * Metodo che ritorna le sconfitte di un utente
     */
    public int getSconfitte() {
        return sconfitte;
    }
    /**
     * Metodo che ritorna le vittorie di un utente
     */
    public int getVittorie() {
        return vittorie;
    }
    /**
     * Metodo che ritorna l'username di un utente
     */
    public String getUsername() {
        return username;
    }
}
