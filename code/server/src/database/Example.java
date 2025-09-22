package database;

import java.util.ArrayList;
import java.util.List;


/**
 * Rappresenta un'istanza (tupla) di esempio composta da un insieme ordinato di valori.
 *
 * <p>Ogni oggetto contenuto nella lista rappresenta il valore di un attributo.
 * La classe fornisce metodi per aggiungere e accedere agli elementi,
 * nonché per confrontare istanze tra loro.</p>
 *
 * <p>Implementa {@link Comparable} per consentire l'ordinamento delle istanze,
 * utile in operazioni di gestione o confronto dei dati.</p>
 *
 * @see Comparable
 */
public class Example implements Comparable<Example>{
	private final List<Object> example = new ArrayList<Object>();

	/**
     * Aggiunge un valore all'esempio.
     *
     * @param o l'oggetto da aggiungere come attributo dell'esempio
     */
	public void add(Object o){
		example.add(o);
	}
	
	/**
     * Restituisce il valore dell'attributo alla posizione specificata.
     *
     * @param i indice dell'attributo da recuperare (0-based)
     * @return l'oggetto in posizione {@code i}
     * @throws IndexOutOfBoundsException se {@code i} è fuori dall'intervallo [0, size-1]
     */
	public Object get(int i){
		return example.get(i);
	}

	/**
     * Confronta questo esempio con un altro in base ai valori dei rispettivi attributi.
     *
     * <p>Il confronto avviene elemento per elemento fino al primo valore differente.
     * I valori devono implementare l'interfaccia {@link Comparable} per essere confrontabili.</p>
     *
     * @param ex l'altro esempio da confrontare
     * @return un valore negativo se questo esempio è minore, zero se uguale,
     *         o positivo se maggiore rispetto all'altro
     * @throws ClassCastException se un valore non implementa {@code Comparable}
     */
	public int compareTo(Example ex) {
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	
	/**
     * Restituisce una rappresentazione testuale dell'esempio.
     *
     * @return una stringa contenente i valori separati da spazi (con spazio finale)
     */
	public String toString(){
		String str="";
		for(Object o:example)
			str+=o.toString()+ " ";
		return str;
	}
	
}