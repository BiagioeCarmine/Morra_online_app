package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.state.SessionSingleton;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Matches {
    static final String url = "https://morra.carminezacc.com";
    static final Gson gson = new Gson();

    public static void getMatch(int matchId, Context context, final MatchResultCallback handler) {
        String path = "/matches/"+ matchId; // MAGGICO FA DA SOLO String.valueOf(matchId)
        SessionSingleton session = SessionSingleton.getInstance();
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        final String jwt = session.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.resultReturned(gson.fromJson(response, Match.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: Error handling
            }
        }
        );
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
