package data;

import java.io.Serializable;

/**
 * Rappresenta un elemento (item) associato a un attributo continuo.
 * 
 * <p>Estende la classe {@link Item} e implementa la serializzazione.</p>
 * 
 * <p>Fornisce un metodo per calcolare la distanza tra il valore dell'item
 * e un altro valore numerico, normalizzando entrambi i valori rispetto
 * all'intervallo dell'attributo continuo di riferimento.</p>
 * 
 */
public class ContinuousItem extends Item implements Serializable {

	/**
     * Costruisce un elemento continuo associato a un attributo e a un valore.
     * 
     * @param attribute attributo continuo associato
     * @param value valore numerico dell'item
     */
    public ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza normalizzata tra il valore di questo item e un altro valore.
     * 
     * <p>Il valore di questo item e il valore passato vengono scalati nell'intervallo [0,1]
     * in base all'attributo continuo, quindi si restituisce la distanza assoluta tra
     * i valori scalati.</p>
     * 
     * @param a oggetto con cui calcolare la distanza, deve essere un {@link Number}
     * @return distanza normalizzata tra i due valori
     * @throws IllegalArgumentException se l'argomento non è un valore numerico
     */
    @Override
    double distance(Object a) {
        if (!(a instanceof Number)) {
            throw new IllegalArgumentException("Expected a numeric value");
        }
        double currentValue = ((Number) getValue()).doubleValue();
        double otherValue = ((Number) a).doubleValue();

        ContinuousAttribute attr = (ContinuousAttribute) getAttribute();
        double scaledCurrent = attr.getScaledValue(currentValue);
        double scaledOther = attr.getScaledValue(otherValue);

        return Math.abs(scaledCurrent - scaledOther);
    }

}