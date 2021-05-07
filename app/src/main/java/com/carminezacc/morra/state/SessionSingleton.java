package com.carminezacc.morra.state;

/**
 * Classe usata per manterenere la sessione in memoria.
 * Trattandosi di un Singleton, il costruttore è privato
 * e si accede all'unica istanza esistente attraverso il
 * metodo {@link #getInstance()}.
 *
 * Ottenuta un'istanza del {@link #SessionSingleton}, si può sapere se
 * la sessione è stata impostata usando {@link #isLoggedIn()}.
 *
 * Dopo aver effettuato il login oppure recuperato da storage
 * e verificato il token, si imposta la sessione usando
 * {@link #setSession}, in modo tale che l'app possa accedere
 * in un secondo momento al token usando {@link #getToken()}, e
 * all'user ID usando {@link #getUserId()}.
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

    /**
     * Istanza unica del singleton
     */
    static private SessionSingleton instance = null;

    /**
     * ID dell'utente, 0 se non
     */
    int userId = 0;
    String token = null;

    /**
     * Costruttore privato per assicurarsi che la classe non venga istanziata più di una volta.
     */
    private SessionSingleton() {}

    /**
     * Metodo utilizzato per ottenere l'user ID salvato nella sessione.
     * @return l'user ID salvato in precedenza con {@link #setSession(int, String)}, {@code 0} se non
     * è mai stato chiamato tale metodo.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Metodo utilizzato per ottenere il token salvato nella sessione.
     * @return il token salvato in precedenza con {@link #setSession(int, String)}, {@code null} se
     * non è mai stato chiamato tale metodo.
     */
    public String getToken() {
        return token;
    }

    /**
     * Imposta {@link #userId} e {@link #token} per l'utente corrente. Da eseguire
     * dopo aver eseguito il login o dopo aver recuperato e verificato il token.
     * @param userId: ID utente dell'utente corrente.
     * @param token: token dell'utente corrente.
     */
    public void setSession(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    /**
     * Metodo che permette di sapere se è stata impostata una sessione per l'utente
     * corrente.
     * @return {@code true} se è stata impostata (l'utente ha eseguito l'accesso),
     * {@code false} se non è stata impostata (l'utente non ha ancora eseguito l'accesso)
     */
    public boolean isLoggedIn() {
        return this.userId != 0 && this.token != null;
    }


    /**
     * Metodo usato per ottenere l'istanza del Singleton, che è unica per l'intera app in
     * esecuzione (trattandosi di un singleton sarebbe scorretto il contrario, ovviamente),
     * questo metodo la crea se non esiste ancora.
     * @return istanza del Singleton
     */
    static public SessionSingleton getInstance() {
        if(instance == null) {
            instance = new SessionSingleton();
        }

        return instance;
    }




}
