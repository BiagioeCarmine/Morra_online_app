package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.carminezacc.morra.state.SessionSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Matchmaking {
    static final String url = "https://morra.carminezacc.com";

    public static void addToPublicQueue(){
        String path = "/mm/queue";
        SessionSingleton session = SessionSingleton.getInstance();
        String jwt = session.getToken();
        /*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TODO: IMPLEMENTARE IL POLLING
            }
        })*/

    }

    public static void addToPrivateQueue(){
        SessionSingleton session = SessionSingleton.getInstance();
        String jwt = session.getToken();

        //TODO: IMPLEMENTARE IL POLLING

    }


    public static void playWithFriend(final String userId, Context context, final PlayWithFriendHandler handler){
        String path = "/mm/play_with_friend";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        SessionSingleton session = SessionSingleton.getInstance();
        final String jwt = session.getToken();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
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
        QueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    //TODO: IMPLEMENTARE QUEUE_STATUS

}
