// Déclaration de la classe StockUnavailableException qui étend la classe Exception
public class StockUnavailableException extends Exception {
    
    // Constructeur de la classe StockUnavailableException
    public StockUnavailableException(String message) {
        // Appel au constructeur de la classe parente (Exception) avec le message d'erreur
        super(message);
    }
}
