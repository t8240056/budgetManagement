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
     */
    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = SCANNER.nextInt();
            SCANNER.nextLine(); // Clear the buffer

            switch (choice) {
                case 1: {
                    System.out.println("\nΤι θα θέλατε να δείτε;");
                    subMenu();
                    int subChoice1 = SCANNER.nextInt();
                    SCANNER.nextLine(); // Clear the buffer
                    switch (subChoice1) {
                        case 1: {
                            incomeExpensesMenu();
                            int incexpChoice = SCANNER.nextInt();
                            SCANNER.nextLine(); // Clear the buffer
                            switch (incexpChoice) {
                                case 1: {
                                    // TODO: Display general information
                                    break;
                                }
                                case 2: {
                                    // TODO: Display general information
                                    break;
                                }
                                case 3: {
                                    System.out.println("\nΈξοδος από την εφαρμογή. "
                                    + "Σας ευχαριστούμε για την επισκεψή σας.");
                                    System.exit(0);
                                    break;
                                }
                                default: {
                                    System.out.println("\nΜη έγκυρη επιλογή.");
                                }
                            }
                            // TODO: Display general information
                            break;
                        }
                        case 2: {
                            ministriesMenu();
                            break;
                        }
                        case 3: {
                            System.out.println("\nΈξοδος από την εφαρμογή. "
                                    + "Σας ευχαριστούμε για την επισκεψή σας.");
                            System.exit(0);
                            break;
                        }
                        default: {
                            System.out.println("\nΜη έγκυρη επιλογή.");
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("\nΤι θα θέλατε να αλλάξετε;");
                    subMenu();
                    int subChoice2 = SCANNER.nextInt();
                    SCANNER.nextLine(); // Clear the buffer
                    // TODO: Add change based on subChoice2
                    break;
                }
                case 3: {
                    // TODO: Show changes
                    break;
                }
                case 4: {
                    System.out.println("\nΈξοδος από την εφαρμογή. "
                            + "Σας ευχαριστούμε για την επισκεψή σας.");
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
        System.out.println("3. Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }

    /**
     * Prints the ministries to the user.
     */
    public static void ministriesMenu() {
        System.out.println("\nSelect a Ministry:");
        System.out.println("0. Έξοδος");
        System.out.println("1. Ministry of Interior");
        System.out.println("2. Ministry of Foreign Affairs");
        System.out.println("3. Ministry of National Defense");
        System.out.println("4. Ministry of Health");
        System.out.println("5. Ministry of Justice");
        System.out.println("6. Ministry of Education, Religious Affairs and Sports");
        System.out.println("7. Ministry of Culture");
        System.out.println("8. Ministry of National Economy and Finance");
        System.out.println("9. Ministry of Rural Development and Food");
        System.out.println("10. Ministry of Environment and Energy");
        System.out.println("11. Ministry of Labor and Social Security");
        System.out.println("12. Ministry of Social Cohesion and Family");
        System.out.println("13. Ministry of Development");
        System.out.println("14. Ministry of Infrastructure and Transport");
        System.out.println("15. Ministry of Shipping and Island Policy");
        System.out.println("16. Ministry of Tourism");
        System.out.println("17. Ministry of Digital Governance");
        System.out.println("18. Ministry of Migration and Asylum");
        System.out.println("19. Ministry of Citizen Protection");
        System.out.println("20. Ministry of Climate Crisis and Civil Protection");
        System.out.print("Select a Ministry: ");
    }

    public static void incomeExpensesMenu() {
        System.out.println("1.Έσοδα");
        System.out.println("2.Έξοδα");
        System.out.println("3.Έξοδος");
        System.out.print("Επιλέξτε επιλογή: ");
    }
}

