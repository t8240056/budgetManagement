package auebprogramming;

/**
 * Test class to execute ExpenseDisplay independently.
 * Verifies that all 6 reports are generated correctly for the GUI.
 */
public final class ExpenseDisplayTest {

    public static void main(final String[] args) {

        System.out.println(">>> INITIALIZING ExpenseDisplay...");
        final String categoriesFile = "expense_categories_2025.csv";
        final String ministriesFile = "expense_ministries_2025.csv";

        final ExpenseDisplay display = new ExpenseDisplay(categoriesFile, ministriesFile);
        System.out.println(">>> Initialization Complete.\n");

        // ----------------------------------------------------
        // TEST 1: Λήψη όλων των αναφορών (Η μέθοδος που θα καλεί το GUI)
        // ----------------------------------------------------
        System.out.println("========== TEST 1: GET ALL REPORTS ==========");
        final String[] allReports = display.getAllExpenseReports();

        if (allReports.length == 6) {
            System.out.println("SUCCESS: Received 6 reports as expected.");
        } else {
            System.err.println("FAILURE: Expected 6 reports, got " + allReports.length);
            return;
        }

        // ----------------------------------------------------
        // TEST 2: Εκτύπωση των Αναφορών για Έλεγχο
        // ----------------------------------------------------
        
        // --- ΑΝΑΦΟΡΕΣ ΑΝΑ ΔΑΠΑΝΗ (Indices 0, 1, 2) ---
        System.out.println("\n--------------------------------------------------");
        System.out.println("   [0] CATEGORIES - KRATIKOS");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[0]);

        System.out.println("\n--------------------------------------------------");
        System.out.println("   [1] CATEGORIES - TAKTIKOS");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[1]);

        System.out.println("\n--------------------------------------------------");
        System.out.println("   [2] CATEGORIES - EPENDYSEON");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[2]);

        // --- ΑΝΑΦΟΡΕΣ ΑΝΑ ΦΟΡΕΑ (Indices 3, 4, 5) ---
        System.out.println("\n--------------------------------------------------");
        System.out.println("   [3] MINISTRIES - KRATIKOS");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[3]);

        System.out.println("\n--------------------------------------------------");
        System.out.println("   [4] MINISTRIES - TAKTIKOS");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[4]);

        System.out.println("\n--------------------------------------------------");
        System.out.println("   [5] MINISTRIES - EPENDYSEON");
        System.out.println("--------------------------------------------------");
        System.out.println(allReports[5]);
    }
}
