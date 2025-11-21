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
                case 1: {
                    System.out.println("\nΤι θα θέλατε να δείτε;");
                    subMenu();
                    int subChoice1 = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer
                    // TODO: Display budget based on subChoice1
                    break;
                }
                case 2: {
                    System.out.println("\nΤι θα θέλατε να αλλάξετε;");
                    subMenu();
                    int subChoice2 = scanner.nextInt();
                    scanner.nextLine(); // Clear the buffer
                    // TODO: Add change based on subChoice2
                    break;
                }
                case 3: {
                    // TODO: Show changes
                    break;
                }
                case 4: {
                    System.out.println("\nΈξοδος από την εφαρμογή. Σας ευχαριστούμε για την επισκεψή σας.");
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("\nΜη έγκυρη επιλογή.");
                }
            }
        }
    }

    /**
     * Prints the main menu to the user.
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

    /**
     * Prints the submenu to the user.
     */
    public static void subMenu() {
        System.out.println("\n1. Γενικές πληροφορίες");
        System.out.println("2. Συγκεκριμένο Υπουργείο");
        System.out.print("Επιλέξτε επιλογή: ");
    }
}
