package auebprogramming;

/**
 * Test class for ExpenseManager.
 * Simulates GUI calls to verify report generation and validation logic.
 * <p>
 * This class demonstrates how the front-end should interact with the
 * ExpenseManager to retrieve formatted strings and handle exceptions.
 * </p>
 *
 * @version 1.0
 */
public final class ExpenseManagerTest {

    /** The filename for the expense categories CSV. */
    private static final String CATEGORIES_FILE = "expense_categories_2025.csv";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ExpenseManagerTest() {
        // Empty constructor
    }

    /**
     * Main method to execute tests.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {
        
        System.out.println(">>> INITIALIZING ExpenseManager...");
        
        // Ensure the file exists in src/main/resources
        final ExpenseManager manager = new ExpenseManager(CATEGORIES_FILE);
        
        System.out.println(">>> Initialization Complete.\n");

        // ---------------------------------------------------------
        // TEST 1: Validation Logic (AppException)
        // ---------------------------------------------------------
        System.out.println("========== TEST 1: VALIDATION ==========");
        
        // Case A: Valid Code
        try {
            System.out.print("Checking valid code '21'... ");
            manager.validateExpenseCode("21");
            System.out.println("OK (Valid).");
        } catch (final AppException e) {
            System.err.println("FAILED: Should not have thrown exception for code 21.");
        }

        // Case B: Invalid Code
        try {
            System.out.print("Checking invalid code '999'... ");
            manager.validateExpenseCode("999");
            System.out.println("FAILED: Should have thrown exception.");
        } catch (final AppException e) {
            System.out.println("OK (Caught expected exception): " + e.getMessage());
        }
        System.out.println();

        // ---------------------------------------------------------
        // TEST 2: Category List Report (Drop-down menu source)
        // ---------------------------------------------------------
        System.out.println("========== TEST 2: CATEGORY LIST REPORT ==========");
        final String listReport = manager.getCategoryListReport();
        System.out.println(listReport);

        // ---------------------------------------------------------
        // TEST 3: Full Expenses Report (State Budget Table)
        // ---------------------------------------------------------
        System.out.println("========== TEST 3: FULL EXPENSES REPORT ==========");
        final String fullReport = manager.getFullExpensesReport();
        System.out.println(fullReport);

        // ---------------------------------------------------------
        // TEST 4: Expense Details Report (Specific Queries)
        // ---------------------------------------------------------
        System.out.println("========== TEST 4: EXPENSE DETAILS REPORT ==========");
        
        // Testing a mix of:
        // - "21" (Regular expense)
        // - "29" (Special expense with hardcoded logic)
        // - "XYZ" (Invalid code to verify error handling in report)
        final String[] queries = {"21", "29", "XYZ"};
        System.out.println("Querying codes: 21, 29, XYZ");
        
        final String detailsReport = manager.getExpenseDetailsReport(queries);
        System.out.println(detailsReport);
    }
}
