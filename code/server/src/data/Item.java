package data;


import java.io.Serializable;

/**
 * Classe astratta che rappresenta un elemento (item) associato a un attributo e a un valore di tipo generico.
 *
 * <p>Un {@code Item} incapsula un {@link Attribute} e il valore corrispondente,
 * ed espone un metodo astratto per calcolare la distanza tra questo item e un altro valore.</p>
 *
 * <p>Le classi derivate (ad esempio {@code ContinuousItem} o {@code DiscreteItem})
 * implementano il metodo {@link #distance(Object)} in base al tipo di valore
 * gestito.</p>
 *
 * @param <T> tipo del valore associato all'item (ad esempio {@code Double}, {@code String}, ecc.)
 *
 * @see Attribute
 */
abstract public class Item<T> implements Serializable {
    private final Attribute attribute;
    private final T Value;

    /**
     * Costruisce un {@code Item} associandolo a un attributo e a un valore specifici.
     *
     * @param attribute l'attributo a cui l'item appartiene
     * @param value     il valore dell'item
     */
    public Item(Attribute attribute, T value) {
        this.attribute = attribute;
        this.Value = value;
        // System.out.println("Inizializzazione item");
    }

    /**
     * Restituisce l'attributo associato a questo item.
     * <p>Il metodo è {@code protected} per limitare l'accesso alle sole sottoclassi.</p>
     *
     * @return l'attributo dell'item
     */
    protected Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Restituisce il valore associato a questo item.
     *
     * @return il valore dell'item
     */
    public T getValue() {
        return this.Value;
    }

    /**
     * Restituisce la rappresentazione testuale del valore dell'item.
     * <p>La stringa risultante dipende dal metodo {@code toString()} della classe {@code T}.</p>
     *
     * @return stringa contenente il valore dell'item
     */
    public String toString() {
        return Value.toString();
    }

    /**
     * Metodo astratto che calcola la distanza tra il valore di questo item e un altro valore.
     *
     * <p>Il significato della distanza dipende dall'implementazione specifica
     * nelle sottoclassi:
     * <ul>
     *   <li>In {@code ContinuousItem}, la distanza può essere la differenza
     *       assoluta tra due valori numerici.</li>
     *   <li>In {@code DiscreteItem}, la distanza può essere definita come
     *       0 se i valori sono uguali, 1 altrimenti.</li>
     * </ul>
     * </p>
     *
     * @param a un valore (atteso dello stesso tipo di {@code T}) con cui calcolare la distanza
     * @return un {@code double} che rappresenta la distanza
     * @throws IllegalArgumentException se {@code a} non è compatibile
     * con il tipo gestito dall'item
     */
    abstract double distance(Object a);


}
