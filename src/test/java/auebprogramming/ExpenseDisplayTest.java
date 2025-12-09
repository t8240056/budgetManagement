package auebprogramming;

/**
 * Test class to execute ExpenseDisplay independently.
 * Verifies that all 6 reports are generated correctly for the GUI.
 * <p>
 * This class ensures that the Display component correctly loads data
 * and formats the output strings for the user interface.
 * </p>
 *
 * @version 1.0
 */
public final class ExpenseDisplayTest {

    /** The filename for the expense categories CSV. */
    private static final String CATEGORIES_FILE = "expense_categories_2025.csv";
    
    /** The filename for the ministry expenses CSV. */
    private static final String MINISTRIES_FILE = "expense_ministries_2025.csv";

    /** Expected number of reports returned by getAllExpenseReports. */
    private static final int EXPECTED_REPORT_COUNT = 6;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ExpenseDisplayTest() {
        // Empty constructor
    }

    /**
     * Main method to execute tests.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {

        System.out.println(">>> INITIALIZING ExpenseDisplay...");
        
        final ExpenseDisplay display = new ExpenseDisplay(CATEGORIES_FILE, MINISTRIES_FILE);
        
        System.out.println(">>> Initialization Complete.\n");

        // ----------------------------------------------------
        // TEST 1: Retrieve all reports (The main GUI method)
        // ----------------------------------------------------
        System.out.println("========== TEST 1: GET ALL REPORTS ==========");
        final String[] allReports = display.getAllExpenseReports();

        if (allReports.length == EXPECTED_REPORT_COUNT) {
            System.out.println("SUCCESS: Received 6 reports as expected.");
        } else {
            System.err.println("FAILURE: Expected 6 reports, got " + allReports.length);
            return;
        }

        // ----------------------------------------------------
        // TEST 2: Print Reports for Verification
        // ----------------------------------------------------
        
        // --- CATEGORY REPORTS (Indices 0, 1, 2) ---
        printReportHeader("0", "CATEGORIES - KRATIKOS");
        System.out.println(allReports[0]);

        printReportHeader("1", "CATEGORIES - TAKTIKOS");
        System.out.println(allReports[1]);

        printReportHeader("2", "CATEGORIES - EPENDYSEON");
        System.out.println(allReports[2]);

        // --- MINISTRY REPORTS (Indices 3, 4, 5) ---
        printReportHeader("3", "MINISTRIES - KRATIKOS");
        System.out.println(allReports[3]);

        printReportHeader("4", "MINISTRIES - TAKTIKOS");
        System.out.println(allReports[4]);

        printReportHeader("5", "MINISTRIES - EPENDYSEON");
        System.out.println(allReports[5]);
    }

    /**
     * Helper method to print a formatted header for test outputs.
     *
     * @param index The index of the report in the array.
     * @param title The description of the report.
     */
    private static void printReportHeader(final String index, final String title) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("   [" + index + "] " + title);
        System.out.println("--------------------------------------------------");
    }
}
