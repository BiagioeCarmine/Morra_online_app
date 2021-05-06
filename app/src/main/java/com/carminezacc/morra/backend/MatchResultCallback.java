package com.carminezacc.morra.backend;

import com.carminezacc.morra.models.Match;

public interface MatchResultCallback {
    void resultReturned(Match match);
}
