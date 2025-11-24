package auebprogramming;

import java.util.Scanner;

/**
 * Main class for the "Prime Minister for a Day" application.
 */
public class Main {

    /** Scanner for reading user input. */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Main method that starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = SCANNER.nextInt();
            SCANNER.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    System.out.println("\nΤι θα θέλατε να δείτε;");
                    subMenu();
                    int subChoice1 = SCANNER.nextInt();
                    SCANNER.nextLine();

                    switch (subChoice1) {
                        case 1:
                            incomeExpensesMenu();
                            int incExpChoice = SCANNER.nextInt();
                            SCANNER.nextLine();

                            switch (incExpChoice) {
                                case 1:
                                    // TODO: Display income info
                                    break;
                                case 2:
                                    // TODO: Display expenses info
                                    break;
                                case 3:
                                    System.out.println(
                                            "\nΈξοδος από την εφαρμογή. "
                                                    + "Σας ευχαριστούμε για την επίσκεψή σας.");
                                    System.exit(0);
                                    break;
                                default:
                                    System.out.println("\nΜη έγκυρη επιλογή.");
                                    break;
                            }
                            break;

                        case 2:
                            ministriesMenu();
                            int ministryChoice = SCANNER.nextInt();
                            SCANNER.nextLine();
                            // TODO: Display ministry info
                            break;

                        case 3:
                            System.out.println(
                                    "\nΈξοδος από την εφαρμογή. "
                                            + "Σας ευχαριστούμε για την επίσκεψή σας.");
                            System.exit(0);
                            break;

                        default:
                            System.out.println("\nΜη έγκυρη επιλογή.");
                            break;
                    }
                    break;

                case 2:
                    System.out.println("\nΤι θα θέλατε να αλλάξετε;");
                    subMenu();
                    int subChoice2 = SCANNER.nextInt();
                    SCANNER.nextLine();
                    // TODO: Apply changes
                    break;

                case 3:
                    // TODO: Show changes made
                    break;

                case 4:
                    System.out.println(
                            "\nΈξοδος από την εφαρμογή. "
                                    + "Σας ευχαριστούμε για την επίσκεψή σας.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("\nΜη έγκυρη επιλογή.");
                    break;
            }
        }
    }

    /** Prints the main menu. */
    public static void printMenu() {
        System.out.println();
        System.out.println("=== Πρωθυπουργός για μια μέρα ===");
        System.out.println("1. Προβολή προϋπολογισμού");
        System.out.println("2. Εισαγωγή αλλαγής");
        System.out.println("3. Εμφάνιση αλλαγών");
        System.out.println("4. Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }

    /** Prints the submenu. */
    public static void subMenu() {
        System.out.println("\n1. Γενικές πληροφορίες");
        System.out.println("2. Συγκεκριμένο Υπουργείο");
        System.out.println("3. Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }

    /** Prints the ministries menu. */
    public static void ministriesMenu() {
        System.out.println("\nΕπιλέξτε Υπουργείο:");
        System.out.println("0. Έξοδος");
        System.out.println("1. Υπουργείο Εσωτερικών");
        System.out.println("2. Υπουργείο Εξωτερικών");
        System.out.println("3. Υπουργείο Εθνικής Άμυνας");
        System.out.println("4. Υπουργείο Υγείας");
        System.out.println("5. Υπουργείο Δικαιοσύνης");
        System.out.println("6. Υπουργείο Παιδείας, Θρησκευμάτων και Αθλητισμού");
        System.out.println("7. Υπουργείο Πολιτισμού");
        System.out.println("8. Υπουργείο Εθνικής Οικονομίας και Οικονομικών");
        System.out.println("9. Υπουργείο Αγροτικής Ανάπτυξης και Τροφίμων");
        System.out.println("10. Υπουργείο Περιβάλλοντος και Ενέργειας");
        System.out.println("11. Υπουργείο Εργασίας και Κοινωνικής Ασφάλισης");
        System.out.println("12. Υπουργείο Κοινωνικής Συνοχής και Οικογένειας");
        System.out.println("13. Υπουργείο Ανάπτυξης");
        System.out.println("14. Υπουργείο Υποδομών και Μεταφορών");
        System.out.println("15. Υπουργείο Ναυτιλίας και Νησιωτικής Πολιτικής");
        System.out.println("16. Υπουργείο Τουρισμού");
        System.out.println("17. Υπουργείο Ψηφιακής Διακυβέρνησης");
        System.out.println("18. Υπουργείο Μετανάστευσης και Ασύλου");
        System.out.println("19. Υπουργείο Προστασίας του Πολίτη");
        System.out.println("20. Υπουργείο Κλιματικής Κρίσης και Πολιτικής Προστασίας");
        System.out.print("Επιλέξτε επιλογή: ");
    }

    /** Prints the income/expenses menu. */
    public static void incomeExpensesMenu() {
        System.out.println("1. Έσοδα");
        System.out.println("2. Έξοδα");
        System.out.println("3. Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }
}
