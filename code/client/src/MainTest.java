import java.io.*;
import java.net.*;

import keyboardinput.Keyboard;

/**
 * Client Java che comunica con un server per eseguire operazioni di clustering.
 * <p>
 * Offre due modalità operative:
 * <ul>
 *   <li>Lettura dei cluster da un file serializzato</li>
 *   <li>Clustering da una tabella del database remoto</li>
 * </ul>
 * La comunicazione avviene tramite socket TCP e scambio di oggetti serializzati.
 * Richiede la libreria {@code keyboardinput.Keyboard} per l'interazione da console.
 */
public class MainTest {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Costruttore del client. Stabilisce una connessione TCP con il server remoto.
     *
     * @param ip   Indirizzo IP del server
     * @param port Porta del server
     * @throws IOException in caso di errori di rete o creazione socket
     */
    public MainTest(String ip, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(ip);
        System.out.println("Connessione a: " + addr);
        Socket socket = new Socket(addr, port);
        System.out.println("Socket creata: " + socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Mostra un semplice menu testuale all'utente per scegliere tra due modalità:
     * <ul>
     *     <li>(1) Caricamento dei cluster da file</li>
     *     <li>(2) Scoperta dei cluster da una tabella del database</li>
     * </ul>
     *
     * @return la scelta dell’utente (1 o 2)
     */
    private int menu() {
        int scelta;
        do {
            System.out.println("\nScegli una opzione:");
            System.out.println("(1) Carica Cluster da File");
            System.out.println("(2) Scopri Cluster da DB");
            System.out.print("Risposta (1/2): ");
            scelta = Keyboard.readInt();
        } while (scelta < 1 || scelta > 2);
        return scelta;
    }

    /**
     * Invia al server il comando per caricare i cluster da un file.
     * <p>Chiede all’utente il nome del file, lo invia al server e attende la risposta.</p>
     *
     * @return Descrizione dei cluster caricati
     * @throws IOException se avvengono errori di comunicazione
     * @throws ClassNotFoundException se il tipo ricevuto non è atteso
     * @throws ServerException se il server segnala un errore
     */
    private String learningFromFile() throws IOException, ClassNotFoundException, ServerException {
        out.writeObject(3);
        System.out.print("Nome file cluster: ");
        String filename = Keyboard.readString();
        out.writeObject(filename);

        String result = (String) in.readObject();
        if ("OK".equals(result)) {
            return (String) in.readObject();
        } else {
            throw new ServerException(result);
        }
    }

    /**
     * Invia al server il nome della tabella da cui ottenere i dati per il clustering.
     *
     * @return il nome della tabella inserito, oppure {@code null} se la tabella non è valida
     * @throws IOException se avvengono errori di comunicazione
     * @throws ClassNotFoundException se il tipo ricevuto non è atteso
     * @throws ServerException se il server segnala un errore (ad esempio tabella non trovata)
     */
    private String sendTableName() throws IOException, ClassNotFoundException, ServerException {
        out.writeObject(0);
        System.out.print("Nome tabella: ");
        String tableName = Keyboard.readString();
        out.writeObject(tableName);
        String result = (String) in.readObject();
        if (!"OK".equals(result)) {
            System.out.println("fail");
            throw new ServerException(result);
        } else {
            String dati = (String) in.readObject();
            if (dati == null || dati.startsWith("Errore")) {
                // System.out.println(dati);
                return null;
            } else {
                System.out.println("\nDati tabella:\n" + dati);
                return tableName;
            }
        }
    }

    /**
     * Richiede all’utente un raggio valido per l’algoritmo di clustering e lo invia al server.     * <p>Prima invia al server un comando di richiesta, poi trasmette il raggio. 
     * Il valore deve essere numerico e maggiore di zero.</p>
     *
     * @return il valore del raggio inserito
     * @throws IOException se avvengono errori di comunicazione
     * @throws ServerException se il server segnala un errore (non previsto qui ma mantenuto per coerenza)
     */
    private double sendRadius() throws IOException, ServerException {
        out.writeObject(1);
        double radius;
        do {
            System.out.print("Inserisci raggio (>0):");
            radius = Keyboard.readDouble();
            if (Double.isNaN(radius) || radius <= 0) {
                System.out.println("Input non valido. Inserisci un numero maggiore di 0.");
            }
        } while (Double.isNaN(radius) || radius <= 0);
        return radius;
    }

    /**
     * Invia il raggio ricevuto al server e avvia il clustering sulla tabella.
     *
     * @param radius il raggio da utilizzare per il clustering
     * @return la descrizione testuale dei cluster ottenuti (il numero di cluster viene stampato a console)
     * @throws IOException se avvengono errori di comunicazione
     * @throws ClassNotFoundException se il tipo ricevuto non è atteso
     * @throws ServerException se il server segnala un errore durante il clustering
     */
    private String learningFromDbTable(double radius) throws IOException, ClassNotFoundException, ServerException {

        out.writeObject(radius);

        String result = (String) in.readObject();
        if (result.equals("OK")) {
            System.out.println("\nNumero cluster: " + in.readObject());
            return (String) in.readObject();
        } else {
            throw new ServerException(result);
        }
    }

    /**
     * Salva i cluster ottenuti su un file.
     * <p>Il nome del file può essere personalizzato oppure si può usare quello di default generato dal sistema.</p>
     *
     * @param tablename Nome della tabella di origine dei dati
     * @param radius    Raggio utilizzato nel clustering (viene incluso nel nome file di default)
     * @throws IOException se avvengono errori di scrittura/lettura
     * @throws ClassNotFoundException se il tipo ricevuto non è atteso
     * @throws ServerException se il server segnala un errore
     */
    private void saveClustersToFile(String tablename, double radius) throws IOException, ClassNotFoundException, ServerException {
        out.writeObject(2);
        String filename = tablename + radius + ".dmp";
        System.out.println("Premi spazio e invio per usare il Nome di Default = " + filename);
        System.out.print("Nome file salvataggio: ");
        String filename2 = Keyboard.readString();
        if (filename2.trim().isEmpty()) {
            out.writeObject(filename);
        } else out.writeObject(filename2);

        String result = (String) in.readObject();
        if (!"OK".equals(result)) {
            throw new ServerException(result);
        } else {
            System.out.println("Salvataggio completato.");
        }
    }

    /**
     * Metodo principale per avviare il client.
     * <p>
     * Stabilisce una connessione con il server all'indirizzo IP e porta specificati da riga di comando.
     * Permette all'utente di:
     * <ul>
     *   <li>Caricare cluster da un file</li>
     *   <li>Scoprire cluster da una tabella del database</li>
     * </ul>
     * In modalità "DB", consente all'utente di:
     * <ul>
     *   <li>Inserire il nome della tabella da analizzare</li>
     *   <li>Specificare un raggio per effettuare il clustering</li>
     *   <li>Salvare i cluster trovati su un file</li>
     * </ul>
     * <p>
     * Il programma gestisce gli errori di comunicazione con il server e consente all'utente di ripetere le operazioni.
     *
     * @param args argomenti della riga di comando: indirizzo IP del server (args[0]) e numero di porta (args[1])
     *             Esempio: {@code java MainTest2 127.0.0.1 12345}
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java MainTest <IP> <PORT>");
            return; }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        MainTest client = null;

        try {
            client = new MainTest(ip, port);

            boolean continuaProgramma = true;
            while (continuaProgramma) {
                int scelta = client.menu();

                switch (scelta) {
                    case 1:
                        try {
                            String clusters = client.learningFromFile();
                            System.out.println(clusters);
                        } catch (Exception e) {
                            System.out.println("Errore: " + e.getMessage());
                        }
                        break;

                    case 2:
                        boolean esci = false;
                        double radius = 0;
                        String tablename = "";
                        while (true) {
                            try {
                                tablename = client.sendTableName();
                                if (tablename == null || tablename.equals("fail")) {
                                    System.out.println("Tabella non valida, ritorno al menu.");
                                    esci = true;
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println(esci);
                                System.out.println("Errore: " + e.getMessage());
                            }
                        }

                        if (!esci) {
                            char repeat;
                            do {
                                try {
                                    radius = client.sendRadius();
                                    String clusters = client.learningFromDbTable(radius);
                                    System.out.println(clusters);
                                    client.saveClustersToFile(tablename, radius);

                                } catch (Exception e) {
                                    System.out.println("Errore: " + e.getMessage());
                                }

                                System.out.print("Nuova esecuzione con altro raggio? (y/n): ");
                                repeat = Keyboard.readChar();
                            } while (Character.toLowerCase(repeat) == 'y');
                            break;
                        }
                }

                char continua;
                do {
                    System.out.print("Vuoi eseguire un'altra operazione? (y/n): ");
                    continua = Keyboard.readChar();
                } while (continua != 'y' && continua != 'Y' && continua != 'n' && continua != 'N');

                continuaProgramma = (continua == 'y' || continua == 'Y');
            }

        } catch (EOFException | SocketException e) {
            System.out.println("Connessione chiusa dal server. Uscita...");
        } catch (IOException e) {
            System.out.println("Errore di comunicazione: " + e.getMessage());
        } finally {
            try {
                if (client != null) {
                    if (client.in != null) client.in.close();
                    if (client.out != null) client.out.close();
                }
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }

        System.out.println("Fine programma.");
    }
}