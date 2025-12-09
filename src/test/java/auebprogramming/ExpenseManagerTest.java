package auebprogramming;

/**
 * Test class for ExpenseManager.
 * Simulates GUI calls to verify report generation and validation logic.
 */
public class ExpenseManagerTest {

    /**
     * Main method to execute tests.
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        
        // 1. Setup
        System.out.println(">>> INITIALIZING ExpenseManager...");
        final String categoriesFile = "expense_categories_2025.csv";
        final ExpenseManager manager = new ExpenseManager(categoriesFile);
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
        } catch (AppException e) {
            System.err.println("FAILED: Should not have thrown exception for 21.");
        }

        // Case B: Invalid Code
        try {
            System.out.print("Checking invalid code '999'... ");
            manager.validateExpenseCode("999");
            System.out.println("FAILED: Should have thrown exception.");
        } catch (AppException e) {
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
        // TEST 4: Expense Details (Specific Queries)
        // ---------------------------------------------------------
        System.out.println("========== TEST 4: EXPENSE DETAILS REPORT ==========");
        
        // Testing a mix of:
        // - "21" (Regular expense)
        // - "29" (Special expense with hardcoded logic)
        // - "XYZ" (Invalid code)
        final String[] queries = {"21", "29", "XYZ"};
        System.out.println("Querying codes: 21, 29, XYZ");
        
        final String detailsReport = manager.getExpenseDetailsReport(queries);
        System.out.println(detailsReport);
    }
}