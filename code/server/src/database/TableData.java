package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import database.TableSchema.Column;

/**
 * Classe che gestisce l'accesso e l'estrazione di dati da una tabella di un database
 * tramite un oggetto {@link DbAccess}.
 * 
 * <p>Fornisce metodi per ottenere insiemi di tuple distinte, valori distinti di una colonna,
 * e valori aggregati (minimo o massimo) di una colonna specifica.</p>
 * 
 * <p>I dati estratti sono rappresentati come oggetti {@link Example} o come collezioni
 * di oggetti generici.</p>
 *
 */
public class TableData {

	DbAccess db;

	/**
     * Costruisce un oggetto TableData associato a un'istanza di {@link DbAccess}.
     * 
     * @param db istanza di DbAccess che gestisce la connessione al database
     */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
     * Restituisce una lista di tuple distinte (transazioni) presenti nella tabella specificata.
     * Ogni tupla è rappresentata come un oggetto {@link Example}.
     * 
     * @param table nome della tabella da cui estrarre le tuple distinte
     * @return lista di tuple distinte presenti nella tabella
     * @throws SQLException se si verifica un errore nell'esecuzione della query SQL
     * @throws EmptySetException se la tabella è vuota (nessuna tupla restituita)
     */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		String query="select distinct ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;
		while (rs.next()) {
			empty=false;
			Example currentTuple=new Example();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i+1));
				else
					currentTuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if(empty) throw new EmptySetException();
		
		
		return transSet;

	}
	
	/**
     * Restituisce un insieme ordinato di valori distinti contenuti in una colonna specifica
     * di una tabella.
     * 
     * @param table nome della tabella da cui estrarre i valori
     * @param column colonna di cui estrarre i valori distinti
     * @return insieme ordinato (TreeSet) di valori distinti presenti nella colonna
     * @throws SQLException se si verifica un errore nell'esecuzione della query SQL
     */
	public Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException{
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		String query="select distinct ";
		query+= column.getColumnName();
		query += (" FROM "+table);
		query += (" ORDER BY " +column.getColumnName());
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
				if(column.isNumber()) {
					valueSet.add(rs.getDouble(1));
				} else {
					valueSet.add(rs.getString(1));
				}
		}
		rs.close();
		statement.close();
		return valueSet;
	}

	/**
     * Restituisce il valore aggregato (minimo o massimo) di una colonna in una tabella.
     * 
     * @param table nome della tabella da cui estrarre il valore aggregato
     * @param column colonna di cui calcolare il valore aggregato
     * @param aggregate tipo di aggregazione da effettuare ({@link QUERY_TYPE#MIN} o {@link QUERY_TYPE#MAX})
     * @return valore aggregato (minimo o massimo) della colonna
     * @throws SQLException se si verifica un errore nell'esecuzione della query SQL
     * @throws NoValueException se non è presente alcun valore nella colonna per l'aggregazione richiesta
     */
	public Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) {
				if(column.isNumber()) {
					value = rs.getFloat(1);
				} else {
					value = rs.getString(1);
				}
		}
		rs.close();
		statement.close();
		if(value==null)
			throw new NoValueException("No " + aggregateOp+ " on "+ column.getColumnName());

		return value;
	}
}
