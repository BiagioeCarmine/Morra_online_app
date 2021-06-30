package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe usata per accedere alle route del backend per la gestione degli utenti
 */
public class Users {
    static final String url = "https://morra.carminezacc.com";
    static final Gson gson = new Gson();

    /**
     * Metodo usato per la registrazione di un utente all'app
     * @param username username dell'utente da registrare
     * @param password password dell'utente da registrare
     * @param context context attuale dell'app
     * @param handler interfaccia usata per comunicare l'avvenuta registrazione dell'utente
     * @param serverErrorHandler interfaccia usata per comunicare eventuali errori durante il
     *                           tentativo di registrazione
     */
    public static void signUp(final String username, final String password, Context context, final SignUpHandler handler, final ServerErrorHandler serverErrorHandler) {
        String path = "/users/signup";
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
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Metodo usato per far accedere un utente all'app
     */
    public static void logIn(final String username, final String password, Context context, final LogInHandler handler, final ServerErrorHandler serverErrorHandler) {
        String path = "/users/login";
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
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Metodo usato per verificare il token di un utente
     */
    public static void verify(final String jwt, Context context, final VerifyHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/verify";
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
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + jwt);
                return params;
            }
        };
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Metodo che ritorna un utente dato un certo Id
     */
    public static void getUser(int userId, Context context, final GetUserHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/user/" + userId;
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

    /**
     * Metodo che ritorna i primi n utenti in ordine descrescente per punteggio
     */
    public static void getRanking(Context context, final GetClassificaHandler handler, final ServerErrorHandler serverErrorHandler){
        String path = "/users/?order_by=punteggio&descending=true&n=5";
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
        });
        VolleyRequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
