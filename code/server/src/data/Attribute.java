package data;

/**
 * Classe astratta che rappresenta un attributo di un dataset,
 * come ad esempio "outlook", "temperature" o "wind".
 *
 * <p>Ogni attributo è caratterizzato da un nome e da un indice
 * che ne indica la posizione all’interno del dataset.</p>
 *
 * <p>Essendo astratta, non può essere istanziata direttamente.
 * Viene estesa da {@code ContinuousAttribute} e {@code DiscreteAttribute}.</p>
 */
abstract class Attribute {
    private String name;
    private int index;

    /**
     * Costruisce un attributo con nome e indice specificati.
     *
     * @param name  nome dell’attributo
     * @param index posizione (indice) dell’attributo nel dataset
     */
    protected Attribute(String name, int index) {
        // System.out.println("Attribute constructed");
        this.name = name;
        this.index = index;
    }

    /**
     * Costruisce un attributo di default senza nome e con indice -1.
     * Utile per inizializzazioni o come segnaposto.
     */
    public Attribute() {
        this.name = "";
        this.index = -1;
    }

    /**
     * Restituisce il nome dell’attributo.
     *
     * @return nome dell’attributo
     */
    String getName() {
        return this.name;
    }

    /**
     * Restituisce l’indice dell’attributo nel dataset.
     *
     * @return indice dell’attributo
     */
    int getIndex() {
        return this.index;
    }

    /**
     * Restituisce una rappresentazione testuale dell’attributo.
     * <p>Di default corrisponde al solo nome, senza l’indice.</p>
     *
     * @return stringa contenente il nome dell’attributo
     */
    public String toString() {
        return name;
    }

}