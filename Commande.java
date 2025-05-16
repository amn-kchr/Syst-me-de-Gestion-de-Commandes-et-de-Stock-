import java.util.HashMap;
import java.util.Map;

public class Commande {
    // Attributs de la classe Commande
    private static int compteur = 1;  // Compteur statique pour générer des ID de commande uniques
    private final int id;             // ID unique de la commande
    private final Map<String, Integer> produits;  // Liste des produits dans la commande (clé: ID produit, valeur: quantité)
    private final double total;       // Montant total de la commande
    private volatile String statut;   // Statut de la commande (ex: "En préparation", "Expédiée", "Livrée")

    // Constructeur de la classe Commande
    public Commande(Map<String, Integer> produits, double total) {
        this.id = compteur++;   // L'ID de la commande est attribué de manière unique avec le compteur statique
        this.produits = new HashMap<>(produits);  // Crée une nouvelle carte de produits pour éviter les modifications extérieures
        this.total = total;    // Initialise le montant total de la commande
        this.statut = "En préparation";  // Initialise le statut de la commande par défaut à "En préparation"
    }

    // Méthode pour obtenir l'ID de la commande
    public int getId() {
        return id;  // Retourne l'ID de la commande
    }

    // Méthode pour obtenir les produits de la commande
    public Map<String, Integer> getProduits() {
        return produits;  // Retourne la carte des produits dans la commande
    }

    // Méthode pour obtenir le total de la commande
    public double getTotal() {
        return total;  // Retourne le montant total de la commande
    }

    // Méthode synchronisée pour obtenir le statut de la commande
    public synchronized String getStatut() {
        return statut;  // Retourne le statut de la commande
    }

    // Méthode synchronisée pour modifier le statut de la commande
    public synchronized void setStatut(String statut) {
        this.statut = statut;  // Modifie le statut de la commande
    }

    // Redéfinition de la méthode toString() pour afficher les informations de la commande
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Commande ID: ").append(id).append("\n");  // Ajoute l'ID de la commande
        for (Map.Entry<String, Integer> entry : produits.entrySet()) {  // Parcourt les produits de la commande
            sb.append("Produit ID: ").append(entry.getKey())  // Ajoute l'ID du produit
              .append(", Quantité: ").append(entry.getValue()).append("\n");  // Ajoute la quantité du produit
        }
        sb.append("Total: ").append(total).append("\nStatut: ").append(statut).append("\n");  // Ajoute le total et le statut de la commande
        return sb.toString();  // Retourne la chaîne représentant la commande
    }
}
