package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.interfaces.PlayWithFriendHandler;
import com.carminezacc.morra.interfaces.QueueStatusHandler;
import com.carminezacc.morra.state.SessionSingleton;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Classe usata per accedere alle route del backend per la gestione del matchmaking
 */
public class Matchmaking {
    static final String url = "https://morra.carminezacc.com";

    /**
     * Metodo che aggiunge un utente alla public queue
     */
    public static void addToPublicQueue(Context context, final QueueStatusHandler handler, final ServerErrorHandler serverErrorHandler) {
        String path = "/mm/queue";
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject response = new JSONObject(res);
                    if (response.getBoolean("created")) {
                        handler.handleMatchCreation(response.getInt("match"));
                    } else if (response.getBoolean("inQueue")) {
                        handler.handlePollingRequired(true, new DateTime(DateTime.parse(response.getString("pollBefore"))));
                    } else {
                        handler.handlePollingRequired(false, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverErrorHandler.error(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                serverErrorHandler.error(error.networkResponse.statusCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", "public");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Metodo che aggiunge un utente alla private queue
     */
    public static void addToPrivateQueue(Context context, final QueueStatusHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/mm/queue";
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject response = new JSONObject(res);
                    if (response.getBoolean("inQueue")) {
                        handler.handlePollingRequired(true, new DateTime(DateTime.parse(response.getString("pollBefore"))));
                    } else {
                        handler.handlePollingRequired(false, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverErrorHandler.error(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                serverErrorHandler.error(error.networkResponse.statusCode);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", "private");
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Metodo che ti permette di giocare con un amico dato il suo Id
     */
    public static void playWithFriend(final String userId, Context context, final PlayWithFriendHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/mm/play_with_friend";
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject response = new JSONObject(res);
                    handler.handlerPlayWithFriend(true, response.getInt("match"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverErrorHandler.error(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.handlerPlayWithFriend(false, 0);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", userId);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Metodo usato per sapere se una partita è stata trovata o meno
     */
    public static void queueStatus(Context context, final QueueStatusHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/mm/queue_status";
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("created")) {
                        handler.handleMatchCreation(response.getInt("match"));
                    }
                    else if (response.getBoolean("inQueue")){
                        handler.handlePollingRequired(true, new DateTime(DateTime.parse(response.getString("pollBefore"))));
                    }
                    else{
                        handler.handlePollingRequired(false, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverErrorHandler.error(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                serverErrorHandler.error(error.networkResponse.statusCode);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
