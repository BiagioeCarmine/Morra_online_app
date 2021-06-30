package com.carminezacc.morra.models;

import org.joda.time.DateTime;

/**
 * Classe che rappresenta il modello di una partita nell'app come appare nel db
 */
public class Match {
    boolean confirmed, finished;
    int id, punti1, punti2, userid1, userid2;
    DateTime startTime, firstRoundResults, confirmationTime;

    /**
     * Metodo che ritorna l'istante in cui il backend decide di confermare o annulare la partita
     */
    public DateTime getConfirmationTime() {
        return confirmationTime;
    }

    public Match(boolean confirmed, boolean finished, int id, int punti1, int punti2, int userid1, int userid2, DateTime startTime, DateTime firstRoundResults, DateTime confirmationTime) {
        this.confirmed = confirmed;
        this.finished = finished;
        this.id = id;
        this.punti1 = punti1;
        this.punti2 = punti2;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.startTime = startTime;
        this.firstRoundResults = firstRoundResults;
        this.confirmationTime = confirmationTime;
    }

    /**
     * Metodo che ritorna true o false, a seconda se la partita è stata confermata o meno
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Metodo che ritorna true o false, a seconda se la partita è finita o meno
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Metodo che ritorna l'id di una partita
     */
    public int getId() {
        return id;
    }

    /**
     * Metodo che ritorna i punti fatti dall'utente con id uguale a {@link #userid1}
     */
    public int getPunti1() {
        return punti1;
    }
    /**
     * Metodo che ritorna i punti fatti dall'utente con id uguale a {@link #userid2}
     */
    public int getPunti2() {
        return punti2;
    }

    /**
     * Metodo che ritorna l'id del primo utente della partita
     */
    public int getUserid1() {
        return userid1;
    }
    /**
     * Metodo che ritorna l'id del secondo utente della partita
     */
    public int getUserid2() {
        return userid2;
    }

    /**
     * Metodo che ritorna l'istante in cui va inviata la prima mossa della partita
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * Metodo che ritorna l'istante in cui va inviata la richiesta per ricevere i risultati del
     * primo round
     */
    public DateTime getFirstRoundResults() {
        return firstRoundResults;
    }

    public void incrementPunti1() {
        punti1++;
    }

    public void incrementPunti2() {
        punti2++;
    }
}
