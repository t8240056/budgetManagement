package auebprogramming;

import java.util.Scanner;

/**
 * Test class to execute ExpenseManager methods and process user input.
 */
public final class TestExpenseManager {
    
    /** Private constructor to prevent instantiation. */
    private TestExpenseManager() {
        // Utility class
    }

    /**
     * Main method to run the expense manager.
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {
        
        // Use the correct filename from the Classpath
        final String categoriesFile = "expense_categories_2025.csv";
        final ExpenseManager manager = new ExpenseManager(categoriesFile);
        
        final Scanner scanner = new Scanner(System.in);

        // Display all categories first (Using the method that shows all details)
        // Αλλαγή επικεφαλίδας
        System.out.println("=== ΟΛΕΣ ΟΙ ΚΑΤΗΓΟΡΙΕΣ ΔΑΠΑΝΩΝ ===");
        manager.showCategories(); 

        // Ask user for codes
        System.out.println("\nΕισάγετε έναν ή περισσότερους κωδικούς δαπανών (διαχωρισμένοι με κενό ή κόμμα) για λεπτομέρειες:");
        final String input = scanner.nextLine().trim();

        // Split input by space or comma
        final String[] codes = input.split("[,\\s]+");

        // Αλλαγή επικεφαλίδας
        System.out.println("\n=== ΛΕΠΤΟΜΕΡΕΙΕΣ ΔΑΠΑΝΩΝ ===");
        manager.showExpenseDetails(codes);

        scanner.close();
    }
}
