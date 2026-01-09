package auebprogramming;

/**
 * Manages loading, searching, and returning state budget data
 * for use in a Graphical User Interface (GUI).
 */
public final class BudgetAnalyzer {

    /** Minimum number of rows required for a detailed budget file. */
    private static final int MIN_DETAILED_ROWS = 5;

    /** Minimum number of rows for Article 2 data. */
    private static final int MIN_ARTICLE2_ROWS = 2;

    /** Data for Article 2. */
    private final String[][] article2Data;

    /** The selected entity code. */
    private int selectedEntityCode;

    /**
     * Constructor. Automatically loads summary data for Article 2.
     */
    public BudgetAnalyzer() {
        this.article2Data = CsvToArray.loadCsvToArray("budget_ministries.csv");
    }

    /**
     * Returns summary data for Article 2 (including header) for JTable display.
     *
     * @return A 2D String array with Article 2 data.
     */
    public String[][] getArticle2Data() {
        if (article2Data.length < MIN_ARTICLE2_ROWS) {
            return new String[0][0];
        }
        return article2Data;
    }

    /**
     * Validates the entity code and returns detailed budget data for the GUI.
     *
     * @param code The four-digit entity code provided by the GUI.
     * @return A 2D String array with detailed budget data.
     * @throws IllegalArgumentException If the code is not found or file is empty.
     */
    public String[][] getDetailedBudget(final int code)
            throws IllegalArgumentException {
        this.selectedEntityCode = code;

        // 1. Check if the code is valid
        if (isCodeValid(code)) {

            // 2. Matching Logic: Code becomes the filename
            final String filename = code + ".csv";

            final String[][] detailedData = CsvToArray
                    .loadCsvToArray(filename);

            // 3. Check if the file was found and is not empty
            if (detailedData == null || detailedData.length < MIN_DETAILED_ROWS) {
                throw new IllegalArgumentException(
                        "Detailed file for code "
                        + code + " was not found or is empty.");
            }

            return detailedData;
        } else {
            // 4. If code is not found in Article 2, throw exception
            throw new IllegalArgumentException("Entity code " + code
                    + " does not correspond to any Article 2 entity.");
        }
    }

    /**
     * Getter for the selected entity code.
     * @return the selected code.
     */
    public int getSelectedEntityCode() {
        return this.selectedEntityCode;
    }

    /**
     * Checks if the given entity code exists in the Article 2 table.
     *
     * @param code The entity code to check.
     * @return true if the code is found, false otherwise.
     */
    private boolean isCodeValid(final int code) {
        final String codeString = String.valueOf(code);
        // Search starting from row 1 (after header)
        for (int i = 1; i < article2Data.length; i++) {
            final String[] row = article2Data[i];
            if (row.length > 0 && row[0].trim().equals(codeString)) {
                return true;
            }
        }
        return false;
    }
}
