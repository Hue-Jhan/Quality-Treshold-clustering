package database;

/**
  * Enumerazione che rappresenta i tipi di query supportati.
 * 
 * <p>Attualmente i tipi disponibili sono:</p>
 * <ul>
 *   <li>{@link #MIN} - per query che richiedono il valore minimo</li>
 *   <li>{@link #MAX} - per query che richiedono il valore massimo</li>
 * </ul>
 */
public enum QUERY_TYPE {
	MIN, MAX
}
