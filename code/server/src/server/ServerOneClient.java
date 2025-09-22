package server;

import data.Data;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.ClusteringRadiusException;
import mining.EmptyDatasetException;
import mining.QTMiner;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * La classe {@code ServerOneClient} rappresenta un thread dedicato alla gestione
 * della comunicazione con un singolo client connesso via socket.
 * <p>
 * Il server interpreta comandi inviati dal client e fornisce risposte coerenti
 * relative a:
 * <ul>
 *     <li>Caricamento dati da una tabella</li>
 *     <li>Esecuzione del clustering con QTMiner</li>
 *     <li>Salvataggio o caricamento dei cluster da/verso file</li>
 * </ul>
 * La comunicazione avviene tramite stream di oggetti (ObjectInputStream/ObjectOutputStream).
 * 
 */
public class ServerOneClient extends Thread {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private QTMiner kmeans;
    private String tableName;

    /**
     * Crea una nuova istanza del gestore client e avvia immediatamente il thread.
     *
     * @param s il socket connesso al client.
     * @throws IOException se si verifica un errore durante l'inizializzazione degli stream.
     */
    public ServerOneClient(Socket s) throws IOException {
        this.socket = s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
        this.start(); // avvia il thread
    }

    /**
     * Metodo eseguito nel thread che gestisce il ciclo di ascolto delle richieste del client.
     * Le richieste sono identificate da interi:
     * <ul>
     *     <li>0: ricezione nome della tabella</li>
     *     <li>1: avvio clustering su tabella</li>
     *     <li>2: salvataggio dei cluster su file</li>
     *     <li>3: caricamento dei cluster da file</li>
     * </ul>
     * In caso di disconnessione o errore, il socket viene chiuso.
     */
    @Override
    public void run() {
        System.out.println("Client Connesso");
        try {
            while (true) {
                Object request = in.readObject();

                if (request instanceof Integer) {
                    int command = (Integer) request;

                    switch (command) {
                        case 0:
                            handleStoreTableFromDb();
                            break;
                        case 1:
                            handleLearningFromDbTable();
                            break;
                        case 2:
                            handleStoreClusterInFile();
                            break;
                        case 3:
                            handleLearningFromFile();
                            break;
                        default:
                            out.writeObject("Comando non valido.");
                            out.flush();
                    }
                } else {
                    out.writeObject("Richiesta non valida.");
                    out.flush();
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnesso.");
        } catch (Exception e) {
            try {
                out.writeObject("Errore: " + e.getMessage());
                out.flush();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del socket.");
            }
        }
    }

    /**
     * Gestisce il comando per memorizzare il nome della tabella da cui leggere i dati.
     * <p>
     * Il client deve inviare una stringa contenente il nome della tabella; in caso contrario,
     * viene restituito un messaggio d'errore.
     * 
     * Se la tabella è valida, viene restituita anche la rappresentazione testuale
     * dei dati estratti.
     *
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se l'oggetto ricevuto non è riconoscibile.
     */
    private void handleStoreTableFromDb() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (obj instanceof String) {
            tableName = (String) obj; // salva il nome della tabella
            out.writeObject("OK");
            try {
                Data data = new Data<>(tableName);
                out.writeObject(data.toString());  // invia la stringa dei dati
            } catch (Exception e) {
                out.writeObject("Errore durante il caricamento dati: " + e.getMessage());
            }

        } else {
            out.writeObject("Errore: nome tabella non valido.");
        }
        out.flush();
    }

    /**
     * Gestisce l'avvio del clustering sulla tabella precedentemente indicata,
     * utilizzando il raggio specificato dal client.
     * <p>
     * Esegue il clustering con {@link QTMiner} e restituisce:
     * <ul>
     *     <li>Messaggio di conferma</li>
     *     <li>Numero di iterazioni</li>
     *     <li>Stringa dei cluster</li>
     * </ul>
     * In caso di errore vengono gestite sia le eccezioni di clustering che quelle di accesso al DB.
     *
     * @return Invia al client una conferma, il numero di cluster trovati e la descrizione dei cluster.
     * 
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se il tipo dell'oggetto ricevuto non è atteso.
     */
    private void handleLearningFromDbTable() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (!(obj instanceof Double)) {
            out.writeObject("Errore: raggio non valido.");
            out.flush();
            return;
        }
        double radius = (Double) obj;

        if (tableName == null) {
            out.writeObject("Errore: nessuna tabella specificata.");
            out.flush();
            return;
        }

        try {
            Data data = new Data<>(tableName);
            System.out.println("Dati caricati");
            // out.writeObject("DATI");
            // out.writeObject(data.toString());

            this.kmeans = new QTMiner(radius);
            int numIter = kmeans.compute(data);

            out.writeObject("OK");
            out.writeObject(numIter);
            out.writeObject(kmeans.getC().toString(data));

        } catch (EmptyDatasetException | ClusteringRadiusException | IOException e) {
            out.writeObject("Errore clustering: " + e.getMessage());
        } catch (DatabaseConnectionException | SQLException | EmptySetException | NoValueException e) {
            out.writeObject("Errore durante il caricamento dei dati: " + e.getMessage());
        }
        out.flush();
    }

    /**
     * Gestisce la richiesta di salvataggio dei cluster appresi su file.
     * <p>
     * Il client deve inviare un nome file valido. Se il clustering non è stato ancora eseguito,
     * viene restituito un messaggio d’errore.
     *
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se il tipo dell'oggetto ricevuto non è atteso.
     */
    private void handleStoreClusterInFile() throws IOException, ClassNotFoundException {
        Object obj = in.readObject(); // Leggi il nome file dal client
        System.out.println("Tabella ricevuta: '" + tableName + "'");

        if (!(obj instanceof String)) {
            out.writeObject("Errore: nome file non valido.");
            out.flush();
            return;
        }
        String filename = (String) obj;

        if (kmeans == null) {
            out.writeObject("Errore: nessun cluster da salvare.");
            out.flush();
            return;
        }

        try {
            kmeans.salva(filename);
            out.writeObject("OK");
        } catch (IOException e) {
            out.writeObject("Errore salvataggio cluster: " + e.getMessage());
        }
        out.flush();
    }

    /**
     * Gestisce la richiesta di caricamento di cluster da un file.
     * <p>
     * Il client invia il nome del file e, se il caricamento ha successo,
     * viene restituita la rappresentazione testuale dei cluster.
     *
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se il contenuto del file non è deserializzabile.
     */
    private void handleLearningFromFile() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        System.out.println("Avvio clustering con tabella: '" + tableName + "'");

        if (!(obj instanceof String)) {
            out.writeObject("Errore: nome file non valido.");
            out.flush();
            return;
        }
        String filename = (String) obj;

        try {
            kmeans = new QTMiner(filename);
            if (kmeans.getC() != null) {
                out.writeObject("OK");
                out.writeObject(kmeans.getC().toString());
            } else {
                out.writeObject("Attenzione: dati non caricati, impossibile mostrare i cluster.");
            }
        } catch (FileNotFoundException e) {
            out.writeObject("Errore: file cluster non trovato.");
        } catch (IOException | ClassNotFoundException e) {
            out.writeObject("Errore caricamento cluster: " + e.getMessage());
        }
        out.flush();
    }
}