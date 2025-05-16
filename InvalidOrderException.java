// Déclaration de la classe InvalidOrderException qui étend la classe Exception
public class InvalidOrderException extends Exception {
    
    // Constructeur de la classe InvalidOrderException
    public InvalidOrderException(String message) {
        // Appel au constructeur de la classe parente (Exception) avec le message d'erreur
        super(message);
    }
}
