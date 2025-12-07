package auebprogramming;

/**
 * Utility class for managing the structure of the budget data arrays.
 * Handles adding and removing rows (categories) dynamically.
 * Note: These operations return a new array instance.
 */
public final class BudgetStructureManager {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BudgetStructureManager() {
        // Utility class
    }

    /**
     * Adds a new row (category) to the data array.
     * Creates a new array with an increased size and copies existing data.
     *
     * @param originalData The original 2D String array.
     * @param newRowData   The array representing the new row to add.
     * @return             A new 2D array containing the added row.
     */
    public static String[][] addRow(final String[][] originalData, final String[] newRowData) {
        final int rows = originalData.length;
        final int cols = originalData[0].length;
        final String[][] newData = new String[rows + 1][cols];

        // Copy old data
        for (int i = 0; i < rows; i++) {
            newData[i] = originalData[i];
        }

        // Add new row at the end
        newData[rows] = newRowData;

        return newData;
    }

    /**
     * Deletes a row identified by a specific code.
     * Creates a new array with a decreased size.
     *
     * @param originalData The original 2D String array.
     * @param code         The code of the row to delete.
     * @param codeCol      The column index where the codes are located.
     * @return             A new 2D array with the row removed, or the original
     * array if the code was not found.
     */
    public static String[][] deleteRow(final String[][] originalData,
                                       final String code, final int codeCol) {
        int indexToDelete = -1;

        // Find the row index
        for (int i = 1; i < originalData.length; i++) {
            if (originalData[i][codeCol].equals(code)) {
                indexToDelete = i;
                break;
            }
        }

        // If not found, return original data
        if (indexToDelete == -1) {
            return originalData;
        }

        final int rows = originalData.length;
        final int cols = originalData[0].length;
        final String[][] newData = new String[rows - 1][cols];

        int newRowIndex = 0;
        for (int i = 0; i < rows; i++) {
            if (i == indexToDelete) {
                continue; // Skip the deleted row
            }
            newData[newRowIndex++] = originalData[i];
        }

        return newData;
    }
}
