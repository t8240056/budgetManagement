package auebprogramming;

/**
 * Helper class to resolve CSV filenames based on the selected year.
 * This keeps file paths out of the GUI code.
 */
public final class BudgetFileResolver {

    /**
     * Private constructor to prevent instantiation.
     */
    private BudgetFileResolver() {
        // Utility class
    }

    /**
     * Returns the categories CSV filename for a specific year.
     * @param year The budget year (e.g., 2025, 2026).
     * @return The filename string.
     */
    public static String getCategoriesFileForYear(final int year) {
        if (year == 2026) {
            return "expense_categories_2026.csv";
        } else {
            // Default to 2025
            return "expense_categories_2025.csv";
        }
    }

    /**
     * Returns the ministries CSV filename for a specific year.
     * @param year The budget year (e.g., 2025, 2026).
     * @return The filename string.
     */
    public static String getMinistriesFileForYear(final int year) {
        if (year == 2026) {
            return "expense_ministries_2026.csv";
        } else {
            // Default to 2025
            return "expense_ministries_2025.csv";
        }
    }
}
