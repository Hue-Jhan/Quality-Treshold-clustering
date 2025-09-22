package database;

/**
 * Eccezione lanciata quando un'operazione su un result set SQL restituisce un set vuoto.
 * <p>
 * Questa eccezione viene tipicamente sollevata quando si tenta di accedere a dati
 * che non sono presenti nel database, ad esempio dopo una query che non ha restituito risultati.
 * </p>
 * 
 * <p>Messaggio predefinito: "Il result set è vuoto."</p>
 * 
 * 
 */
public class EmptySetException extends Exception {
	
	/**
     * Costruisce una nuova eccezione con il messaggio predefinito
     */
	public EmptySetException() {
		super("Il result set è vuoto.");
	}
}



