package auebprogramming;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages budget data from CSV files.
 */
public final class RevenueDataManager {

    private final String[][] codes2; // 2-digit codes
    private final String[][] codes3; // 3-digit codes
    private final String[][] codes5; // 5-digit codes
    private final String[][] codes7; // 7-digit codes

    /**
     * Creates a manager and loads all CSV files.
     */
    public RevenueDataManager() {
        this.codes2 = CsvToArray.loadCsvToArray("revenue_categories2_2025.csv");
        this.codes3 = CsvToArray.loadCsvToArray("revenue_categories3_2025.csv");
        this.codes5 = CsvToArray.loadCsvToArray("revenue_categories5_2025.csv");
        this.codes7 = CsvToArray.loadCsvToArray("revenue_categories7_2025.csv");
    }

    /** Returns 2-digit codes. */
    public String[][] get2DigitCodes() {
        return codes2;
    }

    /** Returns 3-digit codes starting with the given prefix. */
    public String[][] get3DigitCodes(final String prefix) {
        return filterByPrefix(codes3, prefix);
    }

    /** Returns 5-digit codes starting with the given prefix. */
    public String[][] get5DigitCodes(final String prefix) {
        return filterByPrefix(codes5, prefix);
    }

    /** Returns 7-digit codes starting with the given prefix. */
    public String[][] get7DigitCodes(final String prefix) {
        return filterByPrefix(codes7, prefix);
    }

    /**
     * Filters a 2D array by the prefix of the first column.
     *
     * @param data   The 2D array
     * @param prefix The prefix to filter by
     * @return Array containing only rows that start with the prefix
     */
    private String[][] filterByPrefix(final String[][] data, final String prefix) {
        final List<String[]> filtered = new ArrayList<>();

        for (String[] row : data) {
            if (row.length > 0 && row[0] != null && row[0].startsWith(prefix)) {
                filtered.add(row);
            }
        }

        return filtered.toArray(new String[0][]);
    }

    /* ============================================================
       VALIDATION METHODS
       ============================================================ */

    /**
     * Validates that the given code contains only digits and has
     * allowed length (2, 3, 5, 7).
     */
    public void validateCodeLength(String code) throws AppException {
        if (code == null || !code.matches("\\d+")) {
            throw new AppException("Ο κωδικός πρέπει να περιέχει μόνο ψηφία.");
        }

        int len = code.length();
        if (len != 2 && len != 3 && len != 5 && len != 7) {
            throw new AppException("Μη έγκυρο μήκος κωδικού: " + code);
        }
    }
}


