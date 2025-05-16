import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GestionnaireStockClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connecté au serveur !");
            System.out.println("Serveur : " + reader.readLine());

            while (true) {
                System.out.print("Vous : ");
                String commande = scanner.nextLine();
                writer.println(commande);

                if (commande.equalsIgnoreCase("quitter")) {
                    System.out.println("Déconnexion...");
                    break;
                }

                String response;
                while (!(response = reader.readLine()).equals("FIN")) {
                    System.out.println("Serveur : " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}