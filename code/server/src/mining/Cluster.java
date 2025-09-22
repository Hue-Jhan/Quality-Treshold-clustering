package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Rappresenta un cluster all'interno dell'algoritmo di clustering QT.
 * <p>
 * Ogni cluster è caratterizzato da:
 * <ul>
 *     <li>un centroide, rappresentato da un oggetto {@link Tuple};</li>
 *     <li>un insieme di identificativi (interi) delle tuple appartenenti al cluster.</li>
 * </ul>
 * </p>
 *
 * <p>
 * La classe implementa:
 * <ul>
 *     <li>{@link Iterable} per consentire l'iterazione sugli ID delle tuple;</li>
 *     <li>{@link Comparable} per confrontare cluster in base alla dimensione e al centroide;</li>
 *     <li>{@link Serializable} per permetterne la serializzazione.</li>
 * </ul>
 * </p>
 *
 * @param <T> tipo degli identificativi delle tuple (estende {@link Integer})
 */
class Cluster<T extends Integer> implements Iterable<Integer>, Comparable<Cluster<T>>, Serializable {
    private final Tuple centroid;
    private final HashSet<Integer> clusteredData;

    /**
     * Crea un nuovo cluster con il centroide specificato.
     *
     * @param centroid il centroide iniziale del cluster
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<>();
    }

    /**
     * Restituisce il centroide del cluster.
     *
     * @return il centroide del cluster
     */
    Tuple getCentroid() {
        return centroid;
    }

    /**
     * Aggiunge un identificativo di tupla al cluster.
     *
     * @param id l'identificativo della tupla
     * @return {@code true} se l'id è stato aggiunto, {@code false} se era già presente
     */
    boolean addData(T id) {
        return clusteredData.add(id);
    }

    /**
     * Verifica se una tupla appartiene al cluster.
     *
     * @param id identificativo della tupla
     * @return true se la tupla è presente, false altrimenti
     */
    private boolean contain(T id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove una tupla dal cluster.
     *
     * @param id identificativo della tupla da rimuovere
     */
    private void removeTuple(T id) {
        clusteredData.remove(id);
    }

    /**
     * Restituisce un iteratore per scorrere gli ID delle tuple nel cluster.
     *
     * @return iteratore sugli ID delle tuple
     */
    int getSize() {
        return clusteredData.size();
    }


    /**
     * Restituisce un iteratore per scorrere gli ID delle tuple nel cluster.
     *
     * @return iteratore sugli ID delle tuple
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Confronta due cluster in base al numero di tuple contenute.
     *
     * <p>Se le dimensioni coincidono, il confronto viene effettuato sui valori
     * del centroide, confrontando attributo per attributo le rispettive
     * rappresentazioni testuali.</p>
     *
     * <p>L'ordinamento definito da questo metodo è utilizzato ad esempio
     * nei {@link java.util.TreeSet} che memorizzano cluster.</p>
     *
     * @param other il cluster da confrontare
     * @return un valore negativo se questo cluster è "minore", zero se uguale,
     *         un valore positivo se "maggiore"
     */
    @Override
    public int compareTo(Cluster other) {
        int sizeComparison = Integer.compare(this.getSize(), other.getSize());
        if (sizeComparison != 0)
            return sizeComparison;

        for (int i = 0; i < this.centroid.getLength(); i++) {
            Object val1 = this.centroid.get(i);
            Object val2 = other.centroid.get(i);

            int cmp = val1.toString().compareTo(val2.toString());
            if (cmp != 0)
                return cmp;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale del centroide del cluster.
     *
     * @return stringa con i valori del centroide tra parentesi
     */
    public String toString() {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i);
        str += ")";
        return str;
    }

    /**
     * Restituisce una rappresentazione dettagliata del cluster.
     *
     * <p>Per ciascun cluster vengono mostrati:
     * <ul>
     *   <li>il centroide,</li>
     *   <li>gli esempi (tuple) appartenenti al cluster, con i loro valori e
     *       la distanza dal centroide,</li>
     *   <li>la distanza media complessiva dal centroide.</li>
     * </ul>
     * Gli esempi sono elencati riga per riga e preceduti dall'indice della tupla.</p>
     *
     * @param data l'oggetto {@link Data} che contiene le tuple originali
     * @return stringa multi-riga con centroide, esempi e distanza media
     */
    String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")\nExamples:\n";

        Iterator<Integer> it = clusteredData.iterator();
        try {
        	while (it.hasNext()) {
        		int id = it.next();
        		str += "[";
        		for (int j = 0; j < data.getNumberOfAttributes(); j++)
        			str += data.getValue(id, j) + " ";
        		str += "] dist=" + getCentroid().getDistance(data.getItemSet(id)) + "\n";
        	}
        	str += "AvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        } catch (Exception e) {
            System.out.println(e);
        }
        str += " \n";
        return str;
    }

}
