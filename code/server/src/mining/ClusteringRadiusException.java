package mining;

/**
 * Eccezione personalizzata lanciata quando il raggio specificato per il clustering
 * non consente la creazione di alcun cluster valido.
 *
 * <p>Questa eccezione viene utilizzata per segnalare che il raggio scelto è troppo
 * piccolo o inadeguato rispetto ai dati da raggruppare.</p>
 *
 * 
 */
public class ClusteringRadiusException extends Exception {
	
	/**
     * Costruisce una nuova eccezione con il messaggio specificato.
     *
     * @param message il messaggio che descrive l'errore
     */
    public ClusteringRadiusException(String message) {
        super(message);
    }
}
