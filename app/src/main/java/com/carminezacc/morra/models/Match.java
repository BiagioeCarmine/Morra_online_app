package com.carminezacc.morra.models;

import org.joda.time.DateTime;

public class Match {
    boolean confirmed, finished;
    int id, punti1, punti2, userid1, userid2;
    DateTime startTime, firstRoundResults;

    public Match(boolean confirmed, boolean finished, int id, int punti1, int punti2, int userid1, int userid2, DateTime startTime, DateTime firstRoundResults) {
        this.confirmed = confirmed;
        this.finished = finished;
        this.id = id;
        this.punti1 = punti1;
        this.punti2 = punti2;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.startTime = startTime;
        this.firstRoundResults = firstRoundResults;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getId() {
        return id;
    }

    public int getPunti1() {
        return punti1;
    }

    public int getPunti2() {
        return punti2;
    }

    public int getUserid1() {
        return userid1;
    }

    public int getUserid2() {
        return userid2;
    }

    public DateTime getStartTime() {
        return startTime;
    }

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
