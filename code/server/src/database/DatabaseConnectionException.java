package database;

/**
 * Eccezione che rappresenta un errore durante la connessione al database.
 * <p>
 * Viene sollevata quando la connessione al database fallisce per cause specificate,
 * ad esempio problemi di rete, credenziali errate o configurazione errata del driver.
 * </p>
 * Estende {@link Exception} e consente di specificare sia il messaggio di errore sia
 * la causa sottostante tramite un'eccezione annidata ({@link Throwable}).
 * 
 * 
 */
public class DatabaseConnectionException extends Exception {
	
	/**
     * Costruisce una nuova eccezione con il messaggio specificato e la causa.
     *
     * @param message il messaggio di dettaglio relativo all'errore di connessione
     * @param cause   la causa che ha provocato l'eccezione (può essere {@code null})
     */
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

