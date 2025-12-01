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

        final String categoriesFile = "expense_categories_2025.csv";
        final String ministriesFile = "expense_ministries_2025.csv";

        // ΛΥΣΗ: Η κλήση πρέπει να έχει τα δύο ορίσματα (Strings)
        final ExpenseDisplay display = new ExpenseDisplay(categoriesFile, ministriesFile);
        
        // ----------------------------------------------------
        // ΔΙΟΡΘΩΣΗ: Αφαιρούμε τη δοκιμή update για να δούμε τα αρχικά δεδομένα
        // ----------------------------------------------------
        /*
        final String categoryCodeToUpdate = "24"; 
        final long newAmount = 9876543210L; 
        System.out.println(">>> ΔΟΚΙΜΗ: Ενημέρωση ποσού για την Κατηγορία " 
            + categoryCodeToUpdate + " σε " + newAmount);
            
        final boolean success = display.updateCategoryAmount(categoryCodeToUpdate, newAmount);
        
        if (success) {
            System.out.println(">>> Η ενημέρωση ήταν επιτυχής.");
        } else {
            System.out.println(">>> Η ενημέρωση απέτυχε.");
        }
        System.out.println("\n" + "-".repeat(60) + "\n");
        */
        
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
