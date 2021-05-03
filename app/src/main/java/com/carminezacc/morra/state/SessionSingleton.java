package com.carminezacc.morra.state;

/**
 * Classe usata per manterenere la sessione in memoria.
 * Trattandosi di un Singleton, il costruttore è privato
 * e si accede all'unica istanza esistente attraverso il
 * metodo {@code getInstance()}.
 *
 * Ottenuta un'istanza del {@code SessionSingleton}, si può sapere se
 * la sessione è stata impostata usando {@code isLoggedIn()}.
 *
 * Dopo aver effettuato il login oppure recuperato da storage
 * e verificato il token, si imposta la sessione usando
 * {@code setSession()}, in modo tale che l'app possa accedere
 * in un secondo momento al token usando {@code getToken()}, e
 * all'user ID usando {@code getUserId()}.
 *
 * Esempio: login eseguito, token in variabile chiamata {@code jwt}, user ID
 * in variabile chiamata {@code userId}, impostiamo la sessione:
 * <pre>
 *     SessionSingleton session = SessionSingleton.getInstance();
 *
 *     session.setSession(userId, jwt);
 *
 * </pre>
 *
 * Esempio: ci serve l'userId e il token per una richiesta
 * <pre>
 *     SessionSingleton session = SessionSingleton.getInstance();
 *
 *     int userId = session.getUserId();
 *     String token = session.getToken();
 * </pre>
 */
public class SessionSingleton {

    static private SessionSingleton instance = null;

    int userId = 0;
    String token = null;

    private SessionSingleton() {}

    public int getUserId() {
        return userId;
    }


    public String getToken() {
        return token;
    }

    public void setSession(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public boolean isLoggedIn() {
        return this.userId != 0 && this.token != null;
    }


    static public SessionSingleton getInstance() {
        if(instance == null) {
            instance = new SessionSingleton();
        }

        return instance;
    }




}
