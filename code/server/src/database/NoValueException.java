package database;

/**
 * Eccezione personalizzata che viene sollevata quando non è disponibile un valore 
 * atteso o richiesto.
 * 
 * <p>Questa eccezione estende la classe {@link Exception} e permette di specificare
 * un messaggio di errore descrittivo.</p>
 * 
 */
public class NoValueException extends Exception {
	
	/**
     * Costruisce una nuova eccezione NoValueException con un messaggio specificato.
     * 
     * @param message il messaggio descrittivo associato all'eccezione
     */
	public NoValueException(String message) {
		super(message);
	}
}
