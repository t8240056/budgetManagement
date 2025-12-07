package auebprogramming;

/**
 * Utility class for modifying budget amounts within a 2D String array.
 * This class provides methods to update specific values based on codes.
 */
public final class BudgetEditor {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetEditor() {
        // Utility class
    }

    /**
     * Updates the amount in a specific row identified by the category code.
     *
     * @param data      The 2D String array containing the budget data.
     * @param code      The unique code to search for (e.g., "21").
     * @param codeCol   The column index where the codes are located.
     * @param amountCol The column index where the amounts are located.
     * @param newAmount The new amount to set.
     * @return          True if the update was successful, false if the code was not found.
     */
    public static boolean updateAmount(final String[][] data, final String code,
                                       final int codeCol, final int amountCol,
                                       final long newAmount) {
        // Start from 1 to skip the header row
        for (int i = 1; i < data.length; i++) {
            // Check if the row has enough columns and matches the code
            if (data[i].length > codeCol && data[i][codeCol].trim().equals(code)) {
                data[i][amountCol] = String.valueOf(newAmount);
                return true;
            }
        }
        return false;
    }
}
