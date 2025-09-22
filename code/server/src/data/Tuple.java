package data;


import java.io.Serializable;
import java.util.Set;

/**
 * Rappresenta una tupla, ossia un insieme ordinato di oggetti {@link Item}.
 *
 * <p>Una tupla è composta da più {@link Item}, ciascuno dei quali associa
 * un attributo a un valore. La classe fornisce metodi per accedere agli
 * elementi della tupla, modificarli e calcolare distanze tra tuple.</p>
 *
 * <p>Le distanze tra tuple vengono calcolate come somma delle distanze tra
 * i singoli item corrispondenti.</p>
 *
 * @see Item
 * @see Data
 */
public class Tuple implements Serializable {
    private final Item [] tuple;

    /**
     * Costruisce una tupla di dimensione specificata.
     *
     * @param size numero di item che compongono la tupla
     */
    public Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * Restituisce il numero di item che compongono la tupla.
     *
     * @return lunghezza della tupla
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'item in posizione {@code i} della tupla.
     *
     * @param i indice dell'item da ottenere (0-based)
     * @return item alla posizione {@code i}
     * @throws ArrayIndexOutOfBoundsException se {@code i} è fuori dall'intervallo [0, length-1]
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * Inserisce un item in posizione {@code i} della tupla.
     *
     * @param c item da inserire
     * @param i indice in cui inserire l'item
     * @throws ArrayIndexOutOfBoundsException se {@code i} è fuori dall'intervallo [0, length-1]
     */
    public void add(Item c, int i) {
        tuple[i] = c;
    }

    /**
     * Calcola la distanza tra questa tupla e un'altra tupla specificata.
     *
     * <p>La distanza viene calcolata come somma delle distanze tra gli
     * item corrispondenti delle due tuple.</p>
     *
     * @param obj tupla con cui calcolare la distanza
     * @return distanza totale come somma delle distanze tra item corrispondenti
     * @throws IllegalArgumentException se le tuple hanno dimensioni diverse
     */
    public double getDistance(Tuple obj) {
        double dis = 0.0;
        if (obj.getLength() != this.getLength()) {
            throw new IllegalArgumentException("Le tuple hanno dimensioni diverse.");
        }
        for (int i = 0; i < tuple.length; i++) {
            dis += tuple[i].distance(obj.get(i).getValue());
        }
        return dis;
    }

    /**
     * Calcola la distanza media tra questa tupla e un insieme di tuple,
     * identificate dagli indici forniti.
     *
     * @param data          oggetto {@link Data} contenente le tuple
     * @param clusteredData insieme di indici delle tuple da considerare
     * @return distanza media tra questa tupla e le tuple in {@code clusteredData}
     * @throws IllegalArgumentException se {@code clusteredData} è vuoto
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
    	if (clusteredData.isEmpty()) {
            throw new IllegalArgumentException("Il set di tuple è vuoto.");
        }
        double sumD = 0.0;
        for (int id : clusteredData) {
            double d = getDistance(data.getItemSet(id));
            sumD += d;
        }
        return sumD / clusteredData.size();
    }

}
