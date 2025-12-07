package auebprogramming;

/**
 * Utility class for managing fund transfers between budget categories.
 * Handles the validation of funds and the atomic update of two categories.
 */
public final class BudgetTransferManager {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetTransferManager() {
        // Utility class
    }

    /**
     * Transfers a specific amount from one category to another.
     *
     * @param data      The 2D String array containing the budget data.
     * @param fromCode  The code of the source category.
     * @param toCode    The code of the destination category.
     * @param codeCol   The column index where the codes are located.
     * @param amountCol The column index where the amounts are located.
     * @param amount    The amount to transfer in Euros.
     * @return          True if the transfer was successful, false otherwise.
     */
    public static boolean transfer(final String[][] data, final String fromCode,
                                   final String toCode, final int codeCol,
                                   final int amountCol, final long amount) {
        int fromIndex = -1;
        int toIndex = -1;

        // 1. Locate the indices for both source and destination
        for (int i = 1; i < data.length; i++) {
            final String currentCode = data[i][codeCol].trim();
            if (currentCode.equals(fromCode)) {
                fromIndex = i;
            } else if (currentCode.equals(toCode)) {
                toIndex = i;
            }
        }

        // Check if both codes were found
        if (fromIndex == -1 || toIndex == -1) {
            return false;
        }

        // 2. Parse amounts and check balance
        try {
            final long currentFrom = parseAmount(data[fromIndex][amountCol]);
            final long currentTo = parseAmount(data[toIndex][amountCol]);

            if (currentFrom < amount) {
                return false; // Insufficient funds
            }

            // 3. Execute transfer
            data[fromIndex][amountCol] = String.valueOf(currentFrom - amount);
            data[toIndex][amountCol] = String.valueOf(currentTo + amount);

            return true;

        } catch (final NumberFormatException e) {
            // Log error if needed, or strictly return false
            return false;
        }
    }

    /**
     * Helper method to parse amounts from String format.
     * Removes spaces and thousands separators.
     *
     * @param amountStr The string representation of the amount.
     * @return          The amount as a long.
     * @throws NumberFormatException If the string cannot be parsed.
     */
    private static long parseAmount(final String amountStr) {
        return Long.parseLong(amountStr.replace(" ", "")
                                       .replace(".", "")
                                       .replace(",", ""));
    }
}
