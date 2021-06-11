package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.interfaces.LastRoundCallback;
import com.carminezacc.morra.interfaces.MatchResultCallback;
import com.carminezacc.morra.interfaces.SetMoveHandler;
import com.carminezacc.morra.models.LastRound;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.state.SessionSingleton;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class Matches {

    /**
     * Deserializer per il DateTime di Joda-time come visto nella <a href="https://github.com/google/gson/blob/master/UserGuide.md#writing-a-deserializer">
     * documentazione ufficiale di GSON</a>.
     */
    static private class DateTimeDeserializer implements JsonDeserializer<DateTime> {
        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new DateTime(json.getAsJsonPrimitive().getAsString());
        }
    }


    static final String url = "https://morra.carminezacc.com";
    static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
            .create();


    public static void getMatch(int matchId, Context context, final MatchResultCallback handler) {
        String path = "/matches/" + matchId; // MAGGICO FA DA SOLO String.valueOf(matchId)
        SessionSingleton session = SessionSingleton.getInstance();
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
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
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void setMove(int matchId, final int hand, final int prediction, Context context, final SetMoveHandler handler){
        String path = "/matches/" + matchId + "/move";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.handleSetMove(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.handleSetMove(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hand", String.valueOf(hand));
                params.put("prediction", String.valueOf(prediction));
                return params;
            }
            @Override
            public Map getHeaders() {
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void lastRound(int matchId, Context context, final LastRoundCallback handler){
        // TODO:gestire fine partita
        String path = "/matches/" + matchId + "/last_round";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.resultReturned(gson.fromJson(response, LastRound.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
