package auebprogramming;

/**
 * Test class for BudgetFileResolver.
 * Verifies that the correct filenames are returned based on the input year.
 * <p>
 * This ensures that the GUI will load the correct data when the user
 * selects a specific year (e.g., 2025 vs 2026).
 * </p>
 *
 * @version 1.0
 */
public final class BudgetFileResolverTest {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetFileResolverTest() {
        // Empty constructor
    }

    /**
     * Main method to execute tests.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {

        System.out.println(">>> STARTING TESTS: BudgetFileResolver");
        System.out.println("--------------------------------------------------");

        // ---------------------------------------------------------
        // TEST 1: Check Year 2025 (Default Scenario)
        // ---------------------------------------------------------
        System.out.println("TEST 1: Resolving files for Year 2025...");
        
        final String cat2025 = BudgetFileResolver
                .getCategoriesFileForYear(2025);
        final String min2025 = BudgetFileResolver
                .getMinistriesFileForYear(2025);

        validateResult("2025 Categories", 
                "expense_categories_2025.csv", cat2025);
        validateResult("2025 Ministries", 
                "expense_ministries_2025.csv", min2025);
        System.out.println();

        // ---------------------------------------------------------
        // TEST 2: Check Year 2026 (New Scenario)
        // ---------------------------------------------------------
        System.out.println("TEST 2: Resolving files for Year 2026...");
        
        final String cat2026 = BudgetFileResolver
                .getCategoriesFileForYear(2026);
        final String min2026 = BudgetFileResolver
                .getMinistriesFileForYear(2026);

        validateResult("2026 Categories", 
                "expense_categories_2026.csv", cat2026);
        validateResult("2026 Ministries", 
                "expense_ministries_2026.csv", min2026);
        System.out.println();

        // ---------------------------------------------------------
        // TEST 3: Check Fallback (Unknown Year)
        // ---------------------------------------------------------
        System.out.println("TEST 3: Resolving files for Unknown Year (2099)...");
        System.out.println("(Should fallback to default 2025)");

        final String catDefault = BudgetFileResolver
                .getCategoriesFileForYear(2099);
        
        // Expecting 2025 file as default behavior
        validateResult("Default Categories", 
                "expense_categories_2025.csv", catDefault);

        System.out.println("--------------------------------------------------");
        System.out.println(">>> TESTS COMPLETED.");
    }

    /**
     * Helper method to validate and print test results.
     *
     * @param label    The label for the test case.
     * @param expected The expected filename.
     * @param actual   The actual filename returned by the resolver.
     */
    private static void validateResult(final String label,
                                       final String expected,
                                       final String actual) {
        if (expected.equals(actual)) {
            System.out.printf("[PASS] %-20s -> %s%n", label, actual);
        } else {
            System.err.printf("[FAIL] %-20s -> Expected: %s, But got: %s%n",
                    label, expected, actual);
        }
    }
}
