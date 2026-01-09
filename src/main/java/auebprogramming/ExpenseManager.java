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

    // Array for category data
    private final String[][] categoriesData;

    /**
     * Constructor 1: Loads data from the expense categories CSV file.
     * Used by the main application.
     *
     * @param categoriesFile The filename of the categories CSV (classpath
     * resource).
     */
    public ExpenseManager(final String categoriesFile) {
        // Use CsvToArray to load data from the classpath
        this.categoriesData = CsvToArray.loadCsvToArray(categoriesFile);
    }

    /**
     * Constructor 2: Accepts raw data directly.
     * Used specifically for UNIT TESTING to avoid file I/O issues.
     *
     * @param data The 2D array containing the data.
     */
    public ExpenseManager(final String[][] data) {
        this.categoriesData = data;
    }

    // ---------------------------
    // PUBLIC VALIDATION METHOD (THROWS EXCEPTION)
    // ---------------------------

    /**
     * Validates if the given expense code exists.
     * Throws an AppException if the code is not found.
     *
     * @param code The expense code to validate.
     * @throws AppException If the code is not found in the expenses list.
     */
    public void validateExpenseCode(final String code) throws AppException {
        if (findRowIndexByCode(code) == -1) {
            throw new AppException("Σφάλμα: ο κωδικός " + code
                    + " δεν ανήκει στις δαπανες εξοδων");
        }
    }

    // ---------------------------
    // GUI REPORT METHODS
    // ---------------------------

    /**
     * Generates a list of all available expense categories.
     *
     * @return The formatted list as a String.
     */
    public String getCategoryListReport() {
        final StringBuilder sb = new StringBuilder();

        // Append Header
        sb.append("ΚΩΔΙΚΟΣ\tΟΝΟΜΑ ΔΑΠΑΝΗΣ").append(System.lineSeparator());

        // Check if data is loaded to avoid NullPointerException
        if (categoriesData == null || categoriesData.length == 0) {
            return sb.toString();
        }

        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < categoriesData.length; i++) {
            // Safety check for row length
            if (categoriesData[i].length <= CATEGORY_DESCRIPTION_COLUMN) {
                continue;
            }

            final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
            final String name = categoriesData[i][CATEGORY_DESCRIPTION_COLUMN];

            sb.append(String.format("%s\t%s%n", code, name));
        }

        return sb.toString();
    }

    /**
     * Generates details for one or more expense codes.
     *
     * @param codes One or more expense codes.
     * @return The formatted details report as a String.
     */
    public String getExpenseDetailsReport(final String... codes) {
        final StringBuilder sb = new StringBuilder();

        for (final String code : codes) {
            final int index = findRowIndexByCode(code);

            if (index != -1) {
                final long[] amounts = getAmountsForRow(index);

                // Safety check
                String name = "N/A";
                if (categoriesData[index].length > CATEGORY_DESCRIPTION_COLUMN) {
                    name = categoriesData[index][CATEGORY_DESCRIPTION_COLUMN];
                }

                sb.append(System.lineSeparator())
                        .append("==============================")
                        .append(System.lineSeparator());
                sb.append("ΚΩΔΙΚΟΣ: ")
                        .append(categoriesData[index][CATEGORY_CODE_COLUMN])
                        .append(System.lineSeparator());
                sb.append("ΟΝΟΜΑ: ").append(name)
                        .append(System.lineSeparator());
                sb.append("------------------------------")
                        .append(System.lineSeparator());

                sb.append(String.format(Locale.GERMAN,
                        "Κρατικός Προϋπολογισμός: %,d €%n", amounts[0]));
                sb.append(String.format(Locale.GERMAN,
                        "Τακτικός Προϋπολογισμός : %,d €%n", amounts[1]));
                sb.append(String.format(Locale.GERMAN,
                        "Πρ. Δημοσίων Επενδύσεων: %,d €%n", amounts[2]));
                sb.append("==============================")
                        .append(System.lineSeparator());

            } else {
                sb.append(System.lineSeparator()).append(code)
                        .append(" : Μη έγκυρος κωδικός")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    /**
     * Generates the full report of all expense categories.
     *
     * @return The formatted expense report as a String.
     */
    public String getFullExpensesReport() {
        final StringBuilder sb = new StringBuilder();
        long totalStateBudget = 0;

        sb.append("1. ΕΞΟΔΑ").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(String.format("%-5s %-60s %s%n",
                "ΚΩΔ.", "ΠΕΡΙΓΡΑΦΗ", "ΠΟΣΟ (ΕΥΡΩ)"));
        sb.append("------------------------------------------"
                + "----------------------------------------")
                .append(System.lineSeparator());

        if (categoriesData != null) {
            for (int i = 1; i < categoriesData.length; i++) {
                // Basic validation needed in case of malformed rows
                if (categoriesData[i].length <= CATEGORY_DESCRIPTION_COLUMN) {
                    continue;
                }

                final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
                final String name = categoriesData[i]
                        [CATEGORY_DESCRIPTION_COLUMN];
                final long[] amounts = getAmountsForRow(i);
                final long amount = amounts[0];

                totalStateBudget += amount;

                sb.append(String.format(Locale.GERMAN, "%-5s %-60s %,15d%n",
                        code + ".",
                        name,
                        amount));
            }
        }

        sb.append("------------------------------------------"
                + "----------------------------------------")
                .append(System.lineSeparator());
        sb.append(String.format(Locale.GERMAN, "Σύνολο: %,d Ευρώ%n",
                totalStateBudget));
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    // ---------------------------
    // PRIVATE HELPER METHODS
    // ---------------------------

    private int findRowIndexByCode(final String code) {
        if (categoriesData == null) {
            return -1;
        }

        for (int i = 1; i < categoriesData.length; i++) {
            if (categoriesData[i].length > CATEGORY_CODE_COLUMN
                    && categoriesData[i][CATEGORY_CODE_COLUMN].equals(code)) {
                return i;
            }
        }
        return -1;
    }

    private long[] getAmountsForRow(final int rowIndex) {
        long stateBudget;
        long regularBudget;
        long investmentBudget;

        // Check array bounds
        if (categoriesData[rowIndex].length <= CATEGORY_CODE_COLUMN) {
            return new long[]{0, 0, 0};
        }

        final String code = categoriesData[rowIndex][CATEGORY_CODE_COLUMN];

        if ("29".equals(code)) {
            stateBudget = 17283053000L;
            regularBudget = 3183053000L;
            investmentBudget = 14100000000L;
        } else {
            try {
                if (categoriesData[rowIndex].length
                        > CATEGORY_STATE_BUDGET_COLUMN) {
                    stateBudget = Long.parseLong(categoriesData[rowIndex]
                            [CATEGORY_STATE_BUDGET_COLUMN].replace(" ", ""));
                    regularBudget = stateBudget;
                    investmentBudget = 0;
                } else {
                    stateBudget = 0;
                    regularBudget = 0;
                    investmentBudget = 0;
                }
            } catch (final NumberFormatException e) {
                stateBudget = 0;
                regularBudget = 0;
                investmentBudget = 0;
            }
        }

        return new long[]{stateBudget, regularBudget, investmentBudget};
    }
}
