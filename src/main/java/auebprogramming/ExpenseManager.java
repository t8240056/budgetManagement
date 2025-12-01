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
     * @param categoriesFile The filename of the categories CSV.
     */
    public ExpenseManager(final String categoriesFile) {
        // Use CsvToArray to load data from the classpath
        this.categoriesData = CsvToArray.loadCsvToArray(categoriesFile);
    }
    
    // ---------------------------
    // PUBLIC DISPLAY METHODS
    // ---------------------------

    /**
     * Display all available expense categories with their codes.
     */
    public void showCategories() {
        System.out.println("CODE\tEXPENSE NAME");
        // Start from the 2nd row (index 1) to skip the header.
        for (int i = 1; i < categoriesData.length; i++) {
            final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
            final String name = categoriesData[i][CATEGORY_DESCRIPTION_COLUMN];
            System.out.printf("%s\t%s%n", code, name);
        }
    }

    /**
     * Display details for one or more expense codes (State Budget, Regular, Investment).
     * @param codes One or more expense codes (e.g., "21", "23").
     */
    public void showExpenseDetails(final String... codes) {
        for (final String code : codes) {
            final int index = findRowIndexByCode(code);

            if (index != -1) {
                // Use getAmountsForRow to safely extract the values
                final long[] amounts = getAmountsForRow(index);
                
                final String name = categoriesData[index][CATEGORY_DESCRIPTION_COLUMN];
                
                System.out.println("\n==============================");
                System.out.println("CODE: " + categoriesData[index][CATEGORY_CODE_COLUMN]);
                System.out.println("NAME: " + name);
                System.out.println("------------------------------");
                
                // Use Locale.GERMAN for correct thousand separators
                System.out.printf(Locale.GERMAN, "State Budget       : %,d €%n", amounts[0]);
                System.out.printf(Locale.GERMAN, "Regular Budget     : %,d €%n", amounts[1]);
                System.out.printf(Locale.GERMAN, "Investment Budget  : %,d €%n", amounts[2]);
                System.out.println("==============================");

            } else {
                System.out.println("\n" + code + " : Invalid code");
            }
        }
    }


    /**
     * Display all expense categories with their code, name, and State Budget amount.
     */
    public void showExpenses() {
        long totalStateBudget = 0;

        System.out.println("1. EXPENSES"); 
        System.out.println();
        // Adjust formatting to match showRevenues style (assuming width 60)
        System.out.printf("%-5s %-60s %s%n", "CODE.", "DESCRIPTION", "AMOUNT (EUR)");
        System.out.println("----------------------------------------------------------------------------------");


        // Print each row in the requested format
        for (int i = 1; i < categoriesData.length; i++) {
            final String code = categoriesData[i][CATEGORY_CODE_COLUMN];
            final String name = categoriesData[i][CATEGORY_DESCRIPTION_COLUMN];
            final long[] amounts = getAmountsForRow(i);
            final long amount = amounts[0]; // State Budget amount

            totalStateBudget += amount;

            System.out.printf(Locale.GERMAN, "%-5s %-60s %,15d%n",
                    code + ".",
                    name,
                    amount
            );
        }

        System.out.println("----------------------------------------------------------------------------------");
        // Display the total amount of expenses (State Budget)
        System.out.printf(Locale.GERMAN, "Total: %,d EUR%n", totalStateBudget);
        System.out.println();
    }
    
    // ---------------------------
    // PRIVATE HELPER METHODS
    // ---------------------------
    
    /**
     * Finds the row index in the categoriesData array by expense code.
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
        
        return new long[]{stateBudget, regularBudget, investmentBudget};
    }
}
