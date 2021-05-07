package com.carminezacc.morra.backend;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.state.SessionSingleton;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Matchmaking {
    static final String url = "https://morra.carminezacc.com";

    public static void addToPublicQueue(Context context, final QueueStatusHandler handler) {
        String path = "/mm/queue";
        SessionSingleton session = SessionSingleton.getInstance();
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        final String jwt = session.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject response = new JSONObject(res);
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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("public queue", String.valueOf(error.networkResponse.statusCode));
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map getParams() {
                Map params = new HashMap();
                params.put("type", "public");
                return params;
            }
            @Override
            public Map getHeaders() {
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void addToPrivateQueue(Context context, final QueueStatusHandler handler){
        String path = "mm/queue";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: Error handling
            }
        }
        ){
            @Override
            public Map getHeaders() {
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
            @Override
            protected Map getParams() {
                Map params = new HashMap();
                params.put("type", "private");
                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public static void playWithFriend(final String userId, Context context, final PlayWithFriendHandler handler){
        String path = "/mm/play_with_friend";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                try {
                    JSONObject response = new JSONObject(res);
                    handler.handlerPlayWithFriend(true, response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.handlerPlayWithFriend(false, 0);
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
            public Map getHeaders() {
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
            @Override
            protected Map getParams() {
                Map params = new HashMap();
                params.put("user", userId);
                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void queueStatus(Context context, final QueueStatusHandler handler){
        String path = "/mm/queue_status";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: Error handling
            }
        }
        ){
            @Override
            public Map getHeaders(){
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
