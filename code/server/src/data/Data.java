package data;

import java.io.*;
import java.sql.*;
import java.util.*;

import database.*;

import java.util.List;

/**
 * Classe che rappresenta un dataset estratto da una tabella di database.
 * 
 * <p>La classe costruisce uno schema di attributi (continui o discreti) e carica
 * i dati distinti dalla tabella indicata. Fornisce metodi per accedere agli esempi,
 * agli attributi e ai valori del dataset.</p>
 * 
 * @throws SQLException se si verifica un errore nella comunicazione con il database
 * @throws EmptySetException se la tabella non contiene dati
 * @throws DatabaseConnectionException se non è possibile connettersi al database
 * @throws NoValueException se i valori min/max per attributi numerici non sono validi
 * 
 */
public class Data<T extends Attribute> {
    private List<Example> data = new ArrayList<>();
    private final int numberOfExamples;
    private final List<Attribute> attributeSet;

    /**
     * Costruisce un dataset a partire dai dati contenuti nella tabella specificata.
     * 
     * <p>La connessione al database viene inizializzata, lo schema degli attributi viene
     * ricavato e per ogni attributo numerico vengono calcolati i valori min e max.
     * Successivamente vengono caricati i dati distinti dalla tabella e memorizzati 
     * internamente.</p>
     * 
     * @param tableName nome della tabella da cui estrarre i dati
     * @throws SQLException se si verifica un errore SQL
     * @throws EmptySetException se la tabella è vuota (nessun dato)
     * @throws DatabaseConnectionException se non è possibile aprire la connessione al DB
     * @throws NoValueException se non sono disponibili valori min/max validi per un attributo numerico
     */
    public Data(String tableName) throws SQLException, EmptySetException, DatabaseConnectionException, NoValueException {
        DbAccess db = new DbAccess();
        db.initConnection();

        attributeSet = new ArrayList<>();

        try {
            TableSchema tableSchema = new TableSchema(db, tableName);
            TableData tableData = new TableData(db);

            for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
                TableSchema.Column col = tableSchema.getColumn(i);

                if (col.isNumber()) {
                    Object minObj = tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MIN);
                    Object maxObj = tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MAX);

                    double min;
                    double max;

                    if (minObj instanceof Number && maxObj instanceof Number) {
                        min = ((Number) minObj).doubleValue();
                        max = ((Number) maxObj).doubleValue();
                    } else {
                        throw new NoValueException("Valori MIN/MAX non numerici per la colonna " + col.getColumnName());
                    }

                    attributeSet.add(new ContinuousAttribute(col.getColumnName(), i, min, max));
                } else {
                    Set<Object> distinctValues = tableData.getDistinctColumnValues(tableName, col);
                    String[] valuesArray = new String[distinctValues.size()];
                    int j = 0;
                    for (Object val : distinctValues) {
                        valuesArray[j++] = val.toString();
                    }

                    attributeSet.add(new DiscreteAttribute<String>(col.getColumnName(), i, valuesArray));
                }
            }

            data = tableData.getDistinctTransazioni(tableName);
            numberOfExamples = data.size();
        } catch (NoValueException e) {
            throw e;
        } finally {
            db.closeConnection();
        }
    }

    /**
     * Restituisce il numero di esempi presenti nel dataset.
     *
     * @return numero di esempi
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero di attributi nello schema del dataset.
     *
     * @return numero di attributi
     */
    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    /**
     * Restituisce lo schema degli attributi del dataset.
     *
     * @return lista di attributi che descrivono il dataset
     */
    public List<Attribute> getAttributeSchema() {
        return attributeSet;
    }

    /**
     * Restituisce l'attributo all'indice specificato.
     *
     * @param index posizione dell'attributo
     * @return attributo corrispondente
     * @throws IndexOutOfBoundsException se l'indice è fuori range
     */
    public Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    /**
     * Restituisce il valore di un attributo per uno specifico esempio.
     * 
     * @param exampleIndex indice dell'esempio
     * @param attributeIndex indice dell'attributo
     * @return valore dell'attributo per l'esempio indicato
     * @throws IndexOutOfBoundsException se gli indici sono fuori dal range
     */
    public Object getValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Costruisce un {@link Tuple} (insieme di item) per un esempio specificato dall'indice.
     * 
     * <p>Per ogni attributo, crea un {@link DiscreteItem} o {@link ContinuousItem} a seconda
     * del tipo di attributo, e lo aggiunge alla tupla.</p>
     * 
     * @param index indice dell'esempio
     * @return {@link Tuple} contenente gli item dell'esempio
     * @throws IllegalArgumentException se il tipo di attributo non è supportato
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());

        for (int i = 0; i < attributeSet.size(); i++) {
            Attribute attr = attributeSet.get(i);
            Object value = data.get(index).get(i);
            Item item;

            if (attr instanceof DiscreteAttribute) {
                item = new DiscreteItem((DiscreteAttribute) attr, (String) value);
            } else if (attr instanceof ContinuousAttribute) {
                item = new ContinuousItem((ContinuousAttribute) attr, ((Number) value).doubleValue());
            } else {
                throw new IllegalArgumentException("Tipo di attributo non supportato");
            }

            tuple.add(item, i);
        }
        return tuple;
    }

    /**
     * Restituisce una rappresentazione testuale del dataset, con l'elenco degli attributi
     * e i valori di ogni esempio separati da virgole.
     * 
     * @return rappresentazione a stringa del dataset
     */
    public String toString() {
        String s = "";
        Iterator<Attribute> it = attributeSet.iterator();
        while (it.hasNext()) {
            s += it.next().getName();
            if (it.hasNext()) s += ",";
        }
        s += "\n";

        for (int i = 0; i < getNumberOfExamples(); i++) {
            s += (i + 1) + ":";
            for (int j = 0; j < getNumberOfAttributes(); j++) {
                s += getValue(i, j);
                if (j < getNumberOfAttributes() - 1)
                    s += ", ";
            }
            s += "\n";
        }

        return s;
    }
}