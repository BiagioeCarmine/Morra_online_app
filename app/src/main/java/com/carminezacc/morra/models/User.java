package com.carminezacc.morra.models;

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

    public int getId() {
        return id;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public int getSconfitte() {
        return sconfitte;
    }

    public int getVittorie() {
        return vittorie;
    }

    public String getUsername() {
        return username;
    }
}
