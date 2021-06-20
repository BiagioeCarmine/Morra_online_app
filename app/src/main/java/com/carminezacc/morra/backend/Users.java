package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.carminezacc.morra.interfaces.GetClassificaHandler;
import com.carminezacc.morra.interfaces.GetUserHandler;
import com.carminezacc.morra.interfaces.LogInHandler;
import com.carminezacc.morra.interfaces.ServerErrorHandler;
import com.carminezacc.morra.interfaces.SignUpHandler;
import com.carminezacc.morra.interfaces.VerifyHandler;
import com.carminezacc.morra.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {
    static final String url = "https://morra.carminezacc.com";
    static final Gson gson = new Gson();

    public static void signUp(final String username, final String password, Context context, final SignUpHandler handler, final ServerErrorHandler serverErrorHandler) {
        String path = "/users/signup";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.handleSignUp(true);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 409) {
                    handler.handleSignUp(false);
                } else {
                    error.printStackTrace();
                    serverErrorHandler.error(error.networkResponse.statusCode);
                }

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void logIn(final String username, final String password, Context context, final LogInHandler handler, final ServerErrorHandler serverErrorHandler) {
        String path = "/users/login";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.handleLogIn(true, response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 401)
                    handler.handleLogIn(false, null);
                else {
                    error.printStackTrace();
                    serverErrorHandler.error(error.networkResponse.statusCode);
                }
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void verify(final String jwt, Context context, final VerifyHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/verify";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    handler.handleVerify(true, response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverErrorHandler.error(0);
                }
            }
        }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error){
                    if(error.networkResponse.statusCode == 401)
                        handler.handleVerify(false, 0);
                    else {
                        error.printStackTrace();
                        serverErrorHandler.error(error.networkResponse.statusCode);
                    }
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void getUser(int userId, Context context, final GetUserHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/user/" + userId;
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handler.resultReturned(gson.fromJson(response, User.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                serverErrorHandler.error(error.networkResponse.statusCode);
            }
        }
        );
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void getRanking(Context context, final GetClassificaHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/?order_by=punteggio&descending=true&n=5";
        RequestQueue queue = VolleyRequestQueueSingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User[] userList = gson.fromJson(response, User[].class);
                handler.resultReturned(userList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                serverErrorHandler.error(error.networkResponse.statusCode);
            }
        });/*{
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_by", "punteggio");
                params.put("descending", "true");
                params.put("n", "5");
                return params;
            }
        };*/
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
