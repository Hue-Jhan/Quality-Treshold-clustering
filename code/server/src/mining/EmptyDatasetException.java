package mining;


/**
 * Eccezione lanciata quando si tenta di eseguire operazioni su un dataset vuoto.
 *
 * <p>Questa eccezione può essere utilizzata per segnalare che non ci sono dati
 * disponibili per procedere con il processo di clustering o con altre operazioni
 * che richiedono un dataset non vuoto.</p>
 *
 * <p>Estende {@link Exception} ed è una checked exception.</p>
 *
 * 
 */
public class EmptyDatasetException extends Exception {
	
	/**
     * Costruttore che accetta un messaggio descrittivo dell'errore.
     *
     * @param message messaggio che descrive il motivo dell'eccezione
     */
    public EmptyDatasetException(String message) {
        super(message);
    }
}
