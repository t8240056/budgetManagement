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

        final ExpenseDisplay display = new ExpenseDisplay(categoriesFile, ministriesFile);
        
        // ----------------------------------------------------
        // EXAMPLE UPDATE: Uncomment this block if you want to test data entry/update
        // ----------------------------------------------------
        /*
        final String categoryCodeToUpdate = "24"; 
        final long newAmount = 9876543210L; 
        System.out.println(">>> TEST: Updating amount for Category " 
            + categoryCodeToUpdate + " to " + newAmount);
            
        final boolean success = display.updateCategoryAmount(categoryCodeToUpdate, newAmount);
        
        if (success) {
            System.out.println(">>> Update successful.");
        } else {
            System.out.println(">>> Update failed.");
        }
        System.out.println("\n" + "-".repeat(60) + "\n");
        */
        
        // ----------------------------------------------------
        // DISPLAY RESULTS
        // ----------------------------------------------------

        // Display Expenses by Category
        display.displayCategories("ΚΡΑΤΙΚΟΣ");
        display.displayCategories("ΤΑΚΤΙΚΟΣ");
        display.displayCategories("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ");

        // Display Expenses by Ministry
        display.displayMinistries("ΚΡΑΤΙΚΟΣ");
        display.displayMinistries("ΤΑΚΤΙΚΟΣ");
        display.displayMinistries("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ");
    }
}
