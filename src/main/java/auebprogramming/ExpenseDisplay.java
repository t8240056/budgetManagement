package auebprogramming;

import java.util.Locale;

/**
 * ExpenseDisplay manages, stores, and displays state budget expense data
 * using native String arrays.
 */
public final class ExpenseDisplay {
    // ... (Constants remain the same) ...

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
    
    // ΝΕΑ ΣΤΑΘΕΡΑ: Για καλύτερη μορφοποίηση (αύξηση πλάτους)
    private static final int DISPLAY_COLUMN_WIDTH = 60;
    // ... (other fields remain the same) ...

    /**
     * Prints the expense categories list in the required format.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     */
    public void displayCategories(final String budgetType) {
        long grandTotal = 0;
        
        System.out.println("==================================================");
        System.out.println(">> ΕΞΟΔΑ");
        System.out.println(">> " + budgetType + " ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ");
        System.out.println(">> ΠΙΣΤΩΣΕΙΣ ΚΑΤΑ ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ - ΕΤΟΥΣ " + BUDGET_YEAR);
        System.out.println("==================================================");
        // Χρήση νέας σταθεράς πλάτους για καλύτερη στοίχιση
        System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n", 
            "ΚΩΔ.", "ΠΕΡΙΓΡΑΦΗ ΔΑΠΑΝΗΣ", "ΠΟΣΟ (ΕΥΡΩ)");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

        // Ξεκινάμε από τη 2η γραμμή (index 1) για να παραλείψουμε το header.
        for (int i = 1; i < categoriesData.length; i++) {
            final String[] row = categoriesData[i];
            
            if (row.length > CATEGORY_AMOUNT_COLUMN) {
                final String code = row[CATEGORY_CODE_COLUMN];
                final String description = row[CATEGORY_DESCRIPTION_COLUMN];
                long amountToDisplay;

                if ("29".equals(code)) {
                    amountToDisplay = getAmountForCategory29(budgetType);
                } else if ("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ".equals(budgetType)) {
                    // ΔΙΟΡΘΩΣΗ 3: Όλες οι άλλες δαπάνες έχουν 0 Επενδύσεις
                    amountToDisplay = 0;
                } else {
                    // Κανονική ανάγνωση για Κρατικό/Τακτικό (αφού είναι ίσες)
                    try {
                        amountToDisplay = Long.parseLong(row[CATEGORY_AMOUNT_COLUMN].replace(" ", ""));
                    } catch (NumberFormatException e) {
                        amountToDisplay = 0;
                    }
                }
                
                // Προσθήκη στο σύνολο
                grandTotal += amountToDisplay;

                System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
                    code,
                    description,
                    String.format(Locale.GERMAN, "%,d", amountToDisplay));
            }
        }
        
        // ΛΟΓΙΚΗ 7: Εμφάνιση συνόλου
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
            "", "**ΣΥΝΟΛΟ**", String.format(Locale.GERMAN, "%,d", grandTotal));
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    /**
     * Prints the ministry expenses list in the required format.
     * @param budgetType The type of budget (e.g., ΚΡΑΤΙΚΟΣ).
     */
    public void displayMinistries(final String budgetType) {
        long grandTotal = 0;

        System.out.println("==================================================");
        System.out.println(">> ΕΞΟΔΑ");
        System.out.println(">> " + budgetType + " ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ");
        System.out.println(">> ΣΥΝΟΠΤΙΚΟΣ ΠΙΝΑΚΑΣ ΠΙΣΤΩΣΕΩΝ ΣΥΝΟΛΙΚΑ ΚΑΤΑ ΦΟΡΕΑ - ΕΤΟΥΣ " + BUDGET_YEAR);
        System.out.println("==================================================");
        // Χρήση νέας σταθεράς πλάτους για καλύτερη στοίχιση
        System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n", 
            "ΚΩΔ.", "ΦΟΡΕΑΣ", "ΠΟΣΟ (ΕΥΡΩ)");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

        // Ξεκινάμε από τη 2η γραμμή (index 1) για να παραλείψουμε το header.
        for (int i = 1; i < ministriesData.length; i++) {
            final String[] row = ministriesData[i];
            
            if (row.length > MINISTRY_TOTAL_COLUMN) {
                final String code = row[MINISTRY_CODE_COLUMN];
                final String ministry = row[MINISTRY_NAME_COLUMN];
                long displayAmount = getMinistryAmount(row, budgetType);

                // ΔΙΟΡΘΩΣΗ 5: Μην εμφανίζεις γραμμές αν το ποσό είναι 0 (για τον Τακτικό)
                // Ο κωδικός 1020 στον Τακτικό δεν εμφανίζεται γιατί το ποσό του είναι μηδενικό;
                // Οχι, το 1020 έχει 5.594.000.000. Το πρόβλημα ήταν οι σταθερές και λύθηκε στο getMinistryAmount.
                // Απλά δεν εμφανίζουμε αν το ποσό είναι 0.
                if (displayAmount > 0) {
                    grandTotal += displayAmount;
                    System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
                        code,
                        ministry,
                        String.format(Locale.GERMAN, "%,d", displayAmount));
                }
            }
        }
        
        // ΛΟΓΙΚΗ 7: Εμφάνιση συνόλου
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-" + DISPLAY_COLUMN_WIDTH + "s %s%n",
            "", "**ΣΥΝΟΛΟ**", String.format(Locale.GERMAN, "%,d", grandTotal));
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    // ... (updateCategoryAmount remains the same) ...
    // ... (Constructor remains the same) ...

    /**
     * Helper method to extract the correct ministry amount based on budget type.
     * @param row The array row containing ministry data.
     * @param budgetType The budget type.
     * @return The amount.
     */
    private long getMinistryAmount(final String[] row, final String budgetType) {
        int columnIndex;
        if ("ΚΡΑΤΙΚΟΣ".equals(budgetType)) {
            // ΔΙΟΡΘΩΣΗ 4: Κρατικός Πρ. = Σύνολο (στήλη 4)
            columnIndex = MINISTRY_TOTAL_COLUMN; 
        } else if ("ΤΑΚΤΙΚΟΣ".equals(budgetType)) {
            // Τακτικός Πρ. (στήλη 2)
            columnIndex = MINISTRY_REGULAR_COLUMN;
        } else if ("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ".equals(budgetType)) {
            // ΠΔΕ (στήλη 3)
            columnIndex = MINISTRY_INVESTMENT_COLUMN; 
        } else {
            return 0;
        }

        try {
            // Χρησιμοποιούμε replace(" ", "") για τα ενδιάμεσα κενά αριθμών (π.χ. "61 88000")
            return Long.parseLong(row[columnIndex].replace(" ", ""));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Πιθανώς λείπει η στήλη ή η τιμή είναι άκυρη
            return 0;
        }
    }
}
