package data;


import java.io.Serializable;

/**
 * Classe che rappresenta un elemento (item) con valore discreto associato ad un attributo discreto.
 * 
 * <p>Un {@code DiscreteItem} incapsula un valore discreto di tipo generico T, che deve essere comparabile,
 * e fornisce una funzione di distanza basata sull'uguaglianza del valore.</p>
 * 
 * @param <T> il tipo del valore discreto che deve implementare {@link Comparable}
 * 
 * 
 * 
 */
public class DiscreteItem<T extends Comparable<T>> extends Item<T> implements Serializable {

	/**
     * Costruisce un nuovo {@code DiscreteItem} associato all'attributo e al valore specificati.
     *
     * @param attribute attributo discreto di riferimento
     * @param value valore discreto associato all'item
     */
    public DiscreteItem(DiscreteAttribute<T> attribute, T value) {
        super(attribute, value);
    }

    /**
     * Costruisce un {@code DiscreteItem} con attributo e valore null.
     */
    public DiscreteItem() {
        super(null, null);
    }

    /**
     * Calcola la distanza tra questo item e un altro valore.
     *
     * <p>La distanza è definita come:</p>
     * <ul>
     *   <li>0 se i valori sono uguali</li>
     *   <li>1 altrimenti</li>
     * </ul>
     *
     * @param a valore da confrontare (dello stesso tipo {@code T})
     * @return 0 se i valori sono uguali, 1 altrimenti
     */
    public double distance(Object a) {
        if (getValue().equals(a)){
            return 0;
        } else {
            return 1;
        }
    }

}
