package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class Users {
    static final String url = "https://morra.carminezacc.com";

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
    public static void LogIn(final String username, final String password, Context context, final LogInHandler handler) {
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
}
