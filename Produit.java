public class Produit {
    // Attributs privés de la classe Produit
    private String id;       // L'identifiant du produit (ex: code unique)
    private String nom;      // Le nom du produit
    private int quantite;    // La quantité disponible du produit
    private double prix;     // Le prix du produit

    // Constructeur de la classe Produit
    public Produit(String id, String nom, int quantite, double prix) {
        this.id = id;        // Initialise l'identifiant du produit
        this.nom = nom;      // Initialise le nom du produit
        this.quantite = quantite;  // Initialise la quantité du produit
        this.prix = prix;    // Initialise le prix du produit
    }

    // Méthode pour obtenir la quantité du produit
    public synchronized int getQuantite() {
        return quantite;   // Retourne la quantité du produit
    }

    // Méthode pour modifier la quantité du produit
    public synchronized void setQuantite(int quantite) {
        this.quantite = quantite;  // Modifie la quantité du produit
    }

    // Méthode pour mettre à jour la quantité et le prix du produit
    public synchronized void updateProduit(int quantite, double prix) {
        this.quantite = quantite;  // Modifie la quantité du produit
        this.prix = prix;          // Modifie le prix du produit
    }

    // Méthode pour obtenir l'identifiant du produit
    public String getId() {
        return id;  // Retourne l'identifiant du produit
    }

    // Méthode pour obtenir le nom du produit
    public String getNom() {
        return nom;  // Retourne le nom du produit
    }

    // Méthode pour obtenir le prix du produit
    public double getPrix() {
        return prix;  // Retourne le prix du produit
    }

    // Redéfinition de la méthode toString() pour afficher les informations du produit
    @Override
    public String toString() {
        return id + " | " + nom + " | Quantité: " + quantite + " | Prix: " + prix;
    }
}
