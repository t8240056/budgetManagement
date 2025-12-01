package auebprogramming;

/**
 * Test class to execute ExpenseDisplay independently and demonstrate updates.
 */
public final class TestExpenseDisplay {

    /**
     * Main method to run the expense display.
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {

        // Ορισμός των ονομάτων των αρχείων (στο src/main/resources).
        final String categoriesFile = "expense_categories_2025.csv";
        final String ministriesFile = "expense_ministries_2025.csv";

        // Η ExpenseDisplay τώρα φορτώνει και αποθηκεύει τους πίνακες εσωτερικά.
        final ExpenseDisplay display = new ExpenseDisplay(categoriesFile, ministriesFile);
        
        // ----------------------------------------------------
        // ΔΟΚΙΜΗ ΕΙΣΑΓΩΓΗΣ ΑΛΛΑΓΩΝ (UPDATE)
        // ----------------------------------------------------
        
        System.out.println("\n" + "-".repeat(60) + "\n");
        
        // ----------------------------------------------------
        // ΕΜΦΑΝΙΣΗ ΤΩΝ ΑΠΟΤΕΛΕΣΜΑΤΩΝ (με τις αλλαγές)
        // ----------------------------------------------------

        // Εμφάνιση Εξόδων ανά Δαπάνη (Κατηγορία)
        display.displayCategories("ΚΡΑΤΙΚΟΣ");
        display.displayCategories("ΤΑΚΤΙΚΟΣ");
        display.displayCategories("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ");

        // Εμφάνιση Εξόδων ανά Φορέα
        display.displayMinistries("ΚΡΑΤΙΚΟΣ");
        display.displayMinistries("ΤΑΚΤΙΚΟΣ");
        display.displayMinistries("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ");
    }
}
