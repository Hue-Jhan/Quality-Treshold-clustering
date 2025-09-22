package data;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Classe che rappresenta un attributo discreto, con un insieme ordinato di valori distinti.
 * 
 * <p>Gli attributi discreti sono tipicamente utilizzati per variabili categoriali
 * come "outlook", "temperature" o "wind". Questa classe mantiene un insieme ordinato
 * (TreeSet) di valori distinti e permette l'iterazione su di essi.</p>
 * 
 * @param <T> tipo generico dei valori discreti che deve implementare {@link Comparable}
 * 
 * 
 * 
 */
public class DiscreteAttribute<T extends Comparable<T>> extends Attribute implements Iterable<T>, Serializable {
    private TreeSet<T> values;

    /**
     * Costruisce un attributo discreto con nome, indice e insieme di valori.
     * 
     * @param name nome dell'attributo
     * @param index indice dell'attributo nello schema
     * @param valuesArray array di valori discreti distinti
     */
    public DiscreteAttribute(String name, int index, T[] valuesArray) {
        super(name, index);
        this.values = new TreeSet<>();
        for (T value : valuesArray) {
            this.values.add(value);
        }
    }

    /**
     * Costruttore di default che crea un attributo discreto senza nome e con indice -1.
     */
    public DiscreteAttribute() {
        super(null, -1);
    }

    /**
     * Metodo che restituisce il numero dei valori presenti nell'attributo discreto
     */
    private int getNumberOfValues() {
        return values.size();
    }

    /**
     * Restituisce un iteratore per scorrere i valori distinti dell'attributo.
     * 
     * @return iteratore su valori distinti
     */
    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }
}