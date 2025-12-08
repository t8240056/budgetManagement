package auebprogramming;

import java.util.Locale;

/**
 * ExpenseManager loads expense data from CSV files and provides methods
 * for targeted display and data querying by code.
 */
public final class ExpenseManager {

    // Constants for data position in the arrays
    private static final int CATEGORY_CODE_COLUMN = 1;
    private static final int CATEGORY_DESCRIPTION_COLUMN = 2;
    private static final int CATEGORY_STATE_BUDGET_COLUMN = 3;

    // Array for category data loaded from CSV
    private final String[][] categoriesData;

    /**
     * Constructor. Loads data from the expense categories CSV file.
     *
     * @param categoriesFile The filename of the categories CSV.
     */
    public ExpenseManager(final String categoriesFile) {
        // Use CsvToArray to load data from the classpath
        this.categoriesData = CsvToArray.loadCsvToArray(categoriesFile);
    }

    // ---------------------------
    // GUI REPORT METHODS (Replacing System.out)
    // ---------------------------

    /**
     * Generates a list of all available expense categories with their codes as a formatted String.
     * @return The formatted list as a String.
     */
    public String getCategoryListReport() {
        final StringBuilder sb = new StringBuilder();

        // Append Header
        sb.append("ΚΩΔΙΚΟΣ\tΟΝΟΜΑ ΔΑΠΑΝΗΣ").append(System.lineSeparator());

        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < categoriesData.length; i++) {
            final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
            final String name = categoriesData[i][CATEGORY_DESCRIPTION_COLUMN];

            // Append row output to StringBuilder
            sb.append(String.format("%s\t%s%n", code, name));
        }

        return sb.toString();
    }

    /**
     * Generates details for one or more expense codes as a single formatted String.
     * @param codes One or more expense codes (e.g., "21", "23").
     * @return The formatted details report as a String.
     */
    public String getExpenseDetailsReport(final String... codes) {
        final StringBuilder sb = new StringBuilder();

        for (final String code : codes) {
            final int index = findRowIndexByCode(code);

            if (index != -1) {
                // Use getAmountsForRow to safely extract the values
                final long[] amounts = getAmountsForRow(index);

                final String name = categoriesData[index][CATEGORY_DESCRIPTION_COLUMN];

                // Append formatted details to StringBuilder
                sb.append(System.lineSeparator()).append("==============================").append(System.lineSeparator());
                sb.append("ΚΩΔΙΚΟΣ: ").append(categoriesData[index][CATEGORY_CODE_COLUMN]).append(System.lineSeparator());
                sb.append("ΟΝΟΜΑ: ").append(name).append(System.lineSeparator());
                sb.append("------------------------------").append(System.lineSeparator());

                // Use Locale.GERMAN for correct thousand separators
                sb.append(String.format(Locale.GERMAN, "Κρατικός Προϋπολογισμός: %,d €%n", amounts[0]));
                sb.append(String.format(Locale.GERMAN, "Τακτικός Προϋπολογισμός : %,d €%n", amounts[1]));
                sb.append(String.format(Locale.GERMAN, "Πρ. Δημοσίων Επενδύσεων: %,d €%n", amounts[2]));
                sb.append("==============================").append(System.lineSeparator());

            } else {
                // Append error message
                sb.append(System.lineSeparator()).append(code).append(" : Μη έγκυρος κωδικός").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    /**
     * Generates the report of all expense categories with their code, name, and State Budget amount as a formatted String.
     * @return The formatted expense report as a String.
     */
    public String getFullExpensesReport() {
        final StringBuilder sb = new StringBuilder();
        long totalStateBudget = 0;

        // Append Header
        sb.append("1. ΕΞΟΔΑ").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(String.format("%-5s %-60s %s%n", "ΚΩΔ.", "ΠΕΡΙΓΡΑΦΗ", "ΠΟΣΟ (ΕΥΡΩ)"));
        sb.append("----------------------------------------------------------------------------------").append(System.lineSeparator());

        // Print each row in the requested format
        for (int i = 1; i < categoriesData.length; i++) {
            final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
            final String name = categoriesData[i][CATEGORY_DESCRIPTION_COLUMN];
            final long[] amounts = getAmountsForRow(i);
            final long amount = amounts[0]; // State Budget amount

            totalStateBudget += amount;

            // Append formatted row to StringBuilder
            sb.append(String.format(Locale.GERMAN, "%-5s %-60s %,15d%n",
                    code + ".",
                    name,
                    amount
            ));
        }

        // Append Total
        sb.append("----------------------------------------------------------------------------------").append(System.lineSeparator());
        sb.append(String.format(Locale.GERMAN, "Σύνολο: %,d Ευρώ%n", totalStateBudget));
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    // ---------------------------
    // PRIVATE HELPER METHODS
    // ---------------------------

    /**
     * Finds the row index in the categoriesData array by expense code.
     *
     * @param code The expense code to search for.
     * @return The row index, or -1 if not found.
     */
    private int findRowIndexByCode(final String code) {
        // Start from 1 to skip the header
        for (int i = 1; i < categoriesData.length; i++) {
            if (categoriesData[i][CATEGORY_CODE_COLUMN].equals(code)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Extracts and calculates the three budget amounts for a given row index.
     * Handles the special case of Code 29 logic.
     *
     * @param rowIndex The index of the row to process.
     * @return A long array: [State Budget, Regular Budget, Investment Budget].
     */
    private long[] getAmountsForRow(final int rowIndex) {
        long stateBudget;
        long regularBudget;
        long investmentBudget;

        final String code = categoriesData[rowIndex][CATEGORY_CODE_COLUMN];

        // Logic for the special code 29 (Appropriations under distribution)
        if ("29".equals(code)) {
            // Use the hardcoded values (since they are not in the CSV structure)
            stateBudget = 17283053000L;
            regularBudget = 3183053000L;
            investmentBudget = 14100000000L;

        } else {
            // Normal logic: Regular = State Budget (and Investments = 0) for most expenses.
            try {
                // Read the total from CSV (column 3, as in your original CSV)
                stateBudget = Long.parseLong(categoriesData[rowIndex][CATEGORY_STATE_BUDGET_COLUMN].replace(" ", ""));
                regularBudget = stateBudget;
                investmentBudget = 0;
            } catch (final NumberFormatException e) {
                stateBudget = 0;
                regularBudget = 0;
                investmentBudget = 0;
            }
        }

        return new long[] { stateBudget, regularBudget, investmentBudget };
    }
}
