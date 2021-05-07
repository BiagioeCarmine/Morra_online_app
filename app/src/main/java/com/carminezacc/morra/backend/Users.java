package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.models.Match;
import com.carminezacc.morra.models.User;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Users {
    static final String url = "https://morra.carminezacc.com";
    static final Gson gson = new Gson();

    public static void signUp(final String username, final String password, Context context, final SignUpHandler handler) {
        String path = "/users/signup";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.handleSignUp(true);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.handleSignUp(false);
            }
        }
        ) {
            @Override
            protected Map getParams() {
                Map params = new HashMap();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    public static void logIn(final String username, final String password, Context context, final LogInHandler handler) {
        String path = "/users/login";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.handleLogIn(true, response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.handleLogIn(false, null);
            }
        }
        ) {
            @Override
            protected Map getParams() {
                Map params = new HashMap();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    public static void verify(final String jwt, Context context, final VerifyHandler handler){
        String path = "/users/verify";
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + path, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    handler.handleVerify(true, response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.handleVerify(false, 0);
                }
            }
        }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error){
                    handler.handleVerify(false, 0);
                }
            }
        ){
            @Override
            public Map getHeaders() {
                Map params = new HashMap();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        QueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
    public static void getUser(int userId, Context context, final GetUserHandler handler){
        String path = "/users/user/" + userId;
        RequestQueue queue = QueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.resultReturned(gson.fromJson(response, User.class));
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
