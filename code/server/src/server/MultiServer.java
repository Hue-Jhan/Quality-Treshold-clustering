package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe {@code MultiServer} rappresenta un semplice server TCP multi-threaded
 * che rimane in ascolto su una porta specificata, accetta connessioni da client
 * e delega la gestione di ciascuna connessione a un nuovo thread tramite la classe
 * {@link ServerOneClient}.
 * <p>
 * Questo approccio consente la gestione simultanea di più client.
 *
 *  
 */
public class MultiServer {
    private final int PORT = 8080;

    /**
     * Metodo principale per avviare il server.
     * Avvia un'istanza di {@code MultiServer} sulla porta 8080.
     *
     * @param args non utilizzati.
     */
    public static void main(String[] args) {
        new MultiServer(8080);
    }

    /**
     * Costruttore del server che avvia l'esecuzione passando la porta desiderata.
     *
     * @param port la porta su cui mettere in ascolto il server.
     * 
     */
    public MultiServer(int port) {
        run(port);
    }

    /**
     * Avvia il server e rimane in ascolto sulla porta specificata.
     * <p>
     * Per ogni connessione entrante accettata, viene istanziato un oggetto
     * {@link ServerOneClient} che si occuperà della comunicazione con il client.
     * Ogni client viene gestito in modo indipendente in un thread separato.
     *
     * @param port la porta sulla quale il server rimane in ascolto.
     */
    public void run(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server in ascolto sulla porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // attende connessioni
                new ServerOneClient(clientSocket); // delega la gestione al thread
            }

        } catch (IOException e) {
            System.err.println("Errore nel server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}