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
    /** The column index for the regular budget in the ministries array. */
    private static final int MINISTRY_REGULAR_COLUMN = 2;
    /** The column index for the total budget (Kratikos) in the ministries array. */
    private static final int MINISTRY_TOTAL_COLUMN = 4;

    /** Internal storage for expense categories data (String[][]). */
    private final String[][] categoriesData;
    /** Internal storage for ministry expenses data (String[][]). */
    private final String[][] ministriesData;

    /** The year of the budget data (fixed for 2025 in this context). */
    private static final int BUDGET_YEAR = 2025;
    
    // Ειδικές τιμές για την κατηγορία 29
    private static final long AMOUNT_29_KRATIKOS = 17283053000L;
    private static final long AMOUNT_29_TAKTIKOS = 3183053000L;
    private static final long AMOUNT_29_EPENDYSEON = 14100000000L;

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
    // DATA MODIFICATION METHOD (για την εισαγωγή αλλαγών)
    // ---------------------------

    /**
     * Updates the amount for a specific expense category code in the internal array.
     * @param code The category code (e.g., "21").
     * @param newAmount The new amount as a long.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateCategoryAmount(final String code, final long newAmount) {
        // Ξεκινάμε από τη 2η γραμμή (index 1) για να παραλείψουμε το header.
        for (int i = 1; i < categoriesData.length; i++) {
            // Ο κωδικός βρίσκεται στη στήλη CATEGORY_CODE_COLUMN
            if (categoriesData[i].length > CATEGORY_CODE_COLUMN
                && categoriesData[i][CATEGORY_CODE_COLUMN].trim().equals(code)) {
                
                // Ενημερώνουμε τη στήλη του ποσού (CATEGORY_AMOUNT_COLUMN)
                if (categoriesData[i].length > CATEGORY_AMOUNT_COLUMN) {
                    // Μετατροπή της long τιμής σε String για αποθήκευση
                    categoriesData[i][CATEGORY_AMOUNT_COLUMN] = String.valueOf(newAmount);
                    return true;
                }
            }
        }
        return false;
    }

    // ---------------------------
    // DISPLAY METHODS
    // ---------------------------

    /**
     * Prints the expense categories list in the required format.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     */
    public void displayCategories(final String budgetType) {
        System.out.println("==================================================");
        System.out.println(">> ΕΞΟΔΑ");
        System.out.println(">> " + budgetType + " ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ");
        System.out.println(">> ΠΙΣΤΩΣΕΙΣ ΚΑΤΑ ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ - ΕΤΟΥΣ " + BUDGET_YEAR);
        System.out.println("==================================================");
        System.out.printf("%-10s %-50s %s%n", "ΚΩΔ.", "ΠΕΡΙΓΡΑΦΗ ΔΑΠΑΝΗΣ", "ΠΟΣΟ (ΕΥΡΩ)");
        System.out.println("--------------------------------------------------");

        // Ξεκινάμε από τη 2η γραμμή (index 1) για να παραλείψουμε το header.
        for (int i = 1; i < categoriesData.length; i++) {
            final String[] row = categoriesData[i];
            
            if (row.length > CATEGORY_AMOUNT_COLUMN) {
                final String code = row[CATEGORY_CODE_COLUMN].trim();
                final String description = row[CATEGORY_DESCRIPTION_COLUMN].trim();
                long amountToDisplay;

                if ("29".equals(code)) {
                    // Ειδικός χειρισμός για την κατηγορία 29
                    amountToDisplay = getAmountForCategory29(budgetType);
                } else {
                    // Κανονική ανάγνωση από τον πίνακα
                    try {
                        // Αφαίρεση τυχόν κενών και χρήση Long.parseLong
                        amountToDisplay = Long.parseLong(row[CATEGORY_AMOUNT_COLUMN].trim());
                    } catch (NumberFormatException e) {
                        amountToDisplay = 0; // Σε περίπτωση σφάλματος ανάγνωσης
                    }
                }
                
                System.out.printf("%-10s %-50s %s%n",
                    code,
                    description,
                    String.format(Locale.GERMAN, "%,d", amountToDisplay));
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    /**
     * Prints the ministry expenses list in the required format.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     */
    public void displayMinistries(final String budgetType) {
        System.out.println("==================================================");
        System.out.println(">> ΕΞΟΔΑ");
        System.out.println(">> " + budgetType + " ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ");
        System.println(">> ΣΥΝΟΠΤΙΚΟΣ ΠΙΝΑΚΑΣ ΠΙΣΤΩΣΕΩΝ ΣΥΝΟΛΙΚΑ ΚΑΤΑ ΦΟΡΕΑ - ΕΤΟΥΣ " + BUDGET_YEAR);
        System.out.println("==================================================");
        System.out.printf("%-10s %-50s %s%n", "ΚΩΔ.", "ΦΟΡΕΑΣ", "ΠΟΣΟ (ΕΥΡΩ)");
        System.out.println("--------------------------------------------------");

        // Ξεκινάμε από τη 2η γραμμή (index 1) για να παραλείψουμε το header.
        for (int i = 1; i < ministriesData.length; i++) {
            final String[] row = ministriesData[i];
            
            // Έλεγχος για τη στήλη του Συνόλου
            if (row.length > MINISTRY_TOTAL_COLUMN) {
                final String code = row[MINISTRY_CODE_COLUMN].trim();
                final String ministry = row[MINISTRY_NAME_COLUMN].trim();
                long displayAmount = getMinistryAmount(row, budgetType);

                if (displayAmount > 0) {
                    System.out.printf("%-10s %-50s %s%n",
                        code,
                        ministry,
                        String.format(Locale.GERMAN, "%,d", displayAmount));
                }
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    /**
     * Helper method to determine the amount for category 29 based on budget type.
     * @param budgetType The budget type.
     * @return The specific amount.
     */
    private long getAmountForCategory29(final String budgetType) {
        if ("ΚΡΑΤΙΚΟΣ".equals(budgetType)) {
            return AMOUNT_29_KRATIKOS;
        } else if ("ΤΑΚΤΙΚΟΣ".equals(budgetType)) {
            return AMOUNT_29_TAKTIKOS;
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
            // Χρειάζεται να καθαρίσουμε την τιμή (π.χ. το "61 88000" από το CSV)
            return Long.parseLong(row[columnIndex].trim().replace(" ", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}