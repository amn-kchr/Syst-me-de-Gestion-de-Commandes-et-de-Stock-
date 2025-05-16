import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private final Socket socket; // Le socket de communication avec le client
    private final Map<String, Produit> stock; // Le stock de produits disponible
    private final Map<String, Chariot> chariots; // Les chariots des clients (chaque client est identifié par un sessionId)
    private final Map<String, List<Commande>> commandes; // Les commandes des clients, associées par sessionId
    private final ExecutorService deliveryExecutor; // Un pool d'exécution pour gérer les livraisons de commandes de manière asynchrone
    private String sessionId; // L'identifiant de session du client (utilisé pour suivre les actions du client)
    private boolean isAdmin; // Indique si l'utilisateur est un administrateur ou un client

    // Constructeur pour initialiser le gestionnaire de client
    public ClientHandler(Socket socket, Map<String, Produit> stock, Map<String, Chariot> chariots, Map<String, List<Commande>> commandes, ExecutorService deliveryExecutor) {
        this.socket = socket;
        this.stock = stock;
        this.chariots = chariots;
        this.commandes = commandes;
        this.deliveryExecutor = deliveryExecutor;
    }

    // Méthode exécutée lors de l'exécution du thread
    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Lecture des entrées du client
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true) // Envoi des réponses au client
        ) {
            // Attribuer un sessionId unique au client et déterminer s'il est administrateur ou client
            sessionId = UUID.randomUUID().toString();
            isAdmin = assignRole(writer);

            // Si c'est un client, initialiser son chariot et ses commandes
            if (!isAdmin) {
                chariots.putIfAbsent(sessionId, new Chariot());
                commandes.putIfAbsent(sessionId, new ArrayList<>());
            }

            String ligne;
            // Lire les commandes envoyées par le client
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(" "); // Diviser la commande en parties
                String commande = parts[0]; // Le premier mot de la commande indique l'action à effectuer

                try {
                    // Traitement des commandes en fonction du rôle de l'utilisateur (administrateur ou client)
                    if (isAdmin) {
                        handleAdminCommand(parts, writer);
                    } else {
                        handleClientCommand(parts, writer);
                    }
                } catch (Exception e) {
                    writer.println(e.getMessage()); // Si une exception est levée, l'envoyer au client
                }
                writer.println("FIN"); // Marquer la fin de la réponse
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client : " + e.getMessage());
        }
    }

    // Méthode pour attribuer un rôle à l'utilisateur (administrateur ou client)
    private boolean assignRole(PrintWriter writer) {
        synchronized (ClientHandler.class) {
            if (!GestionnaireStockServer.isAdminConnected()) {
                GestionnaireStockServer.setAdminConnected(true); // Si l'administrateur n'est pas connecté, le connecter
                writer.println("Bienvenue, Administrateur !");
                return true;
            } else {
                writer.println("Bienvenue, Client !");
                return false; // Sinon, l'utilisateur est un client
            }
        }
    }

    // Méthode pour traiter les commandes de l'administrateur
    private void handleAdminCommand(String[] parts, PrintWriter writer) throws Exception {
        String commande = parts[0];
        switch (commande) {
            case "ajouter_produit":
                if (parts.length == 5) {
                    String id = parts[1];
                    String nom = parts[2];
                    int quantite = Integer.parseInt(parts[3]);
                    double prix = Double.parseDouble(parts[4]);
                    stock.put(id, new Produit(id, nom, quantite, prix));
                    writer.println("Produit ajouté : " + id);
                } else {
                    throw new InvalidOrderException("Erreur : Format invalide pour ajouter_produit.");
                }
                break;

            case "modifier_produit":
                if (parts.length == 4) {
                    String id = parts[1];
                    int quantite = Integer.parseInt(parts[2]);
                    double prix = Double.parseDouble(parts[3]);
                    Produit produit = stock.get(id);
                    if (produit == null) {
                        throw new StockUnavailableException("Erreur : Produit non trouvé.");
                    }
                    produit.updateProduit(quantite, prix);
                    writer.println("Produit modifié : " + id);
                } else {
                    throw new InvalidOrderException("Erreur : Format invalide pour modifier_produit.");
                }
                break;

            case "supprimer_produit":
                if (parts.length == 2) {
                    String id = parts[1];
                    if (stock.remove(id) != null) {
                        writer.println("Produit supprimé : " + id);
                    } else {
                        throw new StockUnavailableException("Erreur : Produit non trouvé.");
                    }
                } else {
                    throw new InvalidOrderException("Erreur : Format invalide pour supprimer_produit.");
                }
                break;

            default:
                writer.println("Commande inconnue pour administrateur.");
                break;
        }
    }

    // Méthode pour traiter les commandes du client
    private void handleClientCommand(String[] parts, PrintWriter writer) throws Exception {
        String commande = parts[0];
        switch (commande) {
            case "catalogue":
                for (Produit produit : stock.values()) {
                    writer.println(produit); // Affiche le catalogue des produits disponibles
                }
                break;

            case "ajouter_au_chariot":
                if (parts.length == 3) {
                    String produitId = parts[1];
                    int quantite = Integer.parseInt(parts[2]);
                    Produit produit = stock.get(produitId);
                    if (produit == null) {
                        throw new StockUnavailableException("Erreur : Produit non trouvé.");
                    }
                    chariots.get(sessionId).ajouterProduit(produitId, quantite); // Ajoute le produit au chariot du client
                    writer.println("Produit ajouté au chariot : " + produitId);
                } else {
                    throw new InvalidOrderException("Erreur : Format invalide.");
                }
                break;

            case "commander":
                Chariot chariot = chariots.get(sessionId);
                Map<String, Integer> produits = chariot.getProduits();
                double total = 0;

                for (Map.Entry<String, Integer> entry : produits.entrySet()) {
                    Produit produit = stock.get(entry.getKey());
                    if (produit == null || produit.getQuantite() < entry.getValue()) {
                        throw new StockUnavailableException("Erreur : Stock insuffisant pour " + entry.getKey());
                    }
                    produit.setQuantite(produit.getQuantite() - entry.getValue()); // Mise à jour de la quantité du produit en stock
                    total += produit.getPrix() * entry.getValue(); // Calcul du total
                }

                Commande nouvelleCommande = new Commande(produits, total);
                commandes.get(sessionId).add(nouvelleCommande); // Ajoute la commande à la liste des commandes du client
                chariot.vider(); // Vide le chariot après la commande

                writer.println("Commande passée. Total : " + total);
                deliveryExecutor.submit(() -> handleDelivery(nouvelleCommande)); // Soumet la gestion de la livraison de la commande dans un thread séparé
                break;

            case "afficher_chariot":
                writer.println(chariots.get(sessionId).toString()); // Affiche le contenu du chariot du client
                break;

            case "voir_commandes":
                List<Commande> listeCommandes = commandes.get(sessionId);
                for (Commande c : listeCommandes) {
                    writer.println(c); // Affiche toutes les commandes du client
                }
                break;

            default:
                writer.println("Commande inconnue pour client.");
                break;
        }
    }

    // Méthode pour simuler la gestion de la livraison de la commande
    private void handleDelivery(Commande commande) {
        try {
            commande.setStatut("En préparation");
            Thread.sleep(new Random().nextInt(5000) + 2000); // Simulation d'un délai de préparation
            commande.setStatut("Expédiée");
            Thread.sleep(new Random().nextInt(5000) + 2000); // Simulation d'un délai d'expédition
            commande.setStatut("Livrée");
            System.out.println("Commande ID " + commande.getId() + " a été livrée.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Erreur pendant la livraison : " + e.getMessage());
        }
    }
}
