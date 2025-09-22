package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Rappresenta lo schema di una tabella di un database, ottenendo informazioni
 * sulle colonne e sui relativi tipi di dati.
 *
 * <p>Utilizza l'oggetto {@link DbAccess} per connettersi al database e
 * recuperare i metadati della tabella specificata tramite JDBC.</p>
 *
 * <p>Lo schema viene rappresentato come una lista di oggetti {@link Column},
 * ciascuno contenente nome e tipo della colonna, con tipi SQL semplificati
 * in {@code "string"} o {@code "number"}.</p>
 *
 * @see DbAccess
 */
public class TableSchema {
	DbAccess db;
	
	/**
     * Classe interna che rappresenta una colonna di una tabella.
     *
     * <p>Ogni colonna è caratterizzata da un nome e da un tipo
     * (generalizzato come {@code "string"} o {@code "number"}).</p>
     */
	public class Column{
		private final String name;
		private final String type;
		
		/**
         * Costruisce una colonna con nome e tipo specificati.
         *
         * @param name nome della colonna
         * @param type tipo della colonna (generalizzato in "string" o "number")
         */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		
		/**
         * Restituisce il nome della colonna.
         *
         * @return nome della colonna
         */
		public String getColumnName(){
			return name;
		}
		
		/**
         * Verifica se la colonna è di tipo numerico.
         *
         * @return {@code true} se la colonna è numerica, {@code false} altrimenti
         */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		/**
         * Restituisce una rappresentazione testuale della colonna.
         *
         * @return stringa contenente nome e tipo della colonna
         */
		public String toString(){
			return name+":"+type;
		}
	}
	List<Column> tableSchema=new ArrayList<Column>();
	
	/**
     * Costruisce lo schema della tabella interrogando i metadati del database.
     *
     * <p>Il costruttore si connette al database tramite {@link DbAccess},
     * ottiene le colonne della tabella specificata e le mappa in oggetti
     * {@link Column}. I tipi SQL vengono tradotti in una forma semplificata
     * ("string" o "number").</p>
     *
     * @param db        istanza di {@link DbAccess} per la connessione al database
     * @param tableName nome della tabella di cui recuperare lo schema
     * @throws SQLException se si verifica un errore durante l'accesso ai metadati
     */
	public TableSchema(DbAccess db, String tableName) throws SQLException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		Connection con=db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);
		   
		while (res.next()) {
			if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
				tableSchema.add(new Column(res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
			}
		}
		res.close();
	}

	/**
     * Restituisce il numero di attributi (colonne) della tabella.
     *
     * @return numero di colonne della tabella
     */
	public int getNumberOfAttributes(){
		return tableSchema.size();
	}
		
	/**
     * Restituisce la colonna alla posizione indicata.
     *
     * @param index posizione della colonna (0-based)
     * @return la colonna corrispondente
     * @throws IndexOutOfBoundsException se l'indice è fuori range
     */
	public Column getColumn(int index){
		return tableSchema.get(index);
	}
		
}

		     


