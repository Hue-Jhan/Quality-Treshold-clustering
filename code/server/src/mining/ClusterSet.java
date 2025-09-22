package mining;

import data.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Rappresenta un insieme ordinato di cluster generati dal processo di clustering.
 *
 * <p>Ogni cluster è identificato da un centroide e contiene un insieme di tuple 
 * (rappresentate dai rispettivi ID). I cluster sono mantenuti all'interno di un 
 * {@link TreeSet}, che garantisce l'ordinamento naturale definito dalla classe {@link Cluster}.</p>
 *
 * <p>La classe implementa {@code Iterable<Cluster<Integer>>}, consentendo 
 * l'iterazione diretta sull'insieme dei cluster.</p>
 *
 * @see Cluster
 * @see Data
 */
public class ClusterSet implements Iterable<Cluster<Integer>>, Serializable {
	
	/**
     * Insieme ordinato di cluster.
     */
    private final Set<Cluster<Integer>> C = new TreeSet<>();

    /**
     * Costruttore di default. 
     * <p>Crea un insieme vuoto di cluster. La visibilità è package-private:
     * utilizzabile solo dalle classi del package {@code mining}.</p>
     */
    ClusterSet(){
    }

    /**
     * Aggiunge un nuovo cluster all'insieme.
     *
     * @param newCluster il cluster da aggiungere
     */
    void add(Cluster<Integer> newCluster) {
        C.add(newCluster);
    }

    /**
     * Restituisce un iteratore per i cluster presenti nell'insieme.
     *
     * @return un iteratore che scorre tutti i cluster nell'ordine stabilito
     *         dall'implementazione di {@link Cluster#compareTo(Object)}
     */
    @Override
    public Iterator<Cluster<Integer>> iterator() {
        return C.iterator();
    }

    /**
     * Restituisce una rappresentazione testuale dell'insieme dei cluster,
     * visualizzando solo i centroidi.
     *
     * <p>Ogni cluster è numerato progressivamente a partire da 0.</p>
     *
     * @return stringa descrittiva dei centroidi dei cluster
     */
    public String toString() {
        String str = "\n";
        Iterator<Cluster<Integer>> it = this.iterator();
        int i = 0;
        while (it.hasNext()) {
            Cluster<Integer> cluster = it.next();
            str += "Cluster " + i++ + ": " + cluster.toString() + "\n";
        }
        return str;
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata dell'insieme dei cluster.
     *
     * <p>Per ogni cluster vengono mostrati:
     * <ul>
     *   <li>il centroide,</li>
     *   <li>gli esempi (tuple) appartenenti al cluster,</li>
     *   <li>la distanza di ciascun esempio dal centroide,</li>
     *   <li>la distanza media complessiva dal centroide.</li>
     * </ul>
     * I cluster sono numerati progressivamente a partire da 1.</p>
     *
     * @param data l'oggetto {@link Data} contenente le tuple originali
     * @return una stringa multi-riga con la descrizione completa di tutti i cluster
     */
    public String toString(Data data) {
        String str = "\n";
        Iterator<Cluster<Integer>> it = this.iterator();
        int i = 1;
        while (it.hasNext()) {
            Cluster<Integer> cluster = it.next();
            str += i++ + ": " + cluster.toString(data) + "\n";
        }
        return str;
    }
}