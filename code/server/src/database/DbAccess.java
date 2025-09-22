package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce l'accesso al database MySQL per l'applicazione.
 *
 * <p>Questa classe si occupa di:
 * <ul>
 *   <li>inizializzare la connessione al database tramite JDBC;</li>
 *   <li>fornire l'accesso alla connessione attiva tramite {@link #getConnection()};</li>
 *   <li>chiudere la connessione in modo sicuro tramite {@link #closeConnection()}.</li>
 * </ul>
 * </p>
 *
 * <p>I parametri di connessione (driver, host, porta, database, utente, password)
 * sono predefiniti all'interno della classe.</p>
 *
 * @see java.sql.Connection
 * @see DatabaseConnectionException
 */
public class DbAccess {
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; 
    private final String DBMS = "jdbc:mysql"; 
    private final String SERVER = "localhost";
    private final String DATABASE = "MapDB"; 
    private final String PORT = "3306"; 
    private final String USER_ID = "MapUser";
    private final String PASSWORD = "map"; 
    private Connection conn; 

    /**
     * Inizializza la connessione al database utilizzando i parametri predefiniti.
     *
     * <p>Il metodo carica il driver JDBC e crea una connessione
     * utilizzando {@link DriverManager}.</p>
     *
     * @throws DatabaseConnectionException se il driver JDBC non viene trovato
     *         o la connessione non può essere stabilita
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

            conn = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectionException("Errore di connessione al database", e);
        }
    }

    /**
     * Restituisce la connessione attiva al database.
     *
     * <p>Se la connessione non è stata inizializzata,
     * il metodo restituisce {@code null}.</p>
     *
     * @return oggetto {@link Connection} se attivo, altrimenti {@code null}
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database, se attiva.
     *
     * <p>Se la connessione è già chiusa o non è stata inizializzata,
     * il metodo non ha effetto.</p>
     *
     * @throws DatabaseConnectionException se si verifica un errore durante la chiusura
     */
    public void closeConnection() throws DatabaseConnectionException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Errore nella chiusura della connessione", e);
        }
    }
}
