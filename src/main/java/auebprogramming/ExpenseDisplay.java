package auebprogramming;

import java.util.Locale;

/**
 * ExpenseDisplay manages, stores, and displays state budget expense data
 * using native String arrays.
 */
public final class ExpenseDisplay {
    /** The column index for the code in the categories array. */
    private static final int CATEGORY_CODE_COLUMN = 1;
    /** The column index for the description in the categories array. */
    private static final int CATEGORY_DESCRIPTION_COLUMN = 2;
    /** The column index for the amount in the categories array. */
    private static final int CATEGORY_AMOUNT_COLUMN = 3;

    /** The column index for the code in the ministries array. */
    private static final int MINISTRY_CODE_COLUMN = 0;
    /** The column index for the ministry name in the ministries array (Ministry/Agency). */
    private static final int MINISTRY_NAME_COLUMN = 1;
    /** The column index for the regular budget in the ministries array. */
    private static final int MINISTRY_REGULAR_COLUMN = 2;
    /** The column index for the investment budget in the ministries array. */
    private static final int MINISTRY_INVESTMENT_COLUMN = 3;
    /** The column index for the total budget (Kratikos) in the ministries array. */
    private static final int MINISTRY_TOTAL_COLUMN = 4;

    // Constant for better formatting (increased width)
    private static final int DISPLAY_COLUMN_WIDTH = 70;

    /** The year of the budget data. */
    private static final int BUDGET_YEAR = 2025;

    // Specific amounts for category 29
    private static final long AMOUNT_29_KRATIKOS = 17283053000L;
    private static final long AMOUNT_29_TAKTIKOS = 3183053000L;
    private static final long AMOUNT_29_EPENDYSEON = 14100000000L;

    /** Internal storage for expense categories data (String[][]). */
    private final String[][] categoriesData;
    /** Internal storage for ministry expenses data (String[][]). */
    private final String[][] ministriesData;

    // ---------------------------
    // CONSTRUCTOR
    // ---------------------------

    /**
     * Constructor for ExpenseDisplay. Loads data from the specified CSV files
     * using the CsvToArray utility and stores it in String arrays.
     * @param categoriesFile The name of the categories CSV file.
     * @param ministriesFile The name of the ministries CSV file.
     */
    public ExpenseDisplay(final String categoriesFile, final String ministriesFile) {
        this.categoriesData = CsvToArray.loadCsvToArray(categoriesFile);
        this.ministriesData = CsvToArray.loadCsvToArray(ministriesFile);
    }

    // ---------------------------
    // DATA MODIFICATION METHOD
    // ---------------------------

    /**
     * Updates the amount for a specific expense category code in the internal array.
     * @param code The category code (e.g., "21").
     * @param newAmount The new amount as a long.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateCategoryAmount(final String code, final long newAmount) {
        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < categoriesData.length; i++) {
            // The code is in the CATEGORY_CODE_COLUMN
            if (categoriesData[i].length > CATEGORY_CODE_COLUMN
                && categoriesData[i][CATEGORY_CODE_COLUMN].trim().equals(code)) {

                // Update the amount column (CATEGORY_AMOUNT_COLUMN)
                if (categoriesData[i].length > CATEGORY_AMOUNT_COLUMN) {
                    // Convert the long value to a String for storage
                    categoriesData[i][CATEGORY_AMOUNT_COLUMN] = String.valueOf(newAmount);
                    return true;
                }
            }
        }
        return false;
    }

    // ---------------------------
    // GUI REPORT METHODS (Replacing System.out)
    // ---------------------------

    /**
     * Generates the expense categories report in the required format as a single String.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     * @return The formatted report content as a String.
     */
    public String getCategoriesReport(final String budgetType) {
        final StringBuilder sb = new StringBuilder();
        long grandTotal = 0;

        // Append Header
        sb.append("==================================================").append(System.lineSeparator());
        sb.append(">> ΕΞΟΔΑ").append(System.lineSeparator());
        sb.append(">> ").append(budgetType).append(" ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ").append(System.lineSeparator());
        sb.append(">> ΠΙΣΤΩΣΕΙΣ ΚΑΤΑ ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ - ΕΤΟΥΣ ").append(BUDGET_YEAR).append(System.lineSeparator());
        sb.append("==================================================").append(System.lineSeparator());
        sb.append(String.format("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
            "ΚΩΔ.", "ΠΕΡΙΓΡΑΦΗ ΔΑΠΑΝΗΣ", "ΠΟΣΟ (ΕΥΡΩ)"));
        sb.append("-------------------------------------------------------------------------------------------------------------------------------------").append(System.lineSeparator());

        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < categoriesData.length; i++) {
            final String[] row = categoriesData[i];

            if (row.length > CATEGORY_AMOUNT_COLUMN) {
                final String code = row[CATEGORY_CODE_COLUMN];
                final String description = row[CATEGORY_DESCRIPTION_COLUMN];
                long amountToDisplay;

                if ("29".equals(code)) {
                    amountToDisplay = getAmountForCategory29(budgetType);
                } else if ("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ".equals(budgetType)) {
                    // All other expenses have 0 Investments
                    amountToDisplay = 0;
                } else {
                    // Normal reading for State/Regular Budget
                    try {
                        amountToDisplay = Long.parseLong(row[CATEGORY_AMOUNT_COLUMN].replace(" ", ""));
                    } catch (NumberFormatException e) {
                        amountToDisplay = 0;
                    }
                }

                // Add to the total
                grandTotal += amountToDisplay;

                sb.append(String.format("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
                    code,
                    description,
                    String.format(Locale.GERMAN, "%,d", amountToDisplay)));
            }
        }

        // Append total (Left alignment)
        sb.append("-------------------------------------------------------------------------------------------------------------------------------------").append(System.lineSeparator());
        sb.append(String.format("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
            "",
            "**ΣΥΝΟΛΟ**",
            String.format(Locale.GERMAN, "%,d", grandTotal)));
        sb.append("-------------------------------------------------------------------------------------------------------------------------------------").append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Generates the ministry expenses report in the required format as a single String.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     * @return The formatted report content as a String.
     */
    public String getMinistriesReport(final String budgetType) {
        final StringBuilder sb = new StringBuilder();
        long grandTotal = 0;

        // Append Header
        sb.append("==================================================").append(System.lineSeparator());
        sb.append(">> ΕΞΟΔΑ").append(System.lineSeparator());
        sb.append(">> ").append(budgetType).append(" ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ").append(System.lineSeparator());
        sb.append(">> ΣΥΝΟΠΤΙΚΟΣ ΠΙΝΑΚΑΣ ΠΙΣΤΩΣΕΩΝ ΣΥΝΟΛΙΚΑ ΚΑΤΑ ΦΟΡΕΑ - ΕΤΟΥΣ ").append(BUDGET_YEAR).append(System.lineSeparator());
        sb.append("==================================================").append(System.lineSeparator());
        sb.append(String.format("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
            "ΚΩΔ.", "ΦΟΡΕΑΣ", "ΠΟΣΟ (ΕΥΡΩ)"));
        sb.append("-------------------------------------------------------------------------------------------------------------------------------------").append(System.lineSeparator());

        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < ministriesData.length; i++) {
            final String[] row = ministriesData[i];

            if (row.length > MINISTRY_TOTAL_COLUMN) {
                final String code = row[MINISTRY_CODE_COLUMN];
                final String ministry = row[MINISTRY_NAME_COLUMN];
                long displayAmount = getMinistryAmount(row, budgetType);

                if (displayAmount > 0) {
                    grandTotal += displayAmount;

                    // Append row output to StringBuilder
                    sb.append(String.format("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
                        code,
                        ministry,
                        String.format(Locale.GERMAN, "%,d", displayAmount)));
                }
            }
        }
        return sb.toString();
    }

     /**
     * Generates all six possible expense reports (Categories and Ministries for
     * State, Regular, and Investment Budgets) and returns them in a String array.
     * The return array is ordered as follows:
     * [0] Categories Report (KRATIKOS)
     * [1] Categories Report (TAKTIKOS)
     * [2] Categories Report (EPENDYSEON)
     * [3] Ministries Report (KRATIKOS)
     * [4] Ministries Report (TAKTIKOS)
     * [5] Ministries Report (EPENDYSEON)
     * * @return A String array containing the six formatted reports.
     */
    public String[] getAllExpenseReports() {

        // 1. Define the 3 Budget Types (Used as method arguments)
        final String kratikos = "ΚΡΑΤΙΚΟΣ";
        final String taktikos = "ΤΑΚΤΙΚΟΣ";
        final String ependyseon = "ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ";

        // 2. Create the array for the 6 results
        final String[] reports = new String[6];

        // 3. Retrieve 3 Expense Category Reports
        reports[0] = this.getCategoriesReport(kratikos);
        reports[1] = this.getCategoriesReport(taktikos);
        reports[2] = this.getCategoriesReport(ependyseon);

        // 4. Retrieve 3 Ministry Expense Reports
        reports[3] = this.getMinistriesReport(kratikos);
        reports[4] = this.getMinistriesReport(taktikos);
        reports[5] = this.getMinistriesReport(ependyseon);

        return reports;
    }

    /**
     * Helper method to determine the amount for category 29 based on budget type.
     * @param budgetType The budget type.
     * @return The specific amount.
     */
    private long getAmountForCategory29(final String budgetType) {
        if ("ΚΡΑΤΙΚΟΣ".equals(budgetType)) {
            return AMOUNT_29_KRATIKOS;

        } else if ("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ".equals(budgetType)) {
            return AMOUNT_29_EPENDYSEON;
        }
        return 0;
    }

    /**
     * Helper method to extract the correct ministry amount based on budget type.
     * @param row The array row containing ministry data.
     * @param budgetType The budget type.
     * @return The amount.
     */
    private long getMinistryAmount(final String[] row, final String budgetType) {
        int columnIndex;
        if ("ΚΡΑΤΙΚΟΣ".equals(budgetType)) {
            columnIndex = MINISTRY_TOTAL_COLUMN;
        } else if ("ΤΑΚΤΙΚΟΣ".equals(budgetType)) {
            columnIndex = MINISTRY_REGULAR_COLUMN;
        } else if ("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ".equals(budgetType)) {
            columnIndex = MINISTRY_INVESTMENT_COLUMN;
        } else {
            return 0;
        }

        try {
            // We use replace(" ", "") to handle internal number spaces (e.g., "61 88000")
            return Long.parseLong(row[columnIndex].replace(" ", ""));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
