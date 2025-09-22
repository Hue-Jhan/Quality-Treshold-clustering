package mining;

import data.Data;
import data.Tuple;
import java.io.*;
import java.util.Iterator;

/**
 * Implementa l'algoritmo di clustering <b>QT (Quality Threshold)</b>.
 *
 * <p>
 * L'algoritmo suddivide un dataset in cluster, ciascuno caratterizzato da un centroide 
 * e da un insieme di tuple (identificate tramite ID) che si trovano entro una certa 
 * distanza (raggio) dal centroide.
 * </p>
 *
 * <p>
 * Per costruire un cluster, l'algoritmo seleziona la tupla che genera il sottogruppo 
 * più numeroso rispettando il vincolo di distanza, e ripete il processo fino a 
 * clusterizzare tutte le tuple.
 * </p>
 *
 * <p>
 * La classe consente inoltre di <b>salvare</b> e <b>ricaricare</b> un risultato di 
 * clusterizzazione da/verso file.
 * </p>
 *
 * @see Cluster
 * @see ClusterSet
 * @see Data
 */
public class QTMiner {
	/**
     * Insieme dei cluster trovati.
     */
    ClusterSet C;
    
    /**
     * Raggio massimo entro cui includere una tupla in un cluster.
     */
    double radius;

    /**
     * Costruttore che inizializza il miner con un valore di raggio specificato.
     *
     * @param radius il raggio massimo di inclusione nel cluster
     */
    public QTMiner(double radius) {
        this.radius = radius;
        C = new ClusterSet();
    }

    /**
     * Costruttore che carica un insieme di cluster precedentemente salvato da file.
     *
     * @param fileName nome del file da cui caricare i cluster
     * @throws FileNotFoundException se il file non esiste
     * @throws IOException se si verifica un errore I/O durante la lettura
     * @throws ClassNotFoundException se il contenuto del file non è compatibile
     */
    public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        ClusterSet temp = (ClusterSet) in.readObject();
        in.close();
        C = new ClusterSet();
        for (Cluster<Integer> c : temp) {
            C.add(c);
        }
    }

    /**
     * Salva l'insieme dei cluster trovati su file.
     *
     * @param fileName nome del file di destinazione
     * @throws FileNotFoundException se il file non può essere creato
     * @throws IOException se si verifica un errore durante la scrittura
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(C);
        out.close();
    }

    /**
     * Restituisce l'insieme dei cluster trovati.
     *
     * @return l'oggetto {@link ClusterSet} contenente i cluster
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo QT per costruire i cluster da un dataset.
     *
     * @param data il dataset da clusterizzare
     * @return il numero di cluster trovati
     * @throws ClusteringRadiusException se tutte le tuple finiscono in un unico cluster
     * @throws EmptyDatasetException se il dataset è vuoto
     */
    public int compute(Data data) throws ClusteringRadiusException, EmptyDatasetException {
        if (data.getNumberOfExamples() == 0) {
            throw new EmptyDatasetException("Dataset is empty!");
        }
        int numclusters = 0;
        boolean[] isClustered = new boolean[data.getNumberOfExamples()];
        for (int i = 0; i < isClustered.length; i++)
            isClustered[i] = false; 
        int countClustered = 0;
        while (countClustered != data.getNumberOfExamples()) {
            Cluster<Integer> c = buildCandidateCluster(data, isClustered);
            C.add(c); // cluster finali
            numclusters++;

            for (Integer id : c) {
                isClustered[id] = true;
            }
            countClustered += c.getSize();
        }

        if (numclusters == 1) {
            throw new ClusteringRadiusException(data.getNumberOfExamples() + " tuples in one cluster!");
        }

        return numclusters;
    }

    /**
     * Costruisce il cluster candidato migliore partendo da ogni tupla non ancora clusterizzata
     * e selezionando quella che genera il cluster più numeroso rispettando il raggio.
     *
     * @param data dataset su cui lavorare
     * @param isClustered array booleano che indica se un elemento è già stato clusterizzato
     * @return il cluster più grande tra i candidati
     */
    private Cluster<Integer> buildCandidateCluster(Data data, boolean[] isClustered) {
        Cluster<Integer> bestCluster = null;
        int maxSize = -1;

        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            if (!isClustered[i]) {
                Tuple centroid = data.getItemSet(i); // considera ogni tupla come centroide
                Cluster<Integer> candidateCluster = new Cluster<>(centroid);

                for (int j = 0; j < data.getNumberOfExamples(); j++) {
                    if (!isClustered[j]) {
                        Tuple current = data.getItemSet(j);
                        if (centroid.getDistance(current) <= radius) {
                            candidateCluster.addData(j); // se ogni tupla entra nel radius del
                        }                                // centroide viene agguinta nel cluster
                    }
                }
                if (candidateCluster.getSize() > maxSize) { // il cluster appena fatto sara il piu grande
                    bestCluster = candidateCluster;         // quindi itera tutte le tuple per cercare
                    maxSize = candidateCluster.getSize();   // il cluster piu grande in assoluto
                }
            }
        }
        return bestCluster;
    }
}
