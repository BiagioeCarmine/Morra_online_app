package com.carminezacc.morra.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
