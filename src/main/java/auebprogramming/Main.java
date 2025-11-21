package auebprogramming;

import java.util.Scanner;

/**
 * Main class for the "Prime Minister for a Day" application.
 */
public class Main {

    /** Scanner for reading user input. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main method that starts the application.
     */
    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            switch (choice) {
                case 1:
                    // TODO: Display budget
                    break;
                case 2:
                    // TODO: Add change
                    break;
                case 3:
                    // TODO: Show changes
                    break;
                case 4:
                    System.out.println("Έξοδος από την εφαρμογή. Σας ευχαριστούμε για την επισκεψή σας.");
                    System.exit(0);
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
    }

    /**
     * Prints the user menu (in Greek).
     */
    public static void printMenu() {
        System.out.println();
        System.out.println("=== Πρωθυπουργός για μια μέρα ===");
        System.out.println("1. Προβολή προϋπολογισμού");
        System.out.println("2. Εισαγωγή αλλαγής");
        System.out.println("3. Εμφάνιση αλλαγών");
        System.out.println("4. Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }
}