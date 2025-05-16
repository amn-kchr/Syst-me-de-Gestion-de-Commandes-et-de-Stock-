import java.util.HashMap;
import java.util.Map;

public class Chariot {
    // Attributs privés de la classe Chariot
    private final Map<String, Integer> produits = new HashMap<>(); 
    // La carte (Map) stocke les produits dans le chariot, où la clé est l'ID du produit (String)
    // et la valeur est la quantité de ce produit dans le chariot (Integer).

    // Méthode pour ajouter un produit au chariot
    public void ajouterProduit(String idProduit, int quantite) {
        // Utilise la méthode getOrDefault() pour obtenir la quantité actuelle du produit, ou 0 s'il n'est pas encore dans le chariot.
        produits.put(idProduit, produits.getOrDefault(idProduit, 0) + quantite);
        // Ajoute ou met à jour la quantité du produit dans le chariot.
    }

    // Méthode pour obtenir tous les produits dans le chariot
    public Map<String, Integer> getProduits() {
        return produits;  // Retourne la carte des produits et de leurs quantités.
    }

    // Méthode pour vider le chariot (retirer tous les produits)
    public void vider() {
        produits.clear();  // Supprime tous les produits du chariot.
    }

    // Redéfinition de la méthode toString() pour afficher le contenu du chariot de manière lisible
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Chariot :\n");  // Crée un objet StringBuilder pour construire la chaîne de caractères.
        for (Map.Entry<String, Integer> entry : produits.entrySet()) {  // Parcourt chaque produit dans la carte.
            sb.append("Produit ID: ").append(entry.getKey())  // Ajoute l'ID du produit à la chaîne.
              .append(", Quantité: ").append(entry.getValue())  // Ajoute la quantité du produit à la chaîne.
              .append("\n");  // Ajoute une nouvelle ligne pour chaque produit.
        }
        return sb.toString();  // Retourne la chaîne finale représentant le chariot.
    }
}
