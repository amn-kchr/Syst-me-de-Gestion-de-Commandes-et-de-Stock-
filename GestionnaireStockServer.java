import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GestionnaireStockServer {
    // Le stock de produits, géré par une Map thread-safe pour éviter des problèmes de concurrence
    private static final Map<String, Produit> stock = new ConcurrentHashMap<>();
    // Les chariots des clients, également gérés par une Map thread-safe
    private static final Map<String, Chariot> chariots = new ConcurrentHashMap<>();
    // Les commandes des clients, une Map associant chaque client à sa liste de commandes
    private static final Map<String, List<Commande>> commandes = new ConcurrentHashMap<>();
    // ExecutorService pour gérer la livraison des commandes de manière asynchrone
    private static final ExecutorService deliveryExecutor = Executors.newCachedThreadPool();
    // Variable pour vérifier si un administrateur est connecté
    private static boolean isAdminConnected = false;

    // Méthode synchronisée pour vérifier si un administrateur est déjà connecté
    public static synchronized boolean isAdminConnected() {
        return isAdminConnected;
    }

    // Méthode synchronisée pour définir l'état de connexion de l'administrateur
    public static synchronized void setAdminConnected(boolean connected) {
        isAdminConnected = connected;
    }

    public static void main(String[] args) {
        // Initialisation du stock avec quelques produits par défaut
        stock.put("P001", new Produit("P001", "Ordinateur", 10, 700.0));
        stock.put("P002", new Produit("P002", "Souris", 50, 20.0));
        stock.put("P003", new Produit("P003", "Clavier", 30, 50.0));
        stock.put("P004", new Produit("P004", "Écran", 20, 150.0));

        // Démarrage du serveur sur le port 12345
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Serveur démarré sur le port 12345.");

            // ExecutorService pour gérer les connexions clients de manière concurrente
            ExecutorService executor = Executors.newCachedThreadPool();

            // Boucle infinie pour accepter les connexions des clients
            while (true) {
                // Attente de la connexion d'un client
                Socket clientSocket = serverSocket.accept();
                // Soumettre le traitement de chaque client à un thread dans le pool d'exécution
                executor.submit(new ClientHandler(clientSocket, stock, chariots, commandes, deliveryExecutor));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Si une exception survient, l'afficher
        }
    }
}
