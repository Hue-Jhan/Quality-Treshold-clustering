import java.io.Serializable;

/**
 * Eccezione personalizzata utilizzata per segnalare errori lato server.
 * Può essere serializzata per essere inviata attraverso la rete.
 *
 * <p>Viene tipicamente lanciata in contesti in cui il server rileva
 * una condizione di errore specifica che non è coperta da eccezioni standard.</p>
 *
 *
 */
public class ServerException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore dell'eccezione che accetta un messaggio descrittivo.
     *
     * @param message il messaggio dell'errore
     */
    public ServerException(String message) {
        super(message);
    }
}
