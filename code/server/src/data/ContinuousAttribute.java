package data;


import java.io.Serializable;

/**
 * Rappresenta un attributo continuo, estendendo la classe {@link Attribute}.
 * 
 * <p>Un attributo continuo ha un valore minimo e massimo che definiscono il suo intervallo
 * di valori possibili.</p>
 * 
 * <p>Fornisce inoltre un metodo per scalare un valore all'interno di questo intervallo
 * in un valore normalizzato compreso tra 0 e 1.</p>
 * 
 */
public class ContinuousAttribute extends Attribute implements Serializable {
    private final double min;
    private final double max;

    /**
     * Costruisce un attributo continuo con nome, indice, valore minimo e massimo.
     * 
     * @param name nome dell'attributo
     * @param index indice dell'attributo nel dataset
     * @param min valore minimo dell'attributo
     * @param max valore massimo dell'attributo
     */
    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Restituisce il valore scalato di un dato valore `v`, normalizzato nell'intervallo [0, 1].
     * 
     * @param v valore da scalare
     * @return valore normalizzato compreso tra 0 e 1
     */
    protected double getScaledValue(double v) {
        double v1 = ((v-min)/(max-min));
        return v1;
    }

}
